package ruby.command;

import ruby.storage.Storage;
import ruby.task.TaskList;
import ruby.ui.Ui;

/** Ends the Ruby application after displaying a farewell message. */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printFarewell();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
