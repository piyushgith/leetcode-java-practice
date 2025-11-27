package com.java.example.collection.stack;

// File: Stack With LinkedList.java


public class StackWithLinkedList {
    class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    private Node top; // The top of the stack is the head of the linked list

    // Constructor to initialize an empty stack
    public StackWithLinkedList() {
        top = null;
    }

    // ==========================
    // Stack Operations
    // ==========================

    /**
     * Pushes an element onto the top of the stack.
     * Time Complexity: O(1)
     *
     * @param data The integer to push onto the stack.
     */
    public void push(int data) {
        // Create a new node
        Node newNode = new Node(data);

        // Set the next of the new node to the current top
        newNode.next = top;

        // Update the top to be the new node
        top = newNode;
        System.out.println(data + " pushed to stack");
    }

    /**
     * Removes and returns the top element of the stack.
     * Time Complexity: O(1)
     *
     * @return The integer at the top of the stack.
     * @throws java.util.EmptyStackException if the stack is empty.
     */
    public int pop() {
        // If the stack is empty, throw an exception
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot pop.");
            throw new java.util.EmptyStackException();
        }

        // Store the data from the top node
        int poppedData = top.data;

        // Move the top to the next node in the list
        top = top.next;

        return poppedData;
    }

    /**
     * Looks at the top element of the stack without removing it.
     * Time Complexity: O(1)
     *
     * @return The integer at the top of the stack.
     * @throws java.util.EmptyStackException if the stack is empty.
     */
    public int peek() {
        // If the stack is empty, throw an exception
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot peek.");
            throw new java.util.EmptyStackException();
        }
        return top.data;
    }

    /**
     * Checks if the stack is empty.
     * Time Complexity: O(1)
     *
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return top == null;
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        StackWithLinkedList stack = new StackWithLinkedList();

        System.out.println("Is stack empty? " + stack.isEmpty()); // Expected: true

        // Push elements onto the stack
        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println("\nTop element is: " + stack.peek()); // Expected: 30
        System.out.println("Is stack empty? " + stack.isEmpty()); // Expected: false

        // Pop elements from the stack
        System.out.println("\nPopped element: " + stack.pop()); // Expected: 30
        System.out.println("Popped element: " + stack.pop()); // Expected: 20

        System.out.println("\nTop element is now: " + stack.peek()); // Expected: 10

        System.out.println("\nPopped element: " + stack.pop()); // Expected: 10

        // Try to pop from an empty stack
        try {
            System.out.println("\nTrying to pop from an empty stack...");
            stack.pop();
        } catch (java.util.EmptyStackException e) {
            System.out.println("Caught expected exception: " + e);
        }
    }
}
