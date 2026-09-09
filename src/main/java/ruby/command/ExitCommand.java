package ruby.command;

import ruby.storage.Storage;
import ruby.task.TaskList;

/**
 * Ends the Ruby application after displaying a farewell message.
 */
public class ExitCommand extends Command {
    @Override
    public String execute(TaskList tasks, Storage storage) {
        return "Bye. Hope to see you again soon!";
    }
}
