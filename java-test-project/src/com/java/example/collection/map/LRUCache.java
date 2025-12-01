package com.java.example.collection.map;

import java.util.HashMap;
import java.util.Map;


// LRU Cache - Interview Standard Implementation
// Time: O(1) for get and put
// Space: O(capacity)

class LRUCache {

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));    // returns 1

        cache.put(3, 3);                      // evicts key 2
        System.out.println(cache.get(2));    // returns -1 (not found)

        cache.put(4, 4);                      // evicts key 1
        System.out.println(cache.get(1));    // returns -1 (not found)
        System.out.println(cache.get(3));    // returns 3
        System.out.println(cache.get(4));    // returns 4
    }

    class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Map<Integer, Node> cache;
    private int capacity;
    private Node head; // dummy head (most recent)
    private Node tail; // dummy tail (least recent)

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();

        // Initialize dummy nodes
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }

        Node node = cache.get(key);
        // Move to front (most recently used)
        remove(node);
        addToFront(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // Update existing node
            Node node = cache.get(key);
            node.value = value;
            remove(node);
            addToFront(node);
        } else {
            // Add new node
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToFront(newNode);

            // Check capacity
            if (cache.size() > capacity) {
                // Remove least recently used (before tail)
                Node lru = tail.prev;
                remove(lru);
                cache.remove(lru.key);
            }
        }
    }

    // Remove node from doubly linked list
    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Add node right after head (most recent position)
    private void addToFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
