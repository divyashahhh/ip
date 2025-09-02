package cs2103;

public abstract class Task {

        public final String description;
        boolean isDone;

        public Task(String description) {
            this.description = description;
            this.isDone = false;
        }

        public void markDone() {
            this.isDone = true;
        }

        public void markUndone() {
            this.isDone = false;
        }

        public String getStatusIcon() {
            return (isDone ? "[X]" : "[ ]");
        }

        abstract String icon();

        @Override
        public String toString() {
            return icon() + getStatusIcon() + " " + description;
        }

        public String getDescription() {
            return this.description;
        }
}

