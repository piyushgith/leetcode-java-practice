package com.java.example.collection.list;


// File: FindMiddleElement.java
public class FindMiddleElement {

    static class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    // Helper method to print the list (for testing)
    public static void printList(Node head) {
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

    // ==========================
    // Solution: Fast and Slow Pointers
    // ==========================
    public static Node findMiddle(Node head) {
        // Edge case: if the list is empty or has only one node
        if (head == null) {
            return null;
        }

        Node slow = head;  // Moves one step at a time
        Node fast = head;  // Moves two steps at a time

        // The loop continues as long as the fast pointer can move two steps
        while (fast != null && fast.next != null) {
            slow = slow.next;          // Move slow pointer by 1
            fast = fast.next.next;     // Move fast pointer by 2
        }

        // When the loop ends, the slow pointer will be at the middle
        return slow;
    }

    // Main method to test the solution
    public static void main(String[] args) {
        // --- Test Case 1: Odd number of nodes ---
        System.out.println("--- Test Case 1: Odd-length list ---");
        Node head1 = new Node(10);
        head1.next = new Node(20);
        head1.next.next = new Node(30);
        head1.next.next.next = new Node(40);
        head1.next.next.next.next = new Node(50);

        printList(head1);
        Node middle1 = findMiddle(head1);
        if (middle1 != null) {
            System.out.println("The middle element is: " + middle1.data); // Expected: 30
        }

        System.out.println("\n------------------------------------------\n");

        // --- Test Case 2: Even number of nodes ---
        System.out.println("--- Test Case 2: Even-length list ---");
        Node head2 = new Node(1);
        head2.next = new Node(2);
        head2.next.next = new Node(3);
        head2.next.next.next = new Node(4);
        head2.next.next.next.next = new Node(5);
        head2.next.next.next.next.next = new Node(6);

        printList(head2);
        Node middle2 = findMiddle(head2);
        if (middle2 != null) {
            System.out.println("The middle element is: " + middle2.data); // Expected: 4 (the second of the two middle nodes)
        }
    }
}
