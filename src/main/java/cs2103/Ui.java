//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cs2103;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        this.showLine();
        System.out.println(" Namaste! I'm Paneer — your spicy sidekick for tasks.");
        System.out.println(" Tell me what to cook up, and I'll serve it hot.");
        this.showLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public String readCommand() {
        return this.scanner.nextLine().trim();
    }

    public void showList(List<Task> tasks) {
        this.showLine();
        System.out.println(" Here’s today’s menu — your tasks:");

        for(int i = 0; i < tasks.size(); ++i) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }

        this.showLine();
    }

    public void showAdd(Task t, int newCount) {
        this.showLine();
        System.out.println(" Freshly added to the menu:");
        System.out.println("   " + String.valueOf(t));
        System.out.println(" Now you have " + newCount + " tasks in the list.");
        this.showLine();
    }

    public void showRemove(Task t, int newCount) {
        this.showLine();
        System.out.println(" Removed from the thali:");
        System.out.println("   " + String.valueOf(t));
        System.out.println(" Now you have " + newCount + " tasks in the list.");
        this.showLine();
    }

    public void showMark(Task t) {
        this.showLine();
        System.out.println(" Masala! Marked as done:");
        System.out.println("   " + String.valueOf(t));
        this.showLine();
    }

    public void showUnmark(Task t) {
        this.showLine();
        System.out.println(" Keeping it on the stove — marked as not done yet:");
        System.out.println("   " + String.valueOf(t));
        this.showLine();
    }

    public void showExit() {
        this.showLine();
        System.out.println(" Byeeee! Paneer will plate up again another day!");
        this.showLine();
    }

    public void showError(String message) {
        this.showLine();
        System.out.println(" " + message);
        this.showLine();
    }

    public void showFind(List<Task> matches) {
        this.showLine();
        if (matches.isEmpty()) {
            System.out.println(" No matching dishes found :(");
        } else {
            System.out.println(" Paneer found these matching bites in your list:");

            for(int i = 0; i < matches.size(); ++i) {
                System.out.printf(" %d.%s%n", i + 1, matches.get(i));
            }
        }

        this.showLine();
    }

    public static String formatList(List<Task> tasks) {
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");

        for(int i = 0; i < tasks.size(); ++i) {
            sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
        }

        return sb.toString().trim();
    }

    public static String formatFind(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "No matching tasks found.";
        } else {
            StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");

            for(int i = 0; i < tasks.size(); ++i) {
                sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
            }

            return sb.toString().trim();
        }
    }

    public static String formatSorted(java.util.List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "No tasks to sort.";
        }
        StringBuilder sb = new StringBuilder("Here are your tasks sorted by deadline:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
        }
        return sb.toString().trim();
    }

    public void showSorted(List<Task> tasks) {
        showLine();
        System.out.println("Here are your tasks sorted by deadline:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }
}
