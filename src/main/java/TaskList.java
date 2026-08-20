/**
 * Stores the tasks added during the current chatbot session.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;

    private final String[] tasks;
    private int taskCount;

    public TaskList() {
        this.tasks = new String[MAX_TASKS];
        this.taskCount = 0;
    }

    public String addItem(String item) {
        tasks[taskCount] = item;
        taskCount++;
        return "added: " + item;
    }

    /**
     * Formats all stored tasks as a one-based numbered list.
     *
     * @return numbered tasks, or an empty string when there are no tasks
     */
    public String listItems() {
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < taskCount; i++) {
            if (i > 0) {
                response.append('\n');
            }
            response.append(i + 1)
                    .append(". ")
                    .append(tasks[i]);
        }

        return response.toString();
    }
}
