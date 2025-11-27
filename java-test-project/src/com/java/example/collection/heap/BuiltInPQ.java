package com.java.example.collection.heap;

import java.util.PriorityQueue;

public class BuiltInPQ {

    public static void main(String[] args) {
        // It's a Min-Queue by default
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(1);
        pq.add(10);

        // poll() will return 1, then 5, then 10
        System.out.println(pq.poll());
        System.out.println(pq.poll());
        System.out.println(pq.poll());

        // To make it a Max-Queue, you provide a custom comparator
        PriorityQueue<Integer> maxPq = new PriorityQueue<>((a, b) -> b - a);
        maxPq.add(5);
        maxPq.add(1);
        maxPq.add(10);

        // poll() will now return 10, then 5, then 1
        System.out.println("\nMax-PriorityQueue:");
        System.out.println(maxPq.poll());
        System.out.println(maxPq.poll());
        System.out.println(maxPq.poll());
    }
}
