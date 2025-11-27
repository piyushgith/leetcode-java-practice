package com.java.example.collection.heap;

// We are using the MinHeap class from the previous example.
// Make sure MinHeap.java is in the same directory or package.
import java.util.NoSuchElementException;

public class MyPriorityQueue {
    private MinHeap heap;

    // Constructor: The PriorityQueue is just an interface to our MinHeap
    public MyPriorityQueue() {
        this.heap = new MinHeap();
    }

    // ==========================
    // PriorityQueue Public API
    // ==========================

    /**
     * Inserts an element into the priority queue.
     * Time Complexity: O(log n)
     * @param value The value to insert.
     */
    public void add(int value) {
        heap.insert(value);
    }

    /**
     * Removes and returns the element with the highest priority (the minimum value).
     * Time Complexity: O(log n)
     * @return The minimum value in the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public int poll() {
        return heap.extractMin();
    }

    /**
     * Retrieves, but does not remove, the element with the highest priority.
     * Time Complexity: O(1)
     * @return The minimum value in the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public int peek() {
        return heap.peek();
    }

    /**
     * Checks if the priority queue is empty.
     * Time Complexity: O(1)
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }


    // Main method to test the PriorityQueue implementation
    public static void main(String[] args) {
        MyPriorityQueue pq = new MyPriorityQueue();

        System.out.println("Adding elements: 5, 1, 10, 3, 2");
        pq.add(5);
        pq.add(1);
        pq.add(10);
        pq.add(3);
        pq.add(2);

        System.out.println("Priority queue size: " + pq.size()); // Expected: 5
        System.out.println("Highest priority element (peek): " + pq.peek()); // Expected: 1

        System.out.println("\nRemoving elements one by one (polling):");
        while (!pq.isEmpty()) {
            // The elements should come out in sorted order (1, 2, 3, 5, 10)
            System.out.print(pq.poll() + " ");
        }
        System.out.println("\nIs queue empty? " + pq.isEmpty()); // Expected: true
    }
}
