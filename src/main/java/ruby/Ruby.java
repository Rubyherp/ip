package ruby;

import ruby.command.Command;
import ruby.command.Parser;
import ruby.storage.Storage;
import ruby.task.TaskList;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    private final Storage storage;
    private final TaskList taskList;

    /**
     * Creates Ruby and restores any tasks saved at the given file path.
     *
     * @param filePath Location of Ruby's saved task data.
     */
    public Ruby(String filePath) {
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

}
