package cs2103;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * A task that has a single due date
 * Accepts ISO dates (yyyy-MM-dd), common d/M/uuuu or d-M-uuuu formats, and day-of-week words (e.g., "sunday").
 * Always prints date as "MMM d yyyy".
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate by;

    /**
     * Constructs a Deadline with a description and a raw date string
     *
     * @param description the task description
     * @param byRaw       a date string
     * @throws DateTimeParseException if the date cannot be processed
     */
    public Deadline(String description, String byRaw) {
        super(description);
        this.by = parseFlexibleDate(byRaw);
    }

    @Override
    String icon() {
        return "[D]";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUT_FORMAT) + ")";
    }

    public LocalDate getBy() {
        return this.by;
    }

    public String getByFormatted() {
        return this.by.format(OUT_FORMAT);
    }


    private static LocalDate parseFlexibleDate(String raw) {
        String s = raw.trim();

        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
        }

        DateTimeFormatter[] fmts = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("d/M/uuuu"),
                DateTimeFormatter.ofPattern("d-M-uuuu"),
                DateTimeFormatter.ofPattern("uuuu/M/d"),
                DateTimeFormatter.ofPattern("uuuu-M-d")
        };
        for (DateTimeFormatter f : fmts) {
            try {
                return LocalDate.parse(s, f);
            } catch (DateTimeParseException ignore) {
            }
        }

        DayOfWeek dow = parseDayOfWeekOrNull(s);
        if (dow != null) {
            LocalDate today = LocalDate.now();
            int add = (dow.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
            if (add == 0) {
                add = 7;
            }
            return today.plusDays(add);
        }

        throw new DateTimeParseException("Unrecognized date: " + raw, raw, 0);
    }
    //helper function for recognising day of week
    private static DayOfWeek parseDayOfWeekOrNull(String s) {
        String t = s.toLowerCase(Locale.ROOT);
        switch (t) {
            case "mon":
            case "monday":
                return DayOfWeek.MONDAY;
            case "tue":
            case "tues":
            case "tuesday":
                return DayOfWeek.TUESDAY;
            case "wed":
            case "wednesday":
                return DayOfWeek.WEDNESDAY;
            case "thu":
            case "thur":
            case "thurs":
            case "thursday":
                return DayOfWeek.THURSDAY;
            case "fri":
            case "friday":
                return DayOfWeek.FRIDAY;
            case "sat":
            case "saturday":
                return DayOfWeek.SATURDAY;
            case "sun":
            case "sunday":
                return DayOfWeek.SUNDAY;
            default:
                return null;
        }
    }
}