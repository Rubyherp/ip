package ruby.command;

import ruby.storage.Storage;
import ruby.task.TaskList;

/** Displays every task in Ruby's task list. */
public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Storage storage) {
        String response = tasks.listItems();
        return response;
    }
}
