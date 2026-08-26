package ruby;

/** Displays every task in Ruby's task list. */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.printMessage(tasks.listItems());
    }
}
