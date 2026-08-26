package ruby.task;

import java.time.LocalDateTime;
import ruby.command.Parser;

/**
 * Represents a task that occurs between specified start and end times.
 */
public class Event extends Task {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    /**
     * Creates an event with the specified description, start, and end.
     *
     * @param description Description of the event.
     * @param startDate   Start date and time.
     * @param endDate     End date and time.
     */
    public Event(String description, LocalDateTime startDate, LocalDateTime endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + Parser.formatDateTime(startDate) + " to: "
                + Parser.formatDateTime(endDate) + ")";
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + startDate + " | " + endDate;
    }
}
