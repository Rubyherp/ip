package ruby;

/**
 * Represents one validated instruction the user has given Ruby.
 */
public abstract class Command {
    /**
     * Performs this command using the application's collaborators.
     *
     * @param tasks Tasks managed by Ruby.
     * @param ui Interface used to display responses.
     * @param storage Persists tasks when a command changes them.
     * @throws RubyException If the command cannot be completed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException;

    /**
     * Returns whether this command terminates Ruby.
     *
     * @return False unless a subclass represents an exit command.
     */
    public boolean isExit() {
        return false;
    }
}
