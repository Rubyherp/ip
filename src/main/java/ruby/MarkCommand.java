package ruby;

/** Marks one task as complete. */
public class MarkCommand extends Command {
    private final int index;

    /** Creates a command for the given zero-based task index. */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException {
        ui.printMessage(tasks.markItem(index));
        storage.save(tasks);
    }
}
