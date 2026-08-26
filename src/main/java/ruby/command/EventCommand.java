package ruby.command;

import ruby.RubyException;
import ruby.storage.Storage;
import ruby.task.Event;
import ruby.task.TaskList;
import ruby.ui.Ui;

/** Adds an event task to Ruby's task list. */
public class EventCommand extends Command {
    private final Event event;

    /** Creates a command that adds the parsed event. */
    public EventCommand(Event event) {
        this.event = event;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws RubyException {
        ui.printMessage(tasks.addItem(event));
        storage.save(tasks);
    }
}
