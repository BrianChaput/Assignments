// Brian Chaput
// 10/25/22
// CS-145
// Assignment 2: Phone Book
// This program uses a linked list and allows the user to
// create a phone book. They can add, remove, print, and edit entries.
//
// Path: PhoneBookNode.java
// This class will create a node for the phone book. It will store the name,
// number, city, address, and next node.
public class PhoneBookNode {
    private String name;
    protected String number;
    protected String city;
    protected String address;
    protected PhoneBookNode next;

    // Constructs a phone book node with the given data.
    public PhoneBookNode(String name, String number, String city,
                         String address, PhoneBookNode next) {
        this.name = name;
        this.number = number;
        this.city = city;
        this.address = address;
        this.next = next;
    }

    public PhoneBookNode() {
      this.next = null;
    }

    // Returns the name of this node.
    public String getName() {
        return name;
    }

    // Returns the next node of this node.
    public PhoneBookNode getNext() {
        return next;
    }

    // Sets the number of this node.
    public void setNumber(String number) {
        this.number = number;
    }

    // Sets the city of this node.
    public void setCity(String city) {
        this.city = city;
    }

    // Sets the address of this node.
    public void setAddress(String address) {
        this.address = address;
    }

    // Sets the next node of this node.
    public void setNext(PhoneBookNode next) {
        this.next = next;
    }
}
