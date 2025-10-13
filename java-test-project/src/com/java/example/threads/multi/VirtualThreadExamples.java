package com.java.example.threads.multi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * Virtual Thread Examples - Avoiding Pinning Issues
 * These examples demonstrate proper usage of virtual threads without causing pinning
 */
public class VirtualThreadExamples {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Example 1: HTTP Requests ===");
        asyncHttpRequests();

        System.out.println("\n=== Example 2: Database Operations ===");
        asyncDatabaseOperations();

        System.out.println("\n=== Example 3: Structured Concurrency ===");
        structuredConcurrency();

        System.out.println("\n=== Example 4: ReentrantLock ===");
        reentrantLockExample();

        System.out.println("\n=== Example 5: Producer-Consumer ===");
        producerConsumerExample();

        System.out.println("\n=== Example 6: Rate Limiting ===");
        rateLimitingExample();

        System.out.println("\n=== Example 7: File Operations ===");
        asyncFileOperations();

        System.out.println("\n=== Example 8: Async Chaining ===");
        asyncChaining();
    }

    // Example 1: HTTP Client with Virtual Threads (No Pinning)
    public static void asyncHttpRequests() throws Exception {
        // Use HttpClient which is async-friendly and doesn't cause pinning
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<String> urls = List.of(
                    "https://jsonplaceholder.typicode.com/posts/1",
                    "https://jsonplaceholder.typicode.com/posts/2",
                    "https://jsonplaceholder.typicode.com/posts/3"
            );

            List<CompletableFuture<String>> futures = urls.stream()
                    .map(url -> CompletableFuture.supplyAsync(() -> {
                        try {
                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(URI.create(url))
                                    .GET()
                                    .build();

                            HttpResponse<String> response = client.send(request,
                                    HttpResponse.BodyHandlers.ofString());
                            return "Response from " + url + ": " + response.statusCode();
                        } catch (Exception e) {
                            return "Error: " + e.getMessage();
                        }
                    }, executor))
                    .toList();

            // Wait for all requests to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            futures.forEach(f -> System.out.println(f.join()));
        }
    }

    // Example 2: Database-like Operations with Virtual Threads
    public static void asyncDatabaseOperations() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Simulate multiple database queries
            List<CompletableFuture<String>> results = IntStream.range(1, 11)
                    .mapToObj(id -> CompletableFuture.supplyAsync(() -> {
                        // Simulate DB query with sleep (virtual thread friendly)
                        try {
                            Thread.sleep(Duration.ofMillis(100 + (id * 10)));
                            return "Query result for ID: " + id;
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return "Interrupted: " + id;
                        }
                    }, executor))
                    .toList();

            System.out.println("All queries submitted, waiting for results...");
            results.forEach(f -> System.out.println(f.join()));
        }
    }

    // Example 3: Parallel Processing with StructuredTaskScope (Java 21+)
    public static void structuredConcurrency() throws Exception {
        record UserData(int id, String name) {}
        record OrderData(int userId, double total) {}

        // Helper method to sleep safely
        Runnable sleepTask = () -> {
            try {
                Thread.sleep(1000); // Example
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        };

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<UserData> userTask = scope.fork(() -> {
                sleepTask.run();
                return new UserData(1, "John Doe");
            });

            Subtask<OrderData> orderTask = scope.fork(() -> {
                sleepTask.run();
                return new OrderData(1, 299.99);
            });

            Subtask<String> notificationTask = scope.fork(() -> {
                sleepTask.run();
                return "Notification sent";
            });

            scope.join();
            scope.throwIfFailed();

            System.out.println("User: " + userTask.get());
            System.out.println("Order: " + orderTask.get());
            System.out.println("Notification: " + notificationTask.get());
        }
    }

    // Example 4: ReentrantLock Instead of Synchronized (Avoids Pinning)
    public static void reentrantLockExample() throws Exception {
        class Counter {
            private int count = 0;
            private final ReentrantLock lock = new ReentrantLock();

            public void increment() {
                lock.lock(); // Virtual thread friendly - doesn't cause pinning
                try {
                    count++;
                    Thread.sleep(1); // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }

            public int getCount() {
                return count;
            }
        }

        Counter counter = new Counter();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = IntStream.range(0, 1000)
                    .mapToObj(i -> CompletableFuture.runAsync(counter::increment, executor))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            System.out.println("Final count: " + counter.getCount());
        }
    }

    // Example 5: Producer-Consumer with BlockingQueue
    public static void producerConsumerExample() throws Exception {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
        CountDownLatch latch = new CountDownLatch(1);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Producer
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 20; i++) {
                        queue.put(i); // BlockingQueue.put() doesn't cause pinning
                        System.out.println("Produced: " + i);
                        Thread.sleep(50);
                    }
                    queue.put(-1); // Poison pill
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            // Consumer
            executor.submit(() -> {
                try {
                    while (true) {
                        Integer item = queue.take(); // BlockingQueue.take() doesn't cause pinning
                        if (item == -1) break;
                        System.out.println("Consumed: " + item);
                        Thread.sleep(30);
                    }
                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            latch.await();
        }
    }

    // Example 6: Semaphore for Rate Limiting (No Pinning)
    public static void rateLimitingExample() throws Exception {
        Semaphore rateLimiter = new Semaphore(3); // Max 3 concurrent operations

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> futures = IntStream.range(1, 11)
                    .mapToObj(id -> CompletableFuture.supplyAsync(() -> {
                        try {
                            rateLimiter.acquire(); // Semaphore doesn't cause pinning
                            try {
                                Thread.sleep(Duration.ofMillis(500));
                                return "Task " + id + " completed";
                            } finally {
                                rateLimiter.release();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return "Task " + id + " interrupted";
                        }
                    }, executor))
                    .toList();

            futures.forEach(f -> System.out.println(f.join()));
        }
    }

    // Example 7: Async File I/O with Virtual Threads
    public static void asyncFileOperations() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<String>> operations = List.of(
                    CompletableFuture.supplyAsync(() -> {
                        // Simulate file read
                        try {
                            Thread.sleep(100);
                            return "File1.txt content read";
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return "Read interrupted";
                        }
                    }, executor),

                    CompletableFuture.supplyAsync(() -> {
                        // Simulate file write
                        try {
                            Thread.sleep(150);
                            return "File2.txt written successfully";
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return "Write interrupted";
                        }
                    }, executor)
            );

            operations.forEach(f -> System.out.println(f.join()));
        }
    }

    // Example 8: CompletableFuture Chaining with Virtual Threads
    public static void asyncChaining() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<String> result = CompletableFuture
                    .supplyAsync(() -> {
                        sleep(100);
                        return "Step 1: Fetch user";
                    }, executor)
                    .thenApplyAsync(user -> {
                        sleep(100);
                        return user + " -> Step 2: Fetch orders";
                    }, executor)
                    .thenApplyAsync(orders -> {
                        sleep(100);
                        return orders + " -> Step 3: Calculate total";
                    }, executor)
                    .thenApplyAsync(total -> {
                        sleep(100);
                        return total + " -> Step 4: Send notification";
                    }, executor);

            System.out.println(result.join());
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
