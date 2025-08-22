package cs2103;

import java.util.Scanner;
import java.util.ArrayList;

public class Paneer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        final String LINE = "____________________________________________________________";

        ArrayList<Task> tasks = new ArrayList<>();


        System.out.println(LINE);
        System.out.println(" Hello! I'm Paneer");
        System.out.println(" What can I cook up for you today?");
        System.out.println(LINE);

        while (true) {
            String input = sc.nextLine();

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
                    System.out.println(LINE);
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println(LINE);
                    continue;
                }

                // mark
                if (input.startsWith("mark ")) {
                    char c = input.charAt(input.length() - 1); // last char
                    int index = (c - '0') - 1;                 // convert char to number
                    if (index >= 0 && index < tasks.size()) {
                        tasks.get(index).markDone();
                        System.out.println(LINE);
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks.get(index));
                        System.out.println(LINE);
                    }
                    continue;
                }

                // unmark
                if (input.startsWith("unmark ")) {
                    char c = input.charAt(input.length() - 1); //last char
                    int index = (c - '0') - 1;
                    if (index >= 0 && index < tasks.size()) {
                        tasks.get(index).markUndone();
                        System.out.println(LINE);
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks.get(index));
                        System.out.println(LINE);
                    }
                    continue;
                }




                // add task
                String[] parts = input.split(" ", 2); // split into [command, rest]
                String command = parts[0];
                String rest = (parts.length > 1) ? parts[1] : "";
                Task newTask = null;

                if (command.equals("delete")) {
                    Task removed = tasks.remove((Integer.parseInt(parts[1]) -1));
                    System.out.println(LINE);
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removed);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);
                }

                if (command.equalsIgnoreCase("todo")) {
                    if (rest.isEmpty()) {
                        throw new PaneerException("I have no idea what you mean, try again.");
                    }
                    newTask = new ToDos(rest);

                } else if (command.equalsIgnoreCase("deadline")) {
                    String[] dparts = rest.split("/by", 2);
                    if (dparts.length < 2 || dparts[1].trim().isEmpty()) {
                        throw new PaneerException("I can't decipher those timings, try again.");
                    }
                    newTask = new Deadline(dparts[0].trim(), dparts[1].trim());

                } else if (command.equalsIgnoreCase("event")) {
                    String[] fparts = rest.split("/from", 2);
                    if (fparts.length < 2) {
                        throw new PaneerException("I can't decipher those timings, try again.");
                    }
                    String desc = fparts[0];
                    String[] tparts = fparts[1].split("/to", 2);
                    if (tparts.length == 2) {
                        newTask = new Event(desc, tparts[0].trim(), tparts[1].trim());
                    }
                }


                if (newTask != null) {
                    tasks.add(newTask);
                    System.out.println(LINE);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);
                    continue;
                }


            } catch (PaneerException e) {
                System.out.println(LINE);

                System.out.println(e.getMessage());
                System.out.println(LINE);
            }

        }
    }
}


