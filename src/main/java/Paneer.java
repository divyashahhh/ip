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

        String[] list = new String[100];
        int size = 0;

        System.out.println(LINE);
        System.out.println(" Hello! I'm Paneer");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);

        while (true) {
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println(LINE);
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if (input.equalsIgnoreCase("list")) {
                System.out.println(LINE);
                for (int i = 0; i < size; i++) {
                    System.out.println(" " + (i + 1) + ". " + list[i]);
                }
                System.out.println(LINE);
                continue;
            }
            if (size < list.length) {
                list[size] = input;
                size++;
                System.out.println(LINE);
                System.out.println(" added: " + input);
                System.out.println(LINE);
            } else {
                System.out.println(LINE);
                System.out.println(" Sorry, I can only store up to 100 list.");
                System.out.println(LINE);
            }
        }

        sc.close();
    }
}

