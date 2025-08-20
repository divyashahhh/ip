import java.util.Scanner;
public class Paneer {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String logo =
                " ____                              \n"
                        + "|  _ \\   __ _  _ __   ___  ___ _ __ \n"
                        + "| |_) | / _` || '_ \\ / _ \\/ _ \\ '__|\n"
                        + "|  __/ | (_| || | | |  __/  __/ |   \n"
                        + "|_|     \\__,_||_| |_|\\___|\\___|_|   \n";
        System.out.println("Hello from\n" + logo);
        String line = "____________________________________________________________";
        System.out.println(line);
        System.out.println("Hello! I'm Paneer");
        System.out.println("What can I do for you today?");

        while (true) {
            String input = s.nextLine();
            if (input.equals("bye") || input.equals("Bye")) {
                System.out.println(line);
                System.out.println("Byeeeeee! Paneer shall serve you another day!");
                System.out.println(line);
                break;
            }
            System.out.println(line);
            System.out.println(input);
            System.out.println(line);
        }

    }


}
