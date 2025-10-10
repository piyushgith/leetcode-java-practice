package com.java.example.arrays;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        int[] numbers = {1, 22, 3, 14, 5, 20, 45, 12, 78, 99};

        int max = Arrays.stream(numbers).max().getAsInt();
        int min = Arrays.stream(numbers).min().getAsInt();

        double avg = Arrays.stream(numbers).average().getAsDouble();
        int sum = Arrays.stream(numbers).sum();
        long count = Arrays.stream(numbers).count();

        int[] sorted = Arrays.stream(numbers).sorted().toArray();
        int[] distinct = Arrays.stream(numbers).distinct().toArray();

        int[] filtered = Arrays.stream(numbers).filter(n -> n > 10).toArray();
        int[] mapped = Arrays.stream(numbers).map(n -> n * 2).toArray();
        int[] limited = Arrays.stream(numbers).limit(5).toArray();
        int[] skipped = Arrays.stream(numbers).skip(5).toArray();

        boolean anyMatch = Arrays.stream(numbers).anyMatch(n -> n > 50);
        boolean allMatch = Arrays.stream(numbers).allMatch(n -> n > 0);


        int result = binarySearch(sorted, 45);

        bfs(new int[][]{{1, 2}, {0, 3, 4}, {0, 4}, {1, 5}, {1, 2, 5}, {3, 4}}, 0);


    }

    public static int binarySearch(int[] arr, int key) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == key) {
                return mid; // Key found
            } else if (arr[mid] < key) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }

        return -1; // Key not found
    }

    public static void bfs(int[][] graph, int start) {
        boolean[] visited = new boolean[graph.length];
        java.util.Queue<Integer> queue = new java.util.LinkedList<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            System.out.print(node + "==> ");

            for (int neighbor : graph[node]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }
}