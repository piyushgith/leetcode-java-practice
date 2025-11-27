package com.java.example.collection.stack;

// File: StackWithTwoQueues.java

import java.util.Queue;
import java.util.LinkedList;

public class StackWithTwoQueues {
    private Queue<Integer> q1;
    private Queue<Integer> q2;

    // Constructor to initialize the stack
    public StackWithTwoQueues() {
        q1 = new LinkedList<>();
        q2 = new LinkedList<>();
    }

    // ==========================
    // Stack Operations
    // ==========================

    /**
     * Pushes an element onto the top of the stack.
     * Time Complexity: O(n)
     * @param data The integer to push onto the stack.
     */
    public void push(int data) {
        // 1. Add the new element to the empty queue (q2)
        q2.add(data);

        // 2. Move all elements from the non-empty queue (q1) to q2.
        // This places the new element behind all the old ones, making it the first to be dequeued.
        while (!q1.isEmpty()) {
            q2.add(q1.remove());
        }

        // 3. Swap the references of q1 and q2.
        // Now, q1 contains all the elements in stack order (newest at the front),
        // and q2 is empty again, ready for the next push operation.
        Queue<Integer> temp = q1;
        q1 = q2;
        q2 = temp;

        System.out.println(data + " pushed to stack");
    }

    /**
     * Removes and returns the top element of the stack.
     * Time Complexity: O(1)
     * @return The integer at the top of the stack.
     * @throws java.util.NoSuchElementException if the stack is empty.
     */
    public int pop() {
        // If the primary queue q1 is empty, the stack is empty
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot pop.");
            throw new java.util.NoSuchElementException();
        }
        // The top element is always at the front of q1
        return q1.remove();
    }

    /**
     * Looks at the top element of the stack without removing it.
     * Time Complexity: O(1)
     * @return The integer at the top of the stack.
     * @throws java.util.NoSuchElementException if the stack is empty.
     */
    public int peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot peek.");
            throw new java.util.NoSuchElementException();
        }
        // The top element is always at the front of q1
        return q1.peek();
    }

    /**
     * Checks if the stack is empty.
     * Time Complexity: O(1)
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        // The stack is empty only if the primary queue q1 is empty
        return q1.isEmpty();
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        StackWithTwoQueues stack = new StackWithTwoQueues();

        System.out.println("Is stack empty? " + stack.isEmpty()); // Expected: true

        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println("\nTop element is: " + stack.peek()); // Expected: 30

        System.out.println("\nPopped element: " + stack.pop()); // Expected: 30
        System.out.println("Top element is now: " + stack.peek()); // Expected: 20

        stack.push(40);
        System.out.println("\nTop element after pushing 40: " + stack.peek()); // Expected: 40

        System.out.println("\nPopped element: " + stack.pop()); // Expected: 40
        System.out.println("Popped element: " + stack.pop()); // Expected: 20
        System.out.println("Popped element: " + stack.pop()); // Expected: 10

        System.out.println("Is stack empty? " + stack.isEmpty()); // Expected: true
    }
}
