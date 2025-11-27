package com.java.example.collection.list;


public class ReverseLinkedList {

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
    // Solution 1: Iterative Approach
    // ==========================
    public static Node reverseIterative(Node head) {
        // If the list is empty or has only one node, it's already "reversed"
        if (head == null || head.next == null) {
            return head;
        }

        Node previous = null;
        Node current = head;
        Node next = null;

        while (current != null) {
            // 1. Store the next node before we overwrite current.next
            next = current.next;

            // 2. Reverse the pointer of the current node
            current.next = previous;

            // 3. Move pointers one position ahead for the next iteration
            previous = current;
            current = next;
        }

        // At the end of the loop, 'previous' will be the new head of the reversed list
        return previous;
    }

    // ==========================
    // Solution 2: Recursive Approach
    // ==========================
    public static Node reverseRecursive(Node head) {
        // Base case: if the list is empty or has only one node
        if (head == null || head.next == null) {
            return head;
        }

        // Recursively reverse the rest of the list (from the second node onwards)
        Node newHead = reverseRecursive(head.next);

        // After the recursive call, 'head.next' is the last node of the reversed sub-list.
        // We need to point its 'next' back to the current 'head'.
        // For example: 1 -> 2 -> 3 becomes 1 -> 2 <- 3
        // We need to make 2.next point to 1.
        head.next.next = head;

        // The current 'head' is now the last node, so its 'next' should be null.
        head.next = null;

        // 'newHead' is the head of the fully reversed list, which we pass up the call stack.
        return newHead;
    }


    // Main method to test the solutions
    public static void main(String[] args) {
        // Create a sample linked list: 10 -> 20 -> 30 -> 40
        Node head = new Node(10);
        head.next = new Node(20);
        head.next.next = new Node(30);
        head.next.next.next = new Node(40);

        System.out.println("Original List:");
        printList(head);

        // --- Test Iterative Solution ---
        System.out.println("\nReversing using Iterative method...");
        Node reversedHeadIterative = reverseIterative(head);
        printList(reversedHeadIterative);

        // --- Test Recursive Solution ---
        // Reverse it back again to the original order
        System.out.println("\nReversing back using Recursive method...");
        Node reversedHeadRecursive = reverseRecursive(reversedHeadIterative);
        printList(reversedHeadRecursive);
    }
}
