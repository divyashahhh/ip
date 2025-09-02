package cs2103;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTypesTest {

    @Test
    void deadline_constructsAndExposesBy() {
        Deadline d = new Deadline("submit report", "2019-12-02");
        assertEquals("submit report", d.getDescription());

        assertEquals(LocalDate.of(2019, 12, 2), d.getBy());
        assertFalse(d.isDone);
        d.markDone();
        assertTrue(d.isDone);
    }

    @Test
    void event_constructsAndExposesFromTo() {
        Event e = new Event("camp", "2019-12-02 1200", "2019-12-02 1800");
        assertEquals("camp", e.getDescription());
        assertEquals(LocalDateTime.of(2019, 12, 2, 12, 0), e.getFrom());
        assertEquals(LocalDateTime.of(2019, 12, 2, 18, 0), e.getTo());
        assertFalse(e.isDone);
        e.markDone();
        assertTrue(e.isDone);
    }

    @Test
    void todos_constructsAndMarksDone() {
        ToDos t = new ToDos("read book");
        assertEquals("read book", t.getDescription());
        assertFalse(t.isDone);
        t.markDone();
        assertTrue(t.isDone);
    }
}