package cs2103;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

/**
 * Main application that wires Ui, Storage, Parser, and TaskList together.
 */
public class Paneer {

    private final UI ui;
    private final Storage storage;
    private final TaskList tasks;

    public Paneer(String filePath) {
        assert filePath != null && !filePath.isBlank() : "Paneer filePath must not be empty";
        this.ui = new UI();
        this.storage = new Storage(Paths.get(filePath));
        TaskList loaded = new TaskList(storage.load());
        this.tasks = loaded;
        assert ui != null && storage != null && tasks != null
                : "Paneer requires ui, storage, and tasks initialized";

    }

    /* GUI */

    /** Used by MainWindow to get a response to display in the dialog. */
    public String getResponse(String input) {
        try {
            Parser.ParsedCommand pc = Parser.parse(input == null ? "" : input.trim());

            switch (pc.type) {
                case EXIT:
                    return "Byeeee! Paneer shall serve you another day!";


                case LIST:
                    return UI.formatList(tasks.asUnmodifiableList());

                case FIND: {
                    var matches = tasks.find(pc.desc);
                    return UI.formatFind(matches);
                }

                case MARK: {
                    Task t = tasks.mark(pc.index);
                    storage.save(tasks.asUnmodifiableList());
                    return "Nice! I've marked this task as done:\n  " + t;
                }

                case UNMARK: {
                    Task t = tasks.unmark(pc.index);
                    storage.save(tasks.asUnmodifiableList());
                    return "OK, I've marked this task as not done yet:\n  " + t;
                }

                case DELETE: {
                    Task removed = tasks.remove(pc.index);
                    storage.save(tasks.asUnmodifiableList());
                    return "Noted. I've removed this task:\n  " + removed
                            + "\nNow you have " + tasks.size() + " tasks in the list.";
                }

                case ADD_TODO: {
                    Task t = tasks.add(new ToDos(pc.desc));
                    storage.save(tasks.asUnmodifiableList());
                    return "Got it. I've added this task:\n  " + t
                            + "\nNow you have " + tasks.size() + " tasks in the list.";
                }

                case ADD_DEADLINE: {
                    try {
                        Task t = tasks.add(new Deadline(pc.desc, pc.when1));
                        storage.save(tasks.asUnmodifiableList());
                        return "Got it. I've added this task:\n  " + t
                                + "\nNow you have " + tasks.size() + " tasks in the list.";
                    } catch (DateTimeParseException e) {
                        return "☹ OOPS! Date must be like 2019-12-02 or 2/12/2019 (or a weekday name).";
                    }
                }

                case ADD_EVENT: {
                    try {
                        Task t = tasks.add(new Event(pc.desc, pc.when1, pc.when2));
                        storage.save(tasks.asUnmodifiableList());
                        return "Got it. I've added this task:\n  " + t
                                + "\nNow you have " + tasks.size() + " tasks in the list.";
                    } catch (DateTimeParseException e) {
                        return "☹ OOPS! Times must be like 2019-12-02 1400 (or 2019-12-02 14:00).";
                    }
                }

                default:
                    return "Unknown command.";
            }

        } catch (PaneerException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Paneer slipped: " + e.getMessage();
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