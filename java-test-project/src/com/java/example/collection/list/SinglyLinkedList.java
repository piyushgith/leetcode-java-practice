package com.java.example.collection.list;


//**Implement a Singly Linked List with `insert`, `delete`, and `search` methods.**
// Create the SinglyLinkedList class
public class SinglyLinkedList {

    class Node {
        int data;
        Node next;

        // Constructor to create a new node
        Node(int d) {
            data = d;
            next = null;
        }
    }


    Node head; // The head is the first node in the list

    // Constructor for an empty list
    public SinglyLinkedList() {
        head = null;
    }

    // Method to insert a new node at the end of the list
    public void insert(int data) {
        // Create a new node
        Node newNode = new Node(data);

        // If the list is empty, the new node becomes the head
        if (head == null) {
            head = newNode;
            return;
        }

        // Otherwise, traverse to the end of the list
        Node last = head;
        while (last.next != null) {
            last = last.next;
        }

        // Insert the new node at the end
        last.next = newNode;
    }

    // Method to delete a node with a given key (data)
    public void delete(int key) {
        // Store head node
        Node current = head;
        Node prev = null;

        // CASE 1: The node to be deleted is the head node
        if (current != null && current.data == key) {
            head = current.next; // Change head
            System.out.println(key + " found and deleted (was the head).");
            return;
        }

        // CASE 2: Search for the key to be deleted, keeping track of the previous node
        while (current != null && current.data != key) {
            prev = current;
            current = current.next;
        }

        // If the key was not present in the list
        if (current == null) {
            System.out.println(key + " not found in the list.");
            return;
        }

        // Unlink the node from the linked list
        // The previous node's 'next' now points to the current node's 'next'
        prev.next = current.next;
        System.out.println(key + " found and deleted.");
    }

    // Method to search for a node with a given key
    public boolean search(int key) {
        Node current = head; // Start from the head

        // Traverse through the list
        while (current != null) {
            // If data is found in the current node, return true
            if (current.data == key) {
                return true;
            }
            current = current.next; // Move to the next node
        }

        // If we reach the end of the list and the key is not found
        return false;
    }

    // Helper method to print the contents of the linked list
    public void printList() {
        Node current = head;
        if (current == null) {
            System.out.println("List is empty.");
            return;
        }
        System.out.print("LinkedList: ");
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();

        list.printList(); // List is empty

        // Insert elements
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insert(40);
        list.insert(50);

        list.printList(); // LinkedList: 10 -> 20 -> 30 -> 40 -> 50 -> null

        // Search for elements
        System.out.println("Is 30 in the list? " + list.search(30)); // true
        System.out.println("Is 99 in the list? " + list.search(99)); // false

        // Delete elements
        list.delete(30); // Delete a middle element
        list.printList(); // LinkedList: 10 -> 20 -> 40 -> 50 -> null

        list.delete(10); // Delete the head element
        list.printList(); // LinkedList: 20 -> 40 -> 50 -> null

        list.delete(99); // Try to delete an element that doesn't exist
        list.printList(); // LinkedList: 20 -> 40 -> 50 -> null
    }
}
