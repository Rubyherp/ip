package ruby.command;

import ruby.RubyException;
import ruby.storage.Storage;
import ruby.task.TaskList;

/**
 * Represents one validated instruction the user has given Ruby.
 */
public abstract class Command {
    /**
     * Performs this command using the application's collaborators.
     *
     * @param tasks   Tasks managed by Ruby.
     * @param storage Persists tasks when a command changes them.
     * @throws RubyException If the command cannot be completed.
     */
    public abstract String execute(TaskList tasks, Storage storage) throws RubyException;
}
