package cs2103;
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

        public Task get(int zeroBasedIndex) {
            return tasks.get(zeroBasedIndex);
        }

        public void set(int zeroBasedIndex, Task t) {
            tasks.set(zeroBasedIndex, t);
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

        /** Deep snapshot suitable for undo (new list + task re-instantiation). */
        public List<Task> snapshot() {
            List<Task> copy = new ArrayList<>(tasks.size());
            for (Task t : tasks) {
                Task cloned;
                if (t instanceof Deadline) {
                    Deadline d = (Deadline) t;
                    // Use ISO-8601 for round-trippable parsing
                    cloned = new Deadline(d.getDescription(), d.getBy().toString());
                } else if (t instanceof Event) {
                    Event e = (Event) t;
                    // Use ISO-8601 LocalDateTime strings (accepted by parser)
                    cloned = new Event(e.getDescription(), e.getFrom().toString(), e.getTo().toString());
                } else {
                    cloned = new ToDos(t.getDescription());
                }
                if (t.isDone) {
                    cloned.markDone();
                }
                copy.add(cloned);
            }
            return copy;
        }

        /** Restore full list from a deep snapshot. */
        public void restore(List<Task> snapshot) {
            tasks.clear();
            tasks.addAll(snapshot);
        }
}



