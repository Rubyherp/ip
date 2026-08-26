package ruby.command;

import ruby.RubyException;
import ruby.storage.Storage;
import ruby.task.Deadline;
import ruby.task.TaskList;
import ruby.ui.Ui;

/** Adds a deadline task to Ruby's task list. */
public class DeadlineCommand extends Command {
    private final Deadline deadline;

    /** Creates a command that adds the parsed deadline. */
    public DeadlineCommand(Deadline deadline) {
        this.deadline = deadline;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException {
        ui.printMessage(tasks.addItem(deadline));
        storage.save(tasks);
    }
}
