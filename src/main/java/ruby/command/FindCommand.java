package ruby.command;

import ruby.storage.Storage;
import ruby.task.TaskList;
import ruby.ui.Ui;

/** Finds tasks whose stored text contains the search keyword. */
public class FindCommand extends Command {
    private final String keyword;

    /** Creates a command that searches for tasks containing the given keyword. */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printMessage(tasks.find(keyword));
    }
}
