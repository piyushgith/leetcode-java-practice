package com.java.example.collection.list;

// File: MergeTwoSortedLists.java
public class MergeTwoSortedLists {

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
    // Solution 1: Iterative Approach (Recommended)
    // ==========================
    public static Node mergeLists(Node list1, Node list2) {
        // Create a dummy node to act as the starting point of the merged list.
        // This simplifies handling the head of the new list.
        Node dummy = new Node(0);
        Node tail = dummy; // 'tail' will always point to the last node in the merged list

        // Traverse both lists as long as there are nodes in both
        while (list1 != null && list2 != null) {
            if (list1.data <= list2.data) {
                // Append the smaller node from list1
                tail.next = list1;
                list1 = list1.next; // Move to the next node in list1
            } else {
                // Append the smaller node from list2
                tail.next = list2;
                list2 = list2.next; // Move to the next node in list2
            }
            tail = tail.next; // Move the tail forward
        }

        // At this point, one of the lists is exhausted.
        // Append the remaining nodes from the non-empty list.
        // Only one of these conditions will be true.
        if (list1 != null) {
            tail.next = list1;
        } else {
            tail.next = list2;
        }

        // The merged list starts after the dummy node
        return dummy.next;
    }

    // ==========================
    // Solution 2: Recursive Approach (Elegant but uses more space)
    // ==========================
    public static Node mergeListsRecursive(Node list1, Node list2) {
        // Base case: if one list is empty, return the other
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }

        // Recursive step:
        if (list1.data <= list2.data) {
            // The current node is from list1.
            // The rest of the list is the result of merging the rest of list1 with all of list2.
            list1.next = mergeListsRecursive(list1.next, list2);
            return list1;
        } else {
            // The current node is from list2.
            // The rest of the list is the result of merging list1 with the rest of list2.
            list2.next = mergeListsRecursive(list1, list2.next);
            return list2;
        }
    }


    // Main method to test the solution
    public static void main(String[] args) {
        // Create first sorted list: 1 -> 3 -> 5
        Node list1 = new Node(1);
        list1.next = new Node(3);
        list1.next.next = new Node(5);

        // Create second sorted list: 2 -> 4 -> 6
        Node list2 = new Node(2);
        list2.next = new Node(4);
        list2.next.next = new Node(6);

        System.out.println("--- Test Case 1: Iterative Merge ---");
        System.out.println("List 1:");
        printList(list1);
        System.out.println("List 2:");
        printList(list2);

        Node mergedHead = mergeLists(list1, list2);
        System.out.println("Merged List:");
        printList(mergedHead); // Expected: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> null

        System.out.println("\n------------------------------------------\n");

        // --- Test Case 2: Recursive Merge with different lists ---
        System.out.println("--- Test Case 2: Recursive Merge ---");
        Node list3 = new Node(10);
        list3.next = new Node(50);

        Node list4 = new Node(20);
        list4.next = new Node(30);
        list4.next.next = new Node(40);

        System.out.println("List 3:");
        printList(list3);
        System.out.println("List 4:");
        printList(list4);

        Node mergedHeadRecursive = mergeListsRecursive(list3, list4);
        System.out.println("Merged List (Recursive):");
        printList(mergedHeadRecursive); // Expected: 10 -> 20 -> 30 -> 40 -> 50 -> null
    }
}
