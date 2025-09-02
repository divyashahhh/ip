package cs2103;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Saves/loads tasks to a single file at project root
 * Line format:
 *   T | 1 | description
 *   D | 0 | description | by
 *   E | 1 | description | from | to
 */
public class Storage {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;           // 2019-12-02
    private static final DateTimeFormatter DT_FMT   = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"); // 2019-12-02 1200

    private final Path file;

    public Storage(Path file) {
        this.file = file;
    }

    /** Load tasks; if file missing or corrupted, return what is there or empty */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(file)) {
                return tasks; // first run, nothing to load
            }
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String raw : lines) {
                if (raw == null) continue;
                String line = raw.trim();
                if (line.isEmpty()) continue;

                // split
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
                if (parts.length < 3) continue;

                String type = parts[0];                // T / D / E
                boolean done = "1".equals(parts[1]);
                String desc = parts[2];

                Task t = null;
                if ("T".equalsIgnoreCase(type)) {
                    t = new ToDos(desc);
                } else if ("D".equalsIgnoreCase(type) && parts.length >= 4) {
                    t = new Deadline(desc, parts[3]);
                } else if ("E".equalsIgnoreCase(type) && parts.length >= 5) {
                    t = new Event(desc, parts[3], parts[4]);
                }

                if (t != null) {
                    if (done) t.markDone();
                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("[Storage] Load issue: " + e.getMessage());
        }
        return tasks;
    }

    /** Save all tasks, overwriting the file. */
    public void save(List<Task> tasks) {
        try {
            Path parent = file.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter bw = Files.newBufferedWriter(
                    file, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {
                for (Task t : tasks) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("[Storage] Save issue: " + e.getMessage());
        }
    }


    private String serialize(Task t) {
        String done = t.isDone ? "1" : "0";
        String desc = t.getDescription();

        if (t instanceof ToDos) {
            return "T | " + done + " | " + desc;
        } else if (t instanceof Deadline d) {
            return "D | " + done + " | " + desc + " | " + d.getBy().format(DATE_FMT);
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return "E | " + done + " | " + desc + " | "
                    + e.getFrom().format(DT_FMT) + " | " + e.getTo().format(DT_FMT);
        }
        return "T | " + done + " | " + desc;
    }
}