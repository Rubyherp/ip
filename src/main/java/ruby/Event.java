package ruby;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class Event extends Task {
    private final String startDate;
    private final String endDate;

    /**
     * Creates an event with the specified description, start, and end.
     *
     * @param description Description of the event.
     * @param startDate Start date or time, stored as entered by the user.
     * @param endDate End date or time, stored as entered by the user.
     */
    public Event(String description, String startDate, String endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + startDate + " to: " + endDate + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + startDate + " | " + endDate;
    }
}
