package cs2103;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse_mark_usesOneBasedIndex_convertsToZeroBased() throws PaneerException {
        Parser.ParsedCommand pc = Parser.parse("mark 1");
        assertEquals(Parser.ParsedCommand.Type.MARK, pc.type);
        assertEquals(0, pc.index); // 1 → 0
    }

    @Test
    void parse_deadline_valid_returnsTypeAndFields() throws PaneerException {
        Parser.ParsedCommand pc = Parser.parse("deadline submit report /by 2019-12-02");
        assertEquals(Parser.ParsedCommand.Type.ADD_DEADLINE, pc.type);
        assertEquals("submit report", pc.desc);
        assertEquals("2019-12-02", pc.when1);
    }

    @Test
    void parse_event_missingTo_throwsFriendlyMessage() {
        PaneerException ex = assertThrows(PaneerException.class,
                () -> Parser.parse("event cs2103 final /from 2019-12-02 1400"));
        assertTrue(ex.getMessage().toLowerCase().contains("both start and end"),
                "Message should guide user about both /from and /to");
    }

    @Test
    void parse_empty_throwsSnarkyPaneerMessage() {
        PaneerException ex = assertThrows(PaneerException.class, () -> Parser.parse(""));
        assertTrue(ex.getMessage().contains("Tofu") || !ex.getMessage().isEmpty());
    }

    @Test
    void parse_unknown_throwsHelpfulListOfCommands() {
        PaneerException ex = assertThrows(PaneerException.class, () -> Parser.parse("abracadabra"));
        assertTrue(ex.getMessage().toLowerCase().contains("list"));
        assertTrue(ex.getMessage().toLowerCase().contains("todo"));
        assertTrue(ex.getMessage().toLowerCase().contains("event"));
    }
}
