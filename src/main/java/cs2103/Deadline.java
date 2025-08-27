package cs2103;

public class Deadline extends Task {

    private final String by;

    public Deadline(String description, String by) {
      super(description);
      this.by = by;
    }

    @Override
    public String icon() {
        return "[D]";
    }

    @Override
    public String toString() {
        return icon() + getStatusIcon() + " " + description + " (by: " + by + ")";
    }

    public String getBy() {
        return this.by;
    }

}
