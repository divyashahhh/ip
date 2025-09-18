package cs2103;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Main application that wires Ui, Storage, Parser, and TaskList together.
 */
public class Paneer  {

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private final ArrayDeque<List<Task>> history = new ArrayDeque<>();

    public Paneer(String filePath) {
        assert filePath != null && !filePath.isBlank() : "Paneer filePath must not be empty";
        this.ui = new Ui();
        this.storage = new Storage(Paths.get(filePath));
        this.tasks = new TaskList(storage.load());
        assert ui != null && storage != null && tasks != null
                : "Paneer requires ui, storage, and tasks initialized";

        // initial snapshot
        history.push(tasks.snapshot());

    }

    /* GUI */

    /** Used by MainWindow to get a response to display in the dialog. */
    public String getResponse(String input) {
        try {
            String raw = input == null ? "" : input.trim();
            // Special hidden command used by GUI to apply an edit replacement
            if (raw.startsWith("__APPLY_EDIT__ ")) {
                String[] parts = raw.split(" ", 3);
                int idx = Integer.parseInt(parts[1]);
                String replacement = parts.length >= 3 ? parts[2] : "";
                if (replacement.isEmpty()) {
                    return "No replacement provided — my spices need substance!";
                }
                history.push(tasks.snapshot());
                // Replace: interpret replacement as a fresh command that creates a task;
                // if it is todo/deadline/event, we swap it into the index.
                Parser.ParsedCommand npc = Parser.parse(replacement);
                Task newTask;
                switch (npc.type) {
                    case ADD_TODO:
                        newTask = new ToDos(npc.desc);
                        break;
                    case ADD_DEADLINE:
                        newTask = new Deadline(npc.desc, npc.when1);
                        break;
                    case ADD_EVENT:
                        newTask = new Event(npc.desc, npc.when1, npc.when2);
                        break;
                    default:
                        return "Please enter a task command (todo/deadline/event) — not just chutney.";
                }
                Task was = tasks.get(idx);
                if (was.isDone) {
                    newTask.markDone();
                }
                tasks.set(idx, newTask);
                storage.save(tasks.asUnmodifiableList());
                return "Replaced task " + (idx + 1) + ":\n  " + newTask + "\nTaste test approved.";
            }

            Parser.ParsedCommand pc = Parser.parse(raw);

            switch (pc.type) {
                case UNDO: {
                    if (history.size() <= 1) {
                        return "Nothing to undo — the pan is already clean.";
                    }
                    // pop current state, revert to previous
                    history.pop();
                    List<Task> prev = new ArrayList<>(history.peek());
                    tasks.restore(prev);
                    storage.save(tasks.asUnmodifiableList());
                    return "Undid last change — like scraping the burnt bits off the tava.";
                }
                case EXIT:
                    return "Byeeee! Paneer shall serve you another day — garma garam!";


                case LIST:
                    return Ui.formatList(tasks.asUnmodifiableList());

                case FIND: {
                    var matches = tasks.find(pc.desc);
                    return Ui.formatFind(matches);
                }

                case MARK: {
                    history.push(tasks.snapshot());
                    Task t = tasks.mark(pc.index);
                    storage.save(tasks.asUnmodifiableList());
                    return "Masaledaar! Marked as done:\n  " + t;
                }

                case UNMARK: {
                    history.push(tasks.snapshot());
                    Task t = tasks.unmark(pc.index);
                    storage.save(tasks.asUnmodifiableList());
                    return "Keeping it on simmer — marked as not done yet:\n  " + t;
                }

                case DELETE: {
                    history.push(tasks.snapshot());
                    Task removed = tasks.remove(pc.index);
                    storage.save(tasks.asUnmodifiableList());
                    return "Removed from the thali:\n  " + removed
                            + "\nNow you have " + tasks.size() + " items on your platter.";
                }

                case ADD_TODO: {
                    history.push(tasks.snapshot());
                    Task t = tasks.add(new ToDos(pc.desc));
                    storage.save(tasks.asUnmodifiableList());
                    return "Freshly added to the menu:\n  " + t
                            + "\nNow you have " + tasks.size() + " items on your platter.";
                }

                case ADD_DEADLINE: {
                    try {
                        history.push(tasks.snapshot());
                        Task t = tasks.add(new Deadline(pc.desc, pc.when1));
                        storage.save(tasks.asUnmodifiableList());
                        return "Freshly added to the menu:\n  " + t
                                + "\nNow you have " + tasks.size() + " items on your platter.";
                    } catch (DateTimeParseException e) {
                        return "☹ OOPS! That date is undercooked. Use 2019-12-02 or 2/12/2019 (or a weekday name).";
                    }
                }

                case ADD_EVENT: {
                    try {
                        history.push(tasks.snapshot());
                        Task t = tasks.add(new Event(pc.desc, pc.when1, pc.when2));
                        storage.save(tasks.asUnmodifiableList());
                        return "Freshly added to the menu:\n  " + t
                                + "\nNow you have " + tasks.size() + " items on your platter.";
                    } catch (DateTimeParseException e) {
                        return "☹ OOPS! Those timings are too salty. Use 2019-12-02 1400 (or 2019-12-02 14:00).";
                    }
                }

                case EDIT: {
                    // Interactive entry if no args were supplied in Parser
                    if (pc.desc == null && pc.when1 == null && pc.when2 == null) {
                        return "__PANEER_PROMPT_EDIT__" + pc.index; // special token for GUI
                    }
                    history.push(tasks.snapshot());
                    Task current = tasks.get(pc.index);
                    Task updated = current;
                    if (current instanceof Deadline) {
                        String newDesc = pc.desc != null ? pc.desc : current.getDescription();
                        String by = pc.when2 != null ? pc.when2 : ((Deadline) current).getByFormatted();
                        updated = new Deadline(newDesc, by);
                    } else if (current instanceof Event) {
                        String newDesc = pc.desc != null ? pc.desc : current.getDescription();
                        String from = pc.when1 != null ? pc.when1 : ((Event) current).getFromFormatted();
                        String to = pc.when2 != null ? pc.when2 : ((Event) current).getToFormatted();
                        updated = new Event(newDesc, from, to);
                    } else {
                        String newDesc = pc.desc != null ? pc.desc : current.getDescription();
                        updated = new ToDos(newDesc);
                    }
                    if (current.isDone) {
                        updated.markDone();
                    }
                    tasks.set(pc.index, updated);
                    storage.save(tasks.asUnmodifiableList());
                    return "Updated the recipe:\n  " + updated;
                }

                case SORT: {
                    List<Task> sorted = tasks.sortedByDeadline();
                    return Ui.formatSorted(sorted);
                }

                default:
                    return "Unknown command — that recipe isn’t on the menu.";
            }

        } catch (PaneerException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Paneer slipped on a chilli: " + e.getMessage();
        }
    }

    /** Used by MainWindow to decide when to close the app. */
    public boolean shouldExit(String input) {

        try {
            Parser.ParsedCommand pc = Parser.parse(input == null ? "" : input.trim());
            return pc.type == Parser.ParsedCommand.Type.EXIT;
        } catch (Exception ignored) {
            return false;
        }
    }

    /*  CLI  */

    /** Runs the console app (text-UI). */
    public void run() {
        ui.showWelcome();

        if (tasks.size() > 0) {
            ui.showList(tasks.asUnmodifiableList());
        }

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Parser.ParsedCommand pc = Parser.parse(fullCommand);

                switch (pc.type) {
                    case EXIT:
                        ui.showExit();
                        isExit = true;
                        break;

                    case LIST:
                        ui.showList(tasks.asUnmodifiableList());
                        break;

                    case MARK: {
                        Task t = tasks.mark(pc.index);
                        storage.save(tasks.asUnmodifiableList());
                        ui.showMark(t);
                        break;
                    }

                    case UNMARK: {
                        Task t = tasks.unmark(pc.index);
                        storage.save(tasks.asUnmodifiableList());
                        ui.showUnmark(t);
                        break;
                    }

                    case DELETE: {
                        Task removed = tasks.remove(pc.index);
                        storage.save(tasks.asUnmodifiableList());
                        ui.showRemove(removed, tasks.size());
                        break;
                    }

                    case ADD_TODO: {
                        Task t = tasks.add(new ToDos(pc.desc));
                        storage.save(tasks.asUnmodifiableList());
                        ui.showAdd(t, tasks.size());
                        break;
                    }

                    case ADD_DEADLINE: {
                        try {
                            Task t = tasks.add(new Deadline(pc.desc, pc.when1));
                            storage.save(tasks.asUnmodifiableList());
                            ui.showAdd(t, tasks.size());
                        } catch (DateTimeParseException e) {
                            ui.showError("☹ OOPS! Date must be like 2019-12-02 or 2/12/2019 (or a weekday name).");
                        }
                        break;
                    }

                    case ADD_EVENT: {
                        try {
                            Task t = tasks.add(new Event(pc.desc, pc.when1, pc.when2));
                            storage.save(tasks.asUnmodifiableList());
                            ui.showAdd(t, tasks.size());
                        } catch (DateTimeParseException e) {
                            ui.showError("☹ OOPS! Times must be like 2019-12-02 1400 (or 2019-12-02 14:00).");
                        }
                        break;
                    }
                    case SORT: {
                        List<Task> sorted = tasks.sortedByDeadline();
                        ui.showSorted(sorted);
                        break;
                    }

                    case FIND: {
                        var matches = tasks.find(pc.desc);
                        ui.showFind(matches);
                        break;
                    }

                    default:
                        ui.showError("Unknown command.");
                }

            } catch (PaneerException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Path cwd = Paths.get(System.getProperty("user.dir"));
        Path projectRoot = cwd.getFileName().toString().equals("text-ui-test") ? cwd.getParent() : cwd;
        String savePath = projectRoot.resolve("data").resolve("paneer.txt").toString();
        assert cwd != null : "cwd must resolve";
        assert projectRoot != null : "projectRoot must resolve";
        assert savePath != null && !savePath.isBlank() : "savePath must not be blank";
        new Paneer(savePath).run();
    }
}