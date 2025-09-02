package cs2103;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @TempDir
    Path tmp;

    @Test
    void load_parsesValidLines_ignoresBlankOrMalformed() throws IOException {
        Path data = tmp.resolve("paneer.txt");
        List<String> lines = List.of(
                "   ",
                "T | 1 | read book",
                "D | 0 | submit report | 2019-12-02",
                "E | 1 | camp | 2019-12-02 1200 | 2019-12-02 1800",
                "X | 1 | ???",
                "D | 1 | malformed",
                "E | 0 | bad | onlyFrom"
        );
        Files.write(data, lines, StandardCharsets.UTF_8);

        Storage storage = new Storage(data);
        List<Task> tasks = storage.load();

        assertEquals(3, tasks.size());

        Task t0 = tasks.get(0);
        assertInstanceOf(ToDos.class, t0);
        assertEquals("read book", t0.getDescription());
        assertTrue(t0.isDone);

        Task t1 = tasks.get(1);
        assertInstanceOf(Deadline.class, t1);
        assertEquals("submit report", t1.getDescription());
        assertFalse(t1.isDone);
        assertEquals(LocalDate.of(2019, 12, 2), ((Deadline) t1).getBy());

        Task t2 = tasks.get(2);
        assertInstanceOf(Event.class, t2);
        assertEquals("camp", t2.getDescription());
        assertTrue(t2.isDone);
        assertEquals(LocalDateTime.of(2019, 12, 2, 12, 0), ((Event) t2).getFrom());
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), ((Event) t2).getTo());
    }

    @Test
    void save_writesCanonicalFormat_thatLoadsBackTheSame() throws IOException {
        Path data = tmp.resolve("save.txt");
        Storage storage = new Storage(data);

        List<Task> list = new ArrayList<>();
        ToDos t = new ToDos("read book");
        t.markDone();
        list.add(t);

        Deadline d = new Deadline("submit report", "2019-12-02");
        list.add(d);

        Event e = new Event("camp", "2019-12-02 1200", "2019-12-02 1800");
        e.markDone();
        list.add(e);

        storage.save(list);
        List<String> written = Files.readAllLines(data, StandardCharsets.UTF_8);

        assertEquals(3, written.size());
        assertEquals("T | 1 | read book", written.get(0));
        assertEquals("D | 0 | submit report | 2019-12-02", written.get(1));
        assertEquals("E | 1 | camp | 2019-12-02 1200 | 2019-12-02 1800", written.get(2));

        List<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertInstanceOf(ToDos.class, loaded.get(0));
        assertInstanceOf(Deadline.class, loaded.get(1));
        assertInstanceOf(Event.class, loaded.get(2));
        assertEquals(LocalDate.of(2019, 12, 2), ((Deadline) loaded.get(1)).getBy());
        assertEquals(LocalDateTime.of(2019, 12, 2, 12, 0), ((Event) loaded.get(2)).getFrom());
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), ((Event) loaded.get(2)).getTo());
    }
}