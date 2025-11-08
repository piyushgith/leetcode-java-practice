package com.java.example.threads.multi;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class JavaLocksExamples {

    // DEMO
    public static void main(String[] args) throws Exception {
        System.out.println("=== ReentrantLock Demo ===");
        ReentrantLockExample rle = new ReentrantLockExample();
        rle.increment();
        rle.tryLockExample();

        System.out.println("\n=== ReadWriteLock Demo ===");
        ReadWriteLockExample rwle = new ReadWriteLockExample();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(() -> rwle.readData());
        executor.submit(() -> rwle.readData());
        executor.submit(() -> rwle.writeData("Updated Data"));
        executor.submit(() -> rwle.readData());
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n=== Semaphore Demo ===");
        SemaphoreExample se = new SemaphoreExample();
        ExecutorService semExecutor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            semExecutor.submit(se::accessResource);
        }
        semExecutor.shutdown();
        semExecutor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n=== CountDownLatch Demo ===");
        new CountDownLatchExample().runTasks();

        System.out.println("\n=== Condition Demo ===");
        ConditionExample ce = new ConditionExample();
        Thread consumer = new Thread(ce::consumer);
        Thread producer = new Thread(ce::producer);
        consumer.start();
        Thread.sleep(100); // Ensure consumer waits first
        producer.start();
        consumer.join();
        producer.join();
    }

    // 1. REENTRANT LOCK - Most common explicit lock
    static class ReentrantLockExample {
        private final ReentrantLock lock = new ReentrantLock();
        private int counter = 0;

        public void increment() {
            lock.lock();
            try {
                counter++;
                System.out.println("ReentrantLock - Counter: " + counter);
            } finally {
                lock.unlock(); // Always unlock in finally block
            }
        }

        public void tryLockExample() {
            if (lock.tryLock()) { // Non-blocking attempt
                try {
                    counter += 10;
                    System.out.println("TryLock succeeded: " + counter);
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("TryLock failed - lock not available");
            }
        }
    }

    // 2. READ WRITE LOCK - Multiple readers, single writer
    static class ReadWriteLockExample {
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock readLock = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();
        private String data = "Initial Data";

        public String readData() {
            readLock.lock(); // Multiple threads can acquire read lock
            try {
                System.out.println(Thread.currentThread().getName() + " reading: " + data);
                Thread.sleep(100);
                return data;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                readLock.unlock();
            }
        }

        public void writeData(String newData) {
            writeLock.lock(); // Only one thread can acquire write lock
            try {
                System.out.println(Thread.currentThread().getName() + " writing: " + newData);
                this.data = newData;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock();
            }
        }
    }

    // 3. STAMPED LOCK - Optimistic reading (Java 8+)
    static class StampedLockExample {
        private final StampedLock sl = new StampedLock();
        private double x, y;

        public void move(double deltaX, double deltaY) {
            long stamp = sl.writeLock(); // Exclusive write lock
            try {
                x += deltaX;
                y += deltaY;
                System.out.println("Moved to: (" + x + ", " + y + ")");
            } finally {
                sl.unlockWrite(stamp);
            }
        }

        public double distanceFromOrigin() {
            long stamp = sl.tryOptimisticRead(); // Optimistic read
            double currentX = x;
            double currentY = y;

            if (!sl.validate(stamp)) { // Check if data was modified
                stamp = sl.readLock(); // Upgrade to read lock
                try {
                    currentX = x;
                    currentY = y;
                } finally {
                    sl.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
    }

    // 4. SEMAPHORE - Limit number of concurrent accesses
    static class SemaphoreExample {
        private final Semaphore semaphore = new Semaphore(3); // Max 3 permits

        public void accessResource() {
            try {
                semaphore.acquire(); // Acquire permit
                System.out.println(Thread.currentThread().getName() + " acquired resource");
                Thread.sleep(1000); // Simulate work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println(Thread.currentThread().getName() + " released resource");
                semaphore.release(); // Release permit
            }
        }
    }

    // 5. COUNTDOWN LATCH - Wait for multiple threads to complete
    static class CountDownLatchExample {
        public void runTasks() throws InterruptedException {
            int taskCount = 3;
            CountDownLatch latch = new CountDownLatch(taskCount);

            for (int i = 0; i < taskCount; i++) {
                final int taskId = i;
                new Thread(() -> {
                    try {
                        System.out.println("Task " + taskId + " starting");
                        Thread.sleep(1000);
                        System.out.println("Task " + taskId + " completed");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown(); // Decrement count
                    }
                }).start();
            }

            latch.await(); // Wait for all tasks to complete
            System.out.println("All tasks completed!");
        }
    }

    // 6. CYCLIC BARRIER - Synchronize threads at a barrier point
    static class CyclicBarrierExample {
        public void runPhases() {
            int parties = 3;
            CyclicBarrier barrier = new CyclicBarrier(parties, () -> {
                System.out.println("All threads reached barrier - proceeding");
            });

            for (int i = 0; i < parties; i++) {
                final int threadId = i;
                new Thread(() -> {
                    try {
                        System.out.println("Thread " + threadId + " working on phase 1");
                        Thread.sleep(1000);
                        barrier.await(); // Wait at barrier

                        System.out.println("Thread " + threadId + " working on phase 2");
                        Thread.sleep(1000);
                        barrier.await(); // Reusable barrier
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    // 7. SYNCHRONIZED BLOCK - Intrinsic lock (monitor)
    static class SynchronizedExample {
        private int counter = 0;
        private final Object lock = new Object();

        public void increment() {
            synchronized(lock) { // Lock on specific object
                counter++;
                System.out.println("Synchronized - Counter: " + counter);
            }
        }

        public synchronized void synchronizedMethod() {
            // Locks on 'this' object
            counter += 5;
            System.out.println("Synchronized method - Counter: " + counter);
        }
    }

    // 8. LOCK WITH CONDITION - Wait/Signal mechanism
    static class ConditionExample {
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        private boolean ready = false;

        public void producer() {
            lock.lock();
            try {
                System.out.println("Producer preparing data...");
                Thread.sleep(1000);
                ready = true;
                condition.signal(); // Wake up waiting thread
                System.out.println("Producer signaled");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }

        public void consumer() {
            lock.lock();
            try {
                while (!ready) {
                    System.out.println("Consumer waiting...");
                    condition.await(); // Wait for signal
                }
                System.out.println("Consumer received data");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }


}
