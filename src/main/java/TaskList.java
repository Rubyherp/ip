import java.util.ArrayList;
import java.util.List;

/**
 * Stores the tasks added during the current chatbot session.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public String addItem(Task task) {
        tasks.add(task);
        return "Got it. I've added this task:\n  "
                + task
                + "\nNow you have "
                + tasks.size()
                + " tasks in the list.";
    }

    /**
     * Marks the task at the specified zero-based index as done.
     *
     * @param index zero-based index of the task
     * @return confirmation describing the marked task
     * @throws RubyException if the index does not identify an existing task
     */
    public String markItem(int index) throws RubyException {
        Task task = getTask(index, "mark");
        task.markAsDone();

        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Marks the task at the specified zero-based index as not done.
     *
     * @param index zero-based index of the task
     * @return confirmation describing the unmarked task
     * @throws RubyException if the index does not identify an existing task
     */
    public String unmarkItem(int index) throws RubyException {
        Task task = getTask(index, "unmark");
        task.markAsNotDone();

        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Removes and returns confirmation for the task at a zero-based index.
     *
     * @param index zero-based index of the task
     * @return confirmation describing the removed task and remaining count
     * @throws RubyException if the index does not identify an existing task
     */
    public String deleteItem(int index) throws RubyException {
        Task task = getTask(index, "delete");
        tasks.remove(index);

        return "Noted. I've removed this task:\n  "
                + task
                + "\nNow you have "
                + tasks.size()
                + " tasks in the list.";
    }

    /**
     * Formats all stored tasks as a one-based numbered list.
     *
     * @return numbered tasks, or an empty string when there are no tasks
     */
    public String listItems() {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            response.append('\n')
                    .append(i + 1)
                    .append(".")
                    .append(tasks.get(i));
        }

        return response.toString();
    }

    /**
     * Retrieves a task after checking that the requested index exists.
     */
    private Task getTask(int index, String action) throws RubyException {
        if (tasks.isEmpty()) {
            throw new RubyException("There are no tasks to " + action + ".");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new RubyException(
                    "Task " + (index + 1)
                            + " does not exist; choose a number from 1 to "
                            + tasks.size()
                            + "."
            );
        }
        return tasks.get(index);
    }
}
