package com.java.example.collection.queue;

// File: QueueWithTwoStacks.java

import java.util.Stack;

public class QueueWithTwoStacks {
    private Stack<Integer> stack1; // Stack for enqueuing elements
    private Stack<Integer> stack2; // Stack for dequeuing elements

    // Constructor to initialize the queue
    public QueueWithTwoStacks() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    // ==========================
    // Queue Operations
    // ==========================

    /**
     * Adds an element to the rear of the queue.
     * Time Complexity: O(1)
     * @param data The integer to add.
     */
    public void enqueue(int data) {
        // Simply push the new element onto the first stack
        stack1.push(data);
        System.out.println(data + " enqueued to queue");
    }

    /**
     * Removes and returns the element at the front of the queue.
     * Time Complexity: Amortized O(1)
     * @return The integer at the front of the queue.
     * @throws java.util.NoSuchElementException if the queue is empty.
     */
    public int dequeue() {
        // If both stacks are empty, the queue is empty
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            throw new java.util.NoSuchElementException();
        }

        // If the second stack is empty, we need to transfer all elements
        // from the first stack to the second stack.
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }

        // Pop and return the top element from the second stack.
        // This element is the oldest one, i.e., the front of the queue.
        return stack2.pop();
    }

    /**
     * Looks at the element at the front of the queue without removing it.
     * Time Complexity: Amortized O(1)
     * @return The integer at the front of the queue.
     * @throws java.util.NoSuchElementException if the queue is empty.
     */
    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot peek.");
            throw new java.util.NoSuchElementException();
        }

        // The logic is the same as dequeue, but we use peek() instead of pop()
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
        }

        return stack2.peek();
    }

    /**
     * Checks if the queue is empty.
     * Time Complexity: O(1)
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        // The queue is empty only if both stacks are empty
        return stack1.isEmpty() && stack2.isEmpty();
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        QueueWithTwoStacks queue = new QueueWithTwoStacks();

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        System.out.println("\nFront element is: " + queue.peek()); // Expected: 10

        System.out.println("\nDequeued element: " + queue.dequeue()); // Expected: 10
        System.out.println("Front element is now: " + queue.peek()); // Expected: 20

        queue.enqueue(40); // Add another element

        System.out.println("\nDequeued element: " + queue.dequeue()); // Expected: 20
        System.out.println("Dequeued element: " + queue.dequeue()); // Expected: 30
        System.out.println("Dequeued element: " + queue.dequeue()); // Expected: 40

        System.out.println("Is queue empty? " + queue.isEmpty()); // Expected: true
    }
}
