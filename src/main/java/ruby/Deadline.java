package ruby;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private final String deadline;

    /**
     * Creates a deadline with the specified description and due date or time.
     *
     * @param description Description of the deadline.
     * @param deadline Due date or time, stored as entered by the user.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + deadline + ")";
    }

    @Override
    public String toDataString() {
        return "D | " + super.toDataString() + " | " + deadline;
    }
}
