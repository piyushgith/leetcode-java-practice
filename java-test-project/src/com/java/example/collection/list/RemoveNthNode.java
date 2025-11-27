package com.java.example.collection.list;

// File: RemoveNthNode.java
public class RemoveNthNode {

    static class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    // Helper method to print the list
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
    // Solution: Fast and Slow Pointers with a Dummy Node
    // ==========================
    public static Node removeNthFromEnd(Node head, int n) {
        // Create a dummy node to serve as a starting point.
        // This handles edge cases like removing the head of the list.
        Node dummy = new Node(0);
        dummy.next = head;

        Node slow = dummy;
        Node fast = dummy;

        // Move the fast pointer n steps ahead
        for (int i = 0; i <= n; i++) {
            if (fast == null) {
                // This means n is larger than the list length.
                // Problem constraints usually prevent this.
                return head;
            }
            fast = fast.next;
        }

        // Move both pointers until the fast pointer reaches the end of the list
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }

        // At this point, slow.next is the node to be deleted.
        // Skip the node to delete it.
        if (slow.next != null) {
            slow.next = slow.next.next;
        }

        // The new head of the list is dummy.next
        return dummy.next;
    }

    // Main method to test the solution
    public static void main(String[] args) {
        // --- Test Case 1: Remove a middle node (n=2, so remove 30) ---
        System.out.println("--- Test Case 1: Remove a middle node ---");
        Node head1 = new Node(10);
        head1.next = new Node(20);
        head1.next.next = new Node(30);
        head1.next.next.next = new Node(40);
        head1.next.next.next.next = new Node(50);

        System.out.println("Original List:");
        printList(head1);
        head1 = removeNthFromEnd(head1, 2);
        System.out.println("List after removing 2nd node from end:");
        printList(head1); // Expected: 10 -> 20 -> 40 -> 50 -> null

        System.out.println("\n------------------------------------------\n");

        // --- Test Case 2: Remove the head node (n=4, so remove 10) ---
        System.out.println("--- Test Case 2: Remove the head node ---");
        Node head2 = new Node(1);
        head2.next = new Node(2);
        head2.next.next = new Node(3);
        head2.next.next.next = new Node(4);

        System.out.println("Original List:");
        printList(head2);
        head2 = removeNthFromEnd(head2, 4);
        System.out.println("List after removing 4th node from end:");
        printList(head2); // Expected: 2 -> 3 -> 4 -> null

        System.out.println("\n------------------------------------------\n");

        // --- Test Case 3: Remove the tail node (n=1, so remove 4) ---
        System.out.println("--- Test Case 3: Remove the tail node ---");
        System.out.println("Original List:");
        printList(head2);
        head2 = removeNthFromEnd(head2, 1);
        System.out.println("List after removing 1st node from end:");
        printList(head2); // Expected: 2 -> 3 -> null
    }
}
