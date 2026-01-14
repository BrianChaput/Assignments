// Brian Chaput
// 10/25/22
// CS-145
// Assignment 2: Phone Book
// This program uses a linked list and allows the user to
// create a phone book. They can add, remove, print, and edit entries.
//
// Path: PhoneBookManager.java
// This class will manage the phone book. It will be able to add, remove, edit,
// and print the phone book.
import java.util.*;
public class PhoneBookManager {
    private PhoneBookNode node;
    private int size;

    // Constructs a phone book manager with the given data.
    public PhoneBookManager() {
        node = null;
        size = 0;
    }

    // Adds a new entry to the phone book.
    public void add(Scanner console) {
        System.out.print("Name: ");
        String name = console.nextLine();
        System.out.print("Number: ");
        String number = console.nextLine();
        System.out.print("City: ");
        String city = console.nextLine();
        System.out.print("Address: ");
        String address = console.nextLine();
        node = new PhoneBookNode(name, number, city, address, node);
        size++;
    }

    // Removes an entry from the phone book.
    public void remove(Scanner console) {
        if (size == 0) {
            System.out.println("The phone book is empty!");
        } else {
            System.out.print("Name: ");
            String name = console.nextLine();
            PhoneBookNode current = node;
            PhoneBookNode previous = null;
            while (current != null && !current.getName().equals(name)) {
                previous = current;
                current = current.getNext();
            }
            if (current == null) {
                System.out.println("That name is not in the phone book!");
            } else {
                if (previous == null) {
                    node = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                size--;
            }
        }
    }

    // Edits an entry in the phone book.
    public void edit(Scanner console) {
        if (size == 0) {
            System.out.println("The phone book is empty!");
        } else {
            System.out.print("Name: ");
            String name = console.nextLine();
            PhoneBookNode current = node;
            while (current != null && !current.getName().equals(name)) {
                current = current.getNext();
            }
            if (current == null) {
                System.out.println("That name is not in the phone book!");
            } else {
                System.out.print("Number: ");
                String number = console.nextLine();
                System.out.print("City: ");
                String city = console.nextLine();
                System.out.print("Address: ");
                String address = console.nextLine();
                current.setNumber(number);
                current.setCity(city);
                current.setAddress(address);
            }
        }

    }
    // Prints the contacts in the phone book.
    public void print() {
        if (size == 0) {
            System.out.println("The phone book is empty!");
        } else {
            PhoneBookNode current = node;
            while (current != null) {
                System.out.println(current);
                current = current.getNext();
            }
        }
    }

}
