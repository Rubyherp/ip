/**
 * Stores the tasks added during the current chatbot session.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;

    private final Task[] tasks;
    private int taskCount;

    public TaskList() {
        this.tasks = new Task[MAX_TASKS];
        this.taskCount = 0;
    }

    public String addItem(Task task) {
        tasks[taskCount] = task;
        taskCount++;
        return "Got it. I've added this task:\n  "
                + task
                + "\nNow you have "
                + taskCount
                + " tasks in the list.";
    }

    /**
     * Marks the task at the specified zero-based index as done.
     *
     * @param index zero-based index of the task
     * @return confirmation describing the marked task
     */
    public String markItem(int index) {
        Task task = tasks[index];
        task.markAsDone();

        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Marks the task at the specified zero-based index as not done.
     *
     * @param index zero-based index of the task
     * @return confirmation describing the unmarked task
     */
    public String unmarkItem(int index) {
        Task task = tasks[index];
        task.markAsNotDone();

        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Formats all stored tasks as a one-based numbered list.
     *
     * @return numbered tasks, or an empty string when there are no tasks
     */
    public String listItems() {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            response.append('\n')
                    .append(i + 1)
                    .append(".")
                    .append(tasks[i]);
        }

        return response.toString();
    }
}
