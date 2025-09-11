package cs2103;
import java.awt.color.ICC_ColorSpace;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    /**
     * Encapsulates the list of tasks and operations on the list.
     */

        private final List<Task> tasks;


        public TaskList(List<Task> initial) {
            this.tasks = new ArrayList<>(initial);
        }

        public int size() {
            return tasks.size();
        }

        public List<Task> asUnmodifiableList() {
            return java.util.Collections.unmodifiableList(tasks);
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

        public List<Task> find(String keyword) {
            String k = keyword.toLowerCase();
            List<Task> out = new ArrayList<>();
            for (Task t : tasks) {
                if (t.getDescription().toLowerCase().contains(k)) {
                out.add(t);
                }
            }
            return out;
        }

        public List<Task> sortedByDeadline() {
            return tasks.stream()
                   .sorted(Comparator.comparing(t -> (t instanceof Deadline)
                                                            ? ((Deadline) t).getBy()
                                                            : LocalDate.MAX
                                                ))
                   .collect(Collectors.toList());
        }




}



