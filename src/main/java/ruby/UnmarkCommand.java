package ruby;

/** Marks one task as incomplete. */
public class UnmarkCommand extends Command {
    private final int index;

    /** Creates a command for the given zero-based task index. */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException {
        ui.printMessage(tasks.unmarkItem(index));
        storage.save(tasks);
    }
}
