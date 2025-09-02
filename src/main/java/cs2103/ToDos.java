package cs2103;

public class ToDos extends Task {

    public ToDos(String description) {
        super(description);
    }

    @Override
    public String icon() {
        return "[T]";
    }


}
