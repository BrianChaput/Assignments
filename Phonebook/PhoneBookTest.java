// Brian Chaput
// 10/25/22
// CS-145
// Assignment 2: Phone Book
// This program uses a linked list and allows the user to
// create a phone book. They can add, remove, print, and edit entries.
//
// Path: PhoneBookTest.java
import java.util.*;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBookNode List = new PhoneBookNode();
        PhoneBookManager manager = new PhoneBookManager();

        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to your phone book!");
        System.out.println();
        System.out.println("Please choose one of the following:");
        System.out.println("   a - add a new entry");
        System.out.println("   r - remove an entry");
        System.out.println("   e - edit an entry");
        System.out.println("   p - print the phone book");
        System.out.println("   q - quit");
        System.out.println();
        System.out.print("What would you like to do?: ");
        String command = console.nextLine();
        while (!command.equals("q")) {
            switch (command) {
                case "a" -> manager.add(console);
                case "r" -> manager.remove(console);
                case "e" -> manager.edit(console);
                case "p" -> manager.print();
                default -> System.out.println("Invalid command!");
            }
            System.out.println();
            System.out.print("What would you like to do?: ");
            command = console.nextLine();
        }
        System.out.println("Thank you for using the phone book!");
    }

    }
