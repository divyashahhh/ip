import java.util.Scanner;

public class Paneer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String logo =
                """
                         ____                             \s
                        |  _ \\   __ _  _ __   ___  ___ _ __\s
                        | |_) | / _` || '_ \\ / _ \\/ _ \\ '__|
                        |  __/ | (_| || | | |  __/  __/ |  \s
                        |_|     \\__,_||_| |_|\\___|\\___|_|  \s
                        """;
        System.out.println("Hello from\n" + logo);

        final String LINE = "____________________________________________________________";

        Task[] tasks = new Task[100];
        int size = 0;

        System.out.println(LINE);
        System.out.println(" Hello! I'm Paneer");
        System.out.println(" What can I do for you today?");
        System.out.println(LINE);

        while (true) {
            String input = sc.nextLine();

            // exit
            if (input.equalsIgnoreCase("bye")) {
                System.out.println(LINE);
                System.out.println(" Byeeee! Paneer shall serve you another time!");
                System.out.println(LINE);
                break;
            }

            // list
            if (input.equalsIgnoreCase("list")) {
                System.out.println(LINE);
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < size; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            // mark
            if (input.startsWith("mark ")) {
                char c = input.charAt(input.length() - 1); // last char
                int index = (c - '0') - 1;                 // convert char to number
                if (index >= 0 && index < size) {
                    tasks[index].markDone();
                    System.out.println(LINE);
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[index]);
                    System.out.println(LINE);
                }
                continue;
            }

            // unmark
            if (input.startsWith("unmark ")) {
                char c = input.charAt(input.length() - 1); //last char
                int index = (c - '0') - 1;
                if (index >= 0 && index < size) {
                    tasks[index].markUndone();
                    System.out.println(LINE);
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[index]);
                    System.out.println(LINE);
                }
                continue;
            }
            // add task
            if (size < tasks.length) {
                tasks[size] = new Task(input);
                size++;
                System.out.println(LINE);
                System.out.println(" added: " + input);
                System.out.println(LINE);
            }
        }

        sc.close();
    }
}

