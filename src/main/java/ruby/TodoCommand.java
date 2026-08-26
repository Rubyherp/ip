package ruby;

/** Adds a todo task to Ruby's task list. */
public class TodoCommand extends Command {
    private final String description;

    /** Creates a command that adds a todo with the given description. */
    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException {
        ui.printMessage(tasks.addItem(new Todo(description)));
        storage.save(tasks);
    }
}
