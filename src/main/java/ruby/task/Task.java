package ruby.task;

/**
 * Represents a task and whether the user has completed it.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon representing whether this task is complete.
     *
     * @return {@code X} if complete, or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as complete.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns this task as one line of the saved data file.
     *
     * @return The done flag and description, separated by {@code " | "}.
     */
    public String toDataString() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
