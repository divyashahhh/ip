package cs2103;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all user interaction for the Paneer.
 */
public class UI {

    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Paneer");
        System.out.println(" What can I cook up for you today?");
        showLine();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showList(List<Task> tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    public void showAdd(Task t, int newCount) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + newCount + " tasks in the list.");
        showLine();
    }

    public void showRemove(Task t, int newCount) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + newCount + " tasks in the list.");
        showLine();
    }

    public void showMark(Task t) {
        showLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t);
        showLine();
    }

    public void showUnmark(Task t) {
        showLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        showLine();
    }

    public void showExit() {
        showLine();
        System.out.println(" Byeeee! Paneer shall serve you another day!");
        showLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    public void showFind(List<Task> matches) {
        showLine();
        if (matches.isEmpty()) {
            System.out.println(" No matching tasks found :(");
        } else {
            System.out.println(" Paneer found these matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.printf(" %d.%s%n", i + 1, matches.get(i));
            }
        }
        showLine();
    }

    public static String formatList(java.util.List<Task> tasks) {
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
        }
        return sb.toString().trim();
    }

    public static String formatFind(java.util.List<Task> tasks) {
        if (tasks.isEmpty()) return "No matching tasks found.";
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
        }
        return sb.toString().trim();
    }
}
