package cs2103;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    /**
     * Encapsulates the list of tasks and operations on the list.
     */

        private final List<Task> tasks;

        public TaskList() {
            this.tasks = new ArrayList<>();
        }

        public TaskList(List<Task> initial) {
            this.tasks = new ArrayList<>(initial);
        }

        public int size() {
            return tasks.size();
        }

        public List<Task> asUnmodifiableList() {
            return java.util.Collections.unmodifiableList(tasks);
        }

        public Task get(int zeroBasedIndex) {
            return tasks.get(zeroBasedIndex);
        }

        public Task add(Task t) {
            tasks.add(t);
            return t;
        }

        public Task remove(int zeroBasedIndex) {
            return tasks.remove(zeroBasedIndex);
        }

        public Task mark(int zeroBasedIndex) {
            Task t = tasks.get(zeroBasedIndex);
            t.markDone();
            return t;
        }

        public Task unmark(int zeroBasedIndex) {
            Task t = tasks.get(zeroBasedIndex);
            t.markUndone();
            return t;
        }
}



