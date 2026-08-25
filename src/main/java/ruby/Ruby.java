package ruby;

import java.util.Scanner;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    private Ruby() {
    }

    /**
     * Starts Ruby and processes commands until the input ends or the user exits.
     *
     * @param args Command-line arguments; not used by Ruby.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();

        TaskList taskList;
        try {
            taskList = storage.load();
        } catch (RubyException exception) {
            ui.printMessage("Sorry, I couldn't load your saved tasks: "
                    + exception.getMessage());
            taskList = new TaskList();
        }

        ui.printWelcome();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            try {
                if (!processCommand(userInput, taskList, ui, storage)) {
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
     * @param userInput Raw command entered by the user.
     * @param taskList Tasks stored during this session.
     * @param ui Interface used to display responses.
     * @param storage Persists the task list to disk after every change.
     * @return False only when the chatbot should exit.
     * @throws RubyException If the command is unknown or malformed.
     */
    private static boolean processCommand(String userInput, TaskList taskList, Ui ui,
            Storage storage) throws RubyException {
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
            int index = Parser.parseTaskIndex(command, "mark");
            ui.printMessage(taskList.markItem(index));
            storage.save(taskList);
            return true;
        }

        if (isCommand(command, "unmark")) {
            int index = Parser.parseTaskIndex(command, "unmark");
            ui.printMessage(taskList.unmarkItem(index));
            storage.save(taskList);
            return true;
        }

        if (isCommand(command, "delete")) {
            int index = Parser.parseTaskIndex(command, "delete");
            ui.printMessage(taskList.deleteItem(index));
            storage.save(taskList);
            return true;
        }

        if (isCommand(command, "todo")) {
            String description = command.substring("todo".length()).strip();
            if (description.isEmpty()) {
                throw new RubyException("A todo needs a description.");
            }
            ui.printMessage(taskList.addItem(new Todo(description)));
            storage.save(taskList);
            return true;
        }

        if (isCommand(command, "deadline")) {
            ui.printMessage(taskList.addItem(Parser.parseDeadline(command)));
            storage.save(taskList);
            return true;
        }

        if (isCommand(command, "event")) {
            ui.printMessage(taskList.addItem(Parser.parseEvent(command)));
            storage.save(taskList);
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
}
