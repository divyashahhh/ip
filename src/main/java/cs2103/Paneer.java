package cs2103;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Paneer chatbot
 * Loads and saves tasks, routes user commands and prints using formatted dates/times
 */
public class Paneer {

    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {

        Path cwd = Paths.get(System.getProperty("user.dir"));
        Path projectRoot = cwd.getFileName().toString().equals("text-ui-test")
                ? cwd.getParent()
                : cwd;

        final Path saveFile = projectRoot.resolve("data").resolve("paneer.txt");
        Storage storage = new Storage(saveFile);
        List<Task> tasks = new ArrayList<>(storage.load());

        Scanner sc = new Scanner(System.in);

        System.out.println(LINE);
        System.out.println(" Hello! I'm Paneer");
        System.out.println(" What can I cook up for you today?");
        System.out.println(LINE);

        if (!tasks.isEmpty()) {
            printCurrentList(tasks);
        }

        while (true) {
            String input = sc.nextLine().trim();

            try {
                if (input.isEmpty()) {
                    throw new PaneerException("Who do you think I am? Tofu? Type something worthy of my help!");
                }

                // exit
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println(LINE);
                    System.out.println(" Byeeee! Paneer shall serve you another day!");
                    System.out.println(LINE);
                    break;
                }

                // list
                if (input.equalsIgnoreCase("list")) {
                    printCurrentList(tasks);
                    continue;
                }

                // mark
                if (input.startsWith("mark ")) {
                    int index = parseOneBasedIndex(input.substring(5).trim(), tasks.size());
                    tasks.get(index).markDone();
                    storage.save(tasks);
                    System.out.println(LINE);
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(index));
                    System.out.println(LINE);
                    continue;
                }

                // unmark
                if (input.startsWith("unmark ")) {
                    int index = parseOneBasedIndex(input.substring(7).trim(), tasks.size());
                    tasks.get(index).markUndone();
                    storage.save(tasks);
                    System.out.println(LINE);
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(index));
                    System.out.println(LINE);
                    continue;
                }

                // add / delete
                String[] tokens = input.split(" ", 2); // [command, rest]
                String command = tokens[0];
                String rest = (tokens.length > 1) ? tokens[1].trim() : "";
                Task newTask = null;

                if (command.equalsIgnoreCase("delete")) {
                    int idx = parseOneBasedIndex(rest, tasks.size());
                    Task removed = tasks.remove(idx);
                    storage.save(tasks);
                    System.out.println(LINE);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removed);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);
                    continue;
                }

                if (command.equalsIgnoreCase("todo")) {
                    if (rest.isEmpty()) {
                        throw new PaneerException("Paneer has no idea what you mean, try again!");
                    }
                    newTask = new ToDos(rest);

                } else if (command.equalsIgnoreCase("deadline")) {
                    String[] deadlineParts = rest.split(" /by ", 2);
                    if (deadlineParts.length < 2
                            || deadlineParts[0].trim().isEmpty()
                            || deadlineParts[1].trim().isEmpty()) {
                        throw new PaneerException(
                                "Oh no, I can't decipher those timings. Use <desc> /by yyyy-MM-dd instead");
                    }
                    String desc = deadlineParts[0].trim();
                    String byStr = deadlineParts[1].trim();
                    try {
                        newTask = new Deadline(desc, byStr);
                    } catch (DateTimeParseException e) {
                        throw new PaneerException("☹ OOPS! Date must be like 2019-12-02 or 2/12/2019 (or a weekday name)");
                    }

                } else if (command.equalsIgnoreCase("event")) {
                    String[] fromParts = rest.split(" /from ", 2);
                    if (fromParts.length < 2 || fromParts[0].trim().isEmpty()) {
                        throw new PaneerException(
                                "Oh no, I can't decipher those timings! Use <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm instead");
                    }
                    String desc = fromParts[0].trim();
                    String[] toParts = fromParts[1].split(" /to ", 2);
                    if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                        throw new PaneerException(
                                "Paneer needs both start and end times! Use <desc> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
                    }
                    String startStr = toParts[0].trim();
                    String endStr = toParts[1].trim();
                    try {
                        newTask = new Event(desc, startStr, endStr);
                    } catch (DateTimeParseException e) {
                        throw new PaneerException("☹ OOPS! Times must be like 2019-12-02 1400 (or 2019-12-02 14:00)!");
                    }
                }

                if (newTask != null) {
                    tasks.add(newTask);
                    storage.save(tasks);
                    System.out.println(LINE);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);
                }

            } catch (PaneerException e) {
                System.out.println(LINE);
                System.out.println(" " + e.getMessage());
                System.out.println(LINE);
            } catch (NumberFormatException e) {
                System.out.println(LINE);
                System.out.println(" That doesn't look like a valid number. Try again!");
                System.out.println(LINE);
            } catch (IndexOutOfBoundsException e) {
                System.out.println(LINE);
                System.out.println(" That task number is out of range. Try again!");
                System.out.println(LINE);
            }
        }
    }

    // Helpers
    private static int parseOneBasedIndex(String s, int size) {
        int n = Integer.parseInt(s.trim());
        int idx = n - 1;
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException();
        }
        return idx;
    }

    private static void printCurrentList(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }
}