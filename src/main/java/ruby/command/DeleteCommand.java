package ruby.command;

import ruby.RubyException;
import ruby.storage.Storage;
import ruby.task.TaskList;
import ruby.ui.Ui;

/** Removes one task from Ruby's task list. */
public class DeleteCommand extends Command {
    private final int index;

    /** Creates a command for the given zero-based task index. */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException {
        ui.printMessage(tasks.deleteItem(index));
        storage.save(tasks);
    }
}
