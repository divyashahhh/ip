package cs2103;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A task that has a start and end date-time
 * Accepts "uuuu-MM-dd HHmm" by default, plus a few common alternates
 * Always prints date-times as "MMM d yyyy, h:mma" (e.g., "Dec 2 2019, 2:00PM")
 */
public class Event extends Task {

    private static final DateTimeFormatter OUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    // accepted methods
    private static final DateTimeFormatter[] IN_FORMAT = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm"),
            DateTimeFormatter.ofPattern("d/M/uuuu HH:mm"),
            DateTimeFormatter.ofPattern("d-M-uuuu HHmm"),
            DateTimeFormatter.ofPattern("d-M-uuuu HH:mm")
    };

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructs an Event with a description and raw date-time strings.
     *
     * @param description the event description
     * @param startRaw    start date-time
     * @param endRaw      end date-time
     * @throws DateTimeParseException if a date-time cannot be processed
     */
    public Event(String description, String startRaw, String endRaw) {
        super(description);
        this.startTime = parseFlexibleDateTime(startRaw);
        this.endTime = parseFlexibleDateTime(endRaw);
        if (endTime.isBefore(startTime)) {
            throw new DateTimeParseException("End time is before start time", endRaw, 0);
        }
    }

    @Override
    String icon() {
        return "[E]";
    }

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + startTime.format(OUT_FORMAT)
                + ", to: " + endTime.format(OUT_FORMAT) + ")";
    }

    public LocalDateTime getFrom() {
        return this.startTime;
    }
    public LocalDateTime getTo() {
        return this.endTime;
    }
    public String getFromFormatted() {
        return this.startTime.format(OUT_FORMAT);
    }
    public String getToFormatted() {
        return this.endTime.format(OUT_FORMAT);
    }


    private static LocalDateTime parseFlexibleDateTime(String raw) {
        String s = raw.trim();

        for (DateTimeFormatter f : IN_FORMAT) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException e) {

            }
        }

        try {
            return LocalDateTime.parse(s);
        } catch (DateTimeParseException e) {
        }

        throw new DateTimeParseException("Unrecognized date-time: " + raw, raw, 0);
    }
}