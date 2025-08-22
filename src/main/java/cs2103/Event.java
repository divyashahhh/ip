package cs2103;

public class Event extends Task {
    String startTime;
    String endTime;

    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String icon() {
        return "[E]";
    }

    @Override
    public String toString() {
        return icon() + getStatusIcon() + " " + description + " (from: " + startTime + ", to: " + endTime + ")";
    }

}
