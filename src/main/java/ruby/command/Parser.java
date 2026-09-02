package ruby.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import ruby.RubyException;
import ruby.task.Deadline;
import ruby.task.Event;

/**
 * Parses raw user input into executable commands.
 */
public class Parser {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, HH:mm", Locale.ENGLISH);

    private Parser() {
    }

    /**
     * Parses one complete user command.
     *
     * @param userInput Raw text entered by the user.
     * @return The command represented by the input.
     * @throws RubyException If the command is unknown or malformed.
     */
    public static Command parse(String userInput) throws RubyException {
        String command = userInput.strip();
        if (command.isEmpty()) {
            throw new RubyException("Please enter a command.");
        }

        if ("bye".equals(command)) {
            return new ExitCommand();
        }
        if ("list".equals(command)) {
            return new ListCommand();
        }
        if (isCommand(command, "mark")) {
            return new MarkCommand(parseTaskIndex(command, "mark"));
        }
        if (isCommand(command, "unmark")) {
            return new UnmarkCommand(parseTaskIndex(command, "unmark"));
        }
        if (isCommand(command, "delete")) {
            return new DeleteCommand(parseTaskIndex(command, "delete"));
        }
        if (isCommand(command, "todo")) {
            return new TodoCommand(parseTodo(command));
        }
        if (isCommand(command, "deadline")) {
            return new DeadlineCommand(parseDeadline(command));
        }
        if (isCommand(command, "event")) {
            return new EventCommand(parseEvent(command));
        }
        if (isCommand(command, "find")) {
            return new FindCommand(parseFindKeyword(command));
        }
        throw new RubyException("I don't recognise that command.");
    }

    /**
     * Parses a todo command into its description.
     *
     * @param input Raw todo command.
     * @return The todo description.
     * @throws RubyException If the description is missing.
     */
    private static String parseTodo(String input) throws RubyException {
        String description = input.substring("todo".length()).strip();
        if (description.isEmpty()) {
            throw new RubyException("A todo needs a description.");
        }
        return description;
    }

    /**
     * Parses a one-based task number and converts it to a zero-based index.
     *
     * @param input       Raw command entered by the user.
     * @param commandWord Command word that precedes the task number.
     * @return Zero-based index of the task.
     * @throws RubyException If the task number is missing, invalid, or not
     *                       positive.
     */
    public static int parseTaskIndex(String input, String commandWord) throws RubyException {
        String taskNumber = input.substring(commandWord.length()).strip();
        if (taskNumber.isEmpty()) {
            throw new RubyException("Give me a task number after " + commandWord + ".");
        }

        try {
            int oneBasedIndex = Integer.parseInt(taskNumber);
            if (oneBasedIndex <= 0) {
                throw new RubyException("Task numbers must be positive whole numbers.");
            }
            return oneBasedIndex - 1;
        } catch (NumberFormatException exception) {
            throw new RubyException(
                    "The task number for " + commandWord + " must be a whole number.");
        }
    }

    /**
     * Parses a deadline command into its description and deadline text.
     *
     * @param input Raw deadline command.
     * @return The parsed deadline.
     * @throws RubyException If the command is missing its description or deadline.
     */
    public static Deadline parseDeadline(String input) throws RubyException {
        String details = input.substring("deadline".length()).strip();
        int byIndex = findDelimiter(details, "/by");
        if (byIndex < 0) {
            throw new RubyException("Use: deadline DESCRIPTION /by DATE_OR_TIME.");
        }

        String description = details.substring(0, byIndex).strip();
        String deadline = details.substring(byIndex + "/by".length()).strip();
        if (description.isEmpty()) {
            throw new RubyException("A deadline needs a description.");
        }
        if (deadline.isEmpty()) {
            throw new RubyException("A deadline needs a date or time after /by.");
        }
        return new Deadline(description, parseDateTime(deadline));
    }

    /**
     * Parses an event command into its description, start, and end text.
     *
     * @param input Raw event command.
     * @return The parsed event.
     * @throws RubyException If the command is missing its description, start, or
     *                       end.
     */
    public static Event parseEvent(String input) throws RubyException {
        String details = input.substring("event".length()).strip();
        int fromIndex = findDelimiter(details, "/from");
        if (fromIndex < 0) {
            throw new RubyException("Use: event DESCRIPTION /from START /to END.");
        }

        String description = details.substring(0, fromIndex).strip();
        String dates = details.substring(fromIndex + "/from".length()).strip();
        int toIndex = findDelimiter(dates, "/to");
        if (description.isEmpty()) {
            throw new RubyException("An event needs a description.");
        }
        if (toIndex < 0) {
            throw new RubyException("An event needs an end after /to.");
        }

        String startDate = dates.substring(0, toIndex).strip();
        String endDate = dates.substring(toIndex + "/to".length()).strip();
        if (startDate.isEmpty()) {
            throw new RubyException("An event needs a start after /from.");
        }
        if (endDate.isEmpty()) {
            throw new RubyException("An event needs an end after /to.");
        }
        return new Event(description, parseDateTime(startDate), parseDateTime(endDate));
    }

    /**
     * Parses a find command into its search keyword.
     *
     * @param input Raw find command.
     * @return The keyword to search for.
     * @throws RubyException If the keyword is missing.
     */
    private static String parseFindKeyword(String input) throws RubyException {
        String keyword = input.substring("find".length()).strip();
        if (keyword.isEmpty()) {
            throw new RubyException("Give me a keyword to search for after find.");
        }
        return keyword;
    }

    /**
     * Finds a delimiter only when it appears as a complete whitespace-separated
     * token.
     *
     * @param text      Text to search in.
     * @param delimiter Delimiter to search for.
     * @return Index of the delimiter, or -1 when it is not found as a complete
     *         token.
     */
    private static int findDelimiter(String text, String delimiter) {
        int searchFrom = 0;
        while (searchFrom < text.length()) {
            int index = text.indexOf(delimiter, searchFrom);
            if (index < 0) {
                return -1;
            }

            int afterDelimiter = index + delimiter.length();
            boolean hasLeftBoundary = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            boolean hasRightBoundary = afterDelimiter == text.length()
                    || Character.isWhitespace(text.charAt(afterDelimiter));
            if (hasLeftBoundary && hasRightBoundary) {
                return index;
            }
            searchFrom = index + delimiter.length();
        }
        return -1;
    }

    /**
     * Returns whether the input is a command word, optionally followed by
     * arguments.
     */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord)
                || (input.startsWith(commandWord)
                        && Character.isWhitespace(input.charAt(commandWord.length())));
    }

    /**
     * Parses a user-supplied date or date and time into a LocalDateTime.
     *
     * @param input Date text, e.g. "2019-10-15" or "2019-10-15 1800".
     * @return The parsed date and time; a date without a time becomes midnight.
     * @throws RubyException If the text is not in a recognised format.
     */
    public static LocalDateTime parseDateTime(String input) throws RubyException {
        String text = input.strip();
        if (text.isEmpty()) {
            throw new RubyException("A deadline needs a date or time after /by.");
        }

        try {
            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException exception) {
            // Not a date with a time; try a date alone below.
        }

        try {
            return LocalDate.parse(text).atStartOfDay();
        } catch (DateTimeParseException exception) {
            throw new RubyException(
                    "I don't understand that date. Use yyyy-mm-dd (e.g. 2019-10-15)"
                            + " or yyyy-mm-dd HHmm (e.g. 2019-10-15 1800).");
        }
    }

    /**
     * Formats a date for display as "Oct 15 2019", appending the time only
     * when it was given (a midnight time means the user supplied no time).
     *
     * @param dateTime Date and time to format.
     * @return The formatted date, with a time when one is present.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return dateTime.format(DATE_FORMAT);
        }
        return dateTime.format(DATE_TIME_FORMAT);
    }
}
