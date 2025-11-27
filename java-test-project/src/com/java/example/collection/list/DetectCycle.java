package com.java.example.collection.list;

// File: DetectCycle.java
public class DetectCycle {

    static class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    // ==========================
    // Solution: Floyd's Cycle-Finding Algorithm (Fast and Slow Pointers)
    // ==========================
    public static boolean hasCycle(Node head) {
        if (head == null || head.next == null) {
            // A list with 0 or 1 node cannot have a cycle
            return false;
        }

        Node slow = head;  // Moves one step at a time
        Node fast = head;  // Moves two steps at a time

        while (fast != null && fast.next != null) {
            slow = slow.next;          // Move slow pointer by 1
            fast = fast.next.next;     // Move fast pointer by 2

            // If the slow and fast pointers ever meet, a cycle exists
            if (slow == fast) {
                return true;
            }
        }

        // If the fast pointer reaches the end of the list (null), there is no cycle
        return false;
    }

    // Helper method to print the list (for acyclic lists only)
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


    // Main method to test the solution
    public static void main(String[] args) {
        // --- Test Case 1: List with no cycle ---
        System.out.println("--- Test Case 1: No Cycle ---");
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);

        printList(head1);
        System.out.println("Does this list have a cycle? " + hasCycle(head1)); // Expected: false

        System.out.println("\n------------------------------------------\n");

        // --- Test Case 2: List with a cycle ---
        System.out.println("--- Test Case 2: With Cycle ---");
        Node head2 = new Node(10);
        head2.next = new Node(20);
        head2.next.next = new Node(30);
        head2.next.next.next = new Node(40);
        head2.next.next.next.next = new Node(50);

        // Create a cycle: 50 -> 30
        head2.next.next.next.next.next = head2.next.next;

        // WARNING: Do not call printList(head2) here, as it will cause an infinite loop!
        System.out.println("A cycle has been created (50 -> 30).");
        System.out.println("Does this list have a cycle? " + hasCycle(head2)); // Expected: true
    }
}
