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
        this.ui = new UI();
        this.storage = new Storage(Paths.get(filePath));
        TaskList loaded;
        loaded = new TaskList(storage.load());
        this.tasks = loaded;
    }
// Runs the chatbot to use parser, storage and events classes.
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
                            Task t = tasks.add(new Deadline(pc.desc, pc.when1)); // flexible date parsing inside
                            storage.save(tasks.asUnmodifiableList());
                            ui.showAdd(t, tasks.size());
                        } catch (DateTimeParseException e) {
                            ui.showError("☹ OOPS! Date must be like 2019-12-02 or 2/12/2019 (or a weekday name).");
                        }
                        break;
                    }
                    case ADD_EVENT: {
                        try {
                            Task t = tasks.add(new Event(pc.desc, pc.when1, pc.when2)); // flexible dt parsing inside
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
        new Paneer(savePath).run();
    }
}