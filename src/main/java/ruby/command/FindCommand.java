package ruby.command;

import ruby.storage.Storage;
import ruby.task.TaskList;

/**
 * Finds tasks whose stored text contains the search keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a command that searches for tasks containing the given keyword.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) {
        String response = tasks.find(keyword);
        return response;
    }
}
