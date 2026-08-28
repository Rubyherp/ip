package ruby.command;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ruby.RubyException;
import ruby.task.Deadline;
import ruby.task.Event;

class ParserTest {

    @Test
    void parseDateTime_validDateTime_returnsDateTime() throws RubyException {
        assertEquals(LocalDateTime.of(2026, 8, 28, 18, 0), Parser.parseDateTime("2026-08-28 1800"));
    }

    @Test
    void parseDateTime_validDateOnly_returnsMidnight() throws RubyException {
        assertEquals(LocalDateTime.of(2026, 8, 28, 0, 0), Parser.parseDateTime("2026-08-28"));
    }

    @Test
    void parseDateTime_invalidText_throws() {
        assertThrows(RubyException.class, () -> Parser.parseDateTime("not a date"));
    }

    @Test
    void formatDateTime_midnight_returnsDateOnly() {
        assertEquals("Oct 15 2019", Parser.formatDateTime(LocalDateTime.of(2019, 10, 15, 0, 0)));
    }

    @Test
    void formatDateTime_withTime_returnsDateAndTime() {
        assertEquals("Oct 15 2019, 18:00",
                Parser.formatDateTime(LocalDateTime.of(2019, 10, 15, 18, 0)));
    }

    @Test
    void parseDeadline_validInput_parsesDescriptionAndDeadline() throws RubyException {
        Deadline deadline = Parser.parseDeadline("deadline return book /by 2019-10-15 1800");
        assertEquals("D | 0 | return book | 2019-10-15T18:00", deadline.toDataString());
    }

    @Test
    void parseDeadline_missingBy_throws() {
        assertThrows(RubyException.class, () -> Parser.parseDeadline("deadline return book"));
    }

    @Test
    void parseDeadline_missingDescription_throws() {
        assertThrows(RubyException.class, () -> Parser.parseDeadline("deadline /by 2019-10-15"));
    }

    @Test
    void parseDeadline_missingDeadline_throws() {
        assertThrows(RubyException.class, () -> Parser.parseDeadline("deadline return book /by"));
    }

    @Test
    void parseEvent_validInput_parsesDescriptionStartEnd() throws RubyException {
        Event event = Parser.parseEvent("event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        assertEquals("E | 0 | project meeting | 2019-10-15T14:00 | 2019-10-15T16:00",
                event.toDataString());
    }

    @Test
    void parseEvent_missingTo_throws() {
        assertThrows(RubyException.class,
                () -> Parser.parseEvent("event project meeting /from 2019-10-15 1400"));
    }

    @Test
    void parseTaskIndex_validNumber_returnsZeroBased() throws RubyException {
        assertEquals(2, Parser.parseTaskIndex("mark 3", "mark"));
    }

    @Test
    void parseTaskIndex_missingNumber_throws() {
        assertThrows(RubyException.class, () -> Parser.parseTaskIndex("mark", "mark"));
    }

    @Test
    void parseTaskIndex_nonNumber_throws() {
        assertThrows(RubyException.class, () -> Parser.parseTaskIndex("mark abc", "mark"));
    }

    @Test
    void parseTaskIndex_zeroOrNegative_throws() {
        assertThrows(RubyException.class, () -> Parser.parseTaskIndex("mark 0", "mark"));
        assertThrows(RubyException.class, () -> Parser.parseTaskIndex("mark -1", "mark"));
    }

    @Test
    void parse_emptyInput_throws() {
        assertThrows(RubyException.class, () -> Parser.parse(""));
        assertThrows(RubyException.class, () -> Parser.parse("   "));
    }

    @Test
    void parse_unknownCommand_throws() {
        assertThrows(RubyException.class, () -> Parser.parse("dance"));
    }

    @Test
    void parse_todoCommand_returnsTodoCommand() throws RubyException {
        assertInstanceOf(TodoCommand.class, Parser.parse("todo read book"));
    }
}
