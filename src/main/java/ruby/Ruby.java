package ruby;

import ruby.command.Command;
import ruby.command.Parser;
import ruby.storage.Storage;
import ruby.task.TaskList;
import ruby.ui.Ui;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

    /**
     * Creates Ruby and restores any tasks saved at the given file path.
     *
     * @param filePath Location of Ruby's saved task data.
     */
    public Ruby(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTaskList;
        try {
            loadedTaskList = storage.load();
        } catch (RubyException exception) {
            loadedTaskList = new TaskList();
        }
        taskList = loadedTaskList;
    }

    /**
     * Answers one command with the text Ruby would display in response.
     *
     * @param input The user's raw command text.
     * @return Ruby's response to the command.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parse(input);
            return command.execute(taskList, storage);
        } catch (RubyException exception) {
            return "Sorry, I couldn't process that: " + exception.getMessage();
        }
    }

    /**
     * Runs Ruby until the user exits or the input stream ends.
     */
    public void run() {
        ui.printWelcome();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            try {
                Command command = Parser.parse(ui.readCommand());
                String response = command.execute(taskList, storage);
                ui.printMessage(response);
                isExit = command.isExit();
            } catch (RubyException exception) {
                ui.printMessage("Sorry, I couldn't process that: " + exception.getMessage());
            }
        }
    }

    /**
     * Starts Ruby and processes commands until the input ends or the user exits.
     *
     * @param args Command-line arguments; not used by Ruby.
     */
    public static void main(String[] args) {
        new Ruby("data/ruby.txt").run();
    }
}
