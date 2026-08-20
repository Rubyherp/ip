import java.util.Scanner;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList taskList = new TaskList();

        ui.printWelcome();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            try {
                if (!processCommand(userInput, taskList, ui)) {
                    break;
                }
            } catch (RubyException exception) {
                ui.printMessage("Sorry, I couldn't process that: " + exception.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Processes one user command while keeping the main input loop running.
     *
     * @param userInput raw command entered by the user
     * @param taskList tasks stored during this session
     * @param ui interface used to display responses
     * @return false only when the chatbot should exit
     * @throws RubyException if the command is unknown or malformed
     */
    private static boolean processCommand(String userInput, TaskList taskList, Ui ui)
            throws RubyException {
        String command = userInput.strip();
        if (command.isEmpty()) {
            throw new RubyException("Please enter a command.");
        }

        if ("bye".equals(command)) {
            ui.printFarewell();
            return false;
        }

        if ("list".equals(command)) {
            ui.printMessage(taskList.listItems());
            return true;
        }

        if (isCommand(command, "mark")) {
            int index = parseTaskIndex(command, "mark");
            ui.printMessage(taskList.markItem(index));
            return true;
        }

        if (isCommand(command, "unmark")) {
            int index = parseTaskIndex(command, "unmark");
            ui.printMessage(taskList.unmarkItem(index));
            return true;
        }

        if (isCommand(command, "delete")) {
            int index = parseTaskIndex(command, "delete");
            ui.printMessage(taskList.deleteItem(index));
            return true;
        }

        if (isCommand(command, "todo")) {
            String description = command.substring("todo".length()).strip();
            if (description.isEmpty()) {
                throw new RubyException("A todo needs a description.");
            }
            ui.printMessage(taskList.addItem(new Todo(description)));
            return true;
        }

        if (isCommand(command, "deadline")) {
            ui.printMessage(taskList.addItem(parseDeadline(command)));
            return true;
        }

        if (isCommand(command, "event")) {
            ui.printMessage(taskList.addItem(parseEvent(command)));
            return true;
        }

        throw new RubyException("I don't recognise that command.");
    }

    /**
     * Returns whether the input is a command word, optionally followed by arguments.
     */
    private static boolean isCommand(String input, String commandWord) {
        return input.equals(commandWord)
                || (input.startsWith(commandWord)
                && Character.isWhitespace(input.charAt(commandWord.length())));
    }

    /**
     * Parses a one-based task number and converts it to a zero-based index.
     */
    private static int parseTaskIndex(String input, String commandWord) throws RubyException {
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
                    "The task number for " + commandWord + " must be a whole number."
            );
        }
    }

    /**
     * Parses a deadline command into its description and deadline text.
     */
    private static Deadline parseDeadline(String input) throws RubyException {
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
        return new Deadline(description, deadline);
    }

    /**
     * Parses an event command into its description, start, and end text.
     */
    private static Event parseEvent(String input) throws RubyException {
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
        return new Event(description, startDate, endDate);
    }

    /**
     * Finds a delimiter only when it appears as a complete whitespace-separated token.
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
}
