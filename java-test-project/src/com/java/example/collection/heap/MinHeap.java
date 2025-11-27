package com.java.example.collection.heap;

// File: MinHeap.java

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A MinHeap implementation using a dynamic array (ArrayList).
 * Supports insertion, extraction of the minimum element, and peeking at the minimum element.
 */
public class MinHeap {
    private List<Integer> heap;

    // Constructor to initialize an empty heap
    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    // ==========================
    // Helper Methods to get parent/child indices
    // ==========================
    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < heap.size();
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < heap.size();
    }

    private int parent(int index) {
        return heap.get(getParentIndex(index));
    }

    private int leftChild(int index) {
        return heap.get(getLeftChildIndex(index));
    }

    private int rightChild(int index) {
        return heap.get(getRightChildIndex(index));
    }

    // ==========================
    // Core Heap Operations
    // ==========================

    /**
     * Swaps two elements in the heap list.
     */
    private void swap(int index1, int index2) {
        int temp = heap.get(index1);
        heap.set(index1, heap.get(index2));
        heap.set(index2, temp);
    }

    /**
     * Restores the heap property by bubbling an element up.
     * Used after an insertion.
     * Time Complexity: O(log n)
     */
    private void heapifyUp() {
        int index = heap.size() - 1; // Start with the last element

        // While the element has a parent and is smaller than its parent, swap them
        while (hasParent(index) && heap.get(index) < parent(index)) {
            swap(getParentIndex(index), index);
            index = getParentIndex(index); // Move up to the parent's index
        }
    }

    /**
     * Restores the heap property by bubbling an element down.
     * Used after an extraction.
     * Time Complexity: O(log n)
     */
    private void heapifyDown() {
        int index = 0; // Start with the root element

        // While the element has at least one left child
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);

            // If there is a right child and it's smaller than the left child
            if (hasRightChild(index) && rightChild(index) < leftChild(index)) {
                smallerChildIndex = getRightChildIndex(index);
            }

            // If the current element is smaller than its smallest child, the heap is valid
            if (heap.get(index) < heap.get(smallerChildIndex)) {
                break;
            } else {
                // Otherwise, swap with the smaller child
                swap(index, smallerChildIndex);
            }

            // Move down to the smaller child's index
            index = smallerChildIndex;
        }
    }

    // ==========================
    // Public API
    // ==========================

    /**
     * Inserts a new element into the heap.
     * Time Complexity: O(log n)
     */
    public void insert(int value) {
        heap.add(value);         // Add to the end
        heapifyUp();              // Bubble it up to its correct position
    }

    /**
     * Removes and returns the smallest element (the root) from the heap.
     * Time Complexity: O(log n)
     */
    public int extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        int min = heap.get(0);    // The root is the minimum
        int lastElement = heap.remove(heap.size() - 1); // Remove the last element

        if (!isEmpty()) {
            heap.set(0, lastElement); // Move the last element to the root
            heapifyDown();            // Bubble it down to its correct position
        }
        return min;
    }

    /**
     * Returns the smallest element without removing it.
     * Time Complexity: O(1)
     */
    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }


    // Main method to test the implementation
    public static void main(String[] args) {
        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.insert(4);
        heap.insert(15);
        heap.insert(20);
        heap.insert(0);
        heap.insert(30);

        System.out.println("Min element (peek): " + heap.peek()); // Expected: 0
        System.out.println("Heap size: " + heap.size()); // Expected: 6

        System.out.println("\nExtracting all elements (should be in sorted order):");
        while (!heap.isEmpty()) {
            System.out.print(heap.extractMin() + " "); // Expected: 0 4 10 15 20 30
        }
        System.out.println();
    }
}
