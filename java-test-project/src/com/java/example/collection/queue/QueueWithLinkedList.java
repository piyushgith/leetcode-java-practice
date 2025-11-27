package com.java.example.collection.queue;

// File: QueueWithLinkedList.java

public class QueueWithLinkedList {
    class Node {
        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
        }
    }

    private Node front; // Points to the front of the queue (for dequeue)
    private Node rear;  // Points to the rear of the queue (for enqueue)

    // Constructor to initialize an empty queue
    public QueueWithLinkedList() {
        front = null;
        rear = null;
    }

    // ==========================
    // Queue Operations
    // ==========================

    /**
     * Adds an element to the rear of the queue.
     * Time Complexity: O(1)
     *
     * @param data The integer to add to the queue.
     */
    public void enqueue(int data) {
        // Create a new node
        Node newNode = new Node(data);

        // If the queue is empty, the new node is both front and rear
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            // Add the new node at the end of the list and update rear
            rear.next = newNode;
            rear = newNode;
        }
        System.out.println(data + " enqueued to queue");
    }

    /**
     * Removes and returns the element at the front of the queue.
     * Time Complexity: O(1)
     *
     * @return The integer at the front of the queue.
     * @throws java.util.NoSuchElementException if the queue is empty.
     */
    public int dequeue() {
        // If the queue is empty, throw an exception
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            throw new java.util.NoSuchElementException();
        }

        // Store the data from the front node
        int dequeuedData = front.data;

        // Move the front pointer to the next node
        front = front.next;

        // If front becomes null, it means the queue is now empty, so rear must also be null
        if (front == null) {
            rear = null;
        }

        return dequeuedData;
    }

    /**
     * Looks at the element at the front of the queue without removing it.
     * Time Complexity: O(1)
     *
     * @return The integer at the front of the queue.
     * @throws java.util.NoSuchElementException if the queue is empty.
     */
    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot peek.");
            throw new java.util.NoSuchElementException();
        }
        return front.data;
    }

    /**
     * Checks if the queue is empty.
     * Time Complexity: O(1)
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return front == null;
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        QueueWithLinkedList queue = new QueueWithLinkedList();

        System.out.println("Is queue empty? " + queue.isEmpty()); // Expected: true

        // Enqueue elements
        queue.enqueue(100);
        queue.enqueue(200);
        queue.enqueue(300);

        System.out.println("\nFront element is: " + queue.peek()); // Expected: 100
        System.out.println("Is queue empty? " + queue.isEmpty()); // Expected: false

        // Dequeue elements
        System.out.println("\nDequeued element: " + queue.dequeue()); // Expected: 100
        System.out.println("Front element is now: " + queue.peek()); // Expected: 200

        System.out.println("\nDequeued element: " + queue.dequeue()); // Expected: 200
        System.out.println("Dequeued element: " + queue.dequeue()); // Expected: 300

        System.out.println("Is queue empty? " + queue.isEmpty()); // Expected: true

        // Try to dequeue from an empty queue
        try {
            System.out.println("\nTrying to dequeue from an empty queue...");
            queue.dequeue();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Caught expected exception.");
        }
    }
}
