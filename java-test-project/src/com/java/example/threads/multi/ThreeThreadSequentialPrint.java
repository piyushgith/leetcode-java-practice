package com.java.example.threads.multi;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ThreeThreadSequentialPrint {

    public static void main(String[] args) throws InterruptedException {
        new UsingLockCondition().run();
        new UsingSemaphore().run();
        new UsingSynchronized().run();
        new UsingBlockingQueue().run();
        new UsingCyclicBarrier().run();
    }

    // APPROACH 1: Using ReentrantLock with Condition
    static class UsingLockCondition {
        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        private int turn = 0; // 0: letter, 1: number, 2: space
        private int counter = 1;
        private static final int MAX = 26;

        public void printLetter() {
            for (int i = 0; i < MAX; i++) {
                lock.lock();
                try {
                    while (turn != 0) {
                        condition.await();
                    }
                    System.out.print((char) ('A' + i));
                    turn = 1;
                    condition.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }

        public void printNumber() {
            for (int i = 0; i < MAX; i++) {
                lock.lock();
                try {
                    while (turn != 1) {
                        condition.await();
                    }
                    System.out.print(counter++);
                    turn = 2;
                    condition.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }

        public void printSpace() {
            for (int i = 0; i < MAX; i++) {
                lock.lock();
                try {
                    while (turn != 2) {
                        condition.await();
                    }
                    System.out.print(" ");
                    turn = 0;
                    condition.signalAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }

        public void run() throws InterruptedException {
            System.out.println("Approach 1: Using Lock & Condition");
            Thread t1 = new Thread(this::printLetter, "Letter");
            Thread t2 = new Thread(this::printNumber, "Number");
            Thread t3 = new Thread(this::printSpace, "Space");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
            System.out.println("\n");
        }
    }

    // APPROACH 2: Using Semaphore
    static class UsingSemaphore {
        private final Semaphore letterSem = new Semaphore(1);
        private final Semaphore numberSem = new Semaphore(0);
        private final Semaphore spaceSem = new Semaphore(0);
        private int counter = 1;
        private static final int MAX = 26;

        public void printLetter() {
            for (int i = 0; i < MAX; i++) {
                try {
                    letterSem.acquire();
                    System.out.print((char) ('A' + i));
                    numberSem.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void printNumber() {
            for (int i = 0; i < MAX; i++) {
                try {
                    numberSem.acquire();
                    System.out.print(counter++);
                    spaceSem.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void printSpace() {
            for (int i = 0; i < MAX; i++) {
                try {
                    spaceSem.acquire();
                    System.out.print(" ");
                    letterSem.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void run() throws InterruptedException {
            System.out.println("Approach 2: Using Semaphore");
            Thread t1 = new Thread(this::printLetter, "Letter");
            Thread t2 = new Thread(this::printNumber, "Number");
            Thread t3 = new Thread(this::printSpace, "Space");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
            System.out.println("\n");
        }
    }

    // APPROACH 3: Using synchronized with wait/notify
    static class UsingSynchronized {
        private final Object lock = new Object();
        private int turn = 0;
        private int counter = 1;
        private static final int MAX = 26;

        public void printLetter() {
            for (int i = 0; i < MAX; i++) {
                synchronized (lock) {
                    while (turn != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.print((char) ('A' + i));
                    turn = 1;
                    lock.notifyAll();
                }
            }
        }

        public void printNumber() {
            for (int i = 0; i < MAX; i++) {
                synchronized (lock) {
                    while (turn != 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.print(counter++);
                    turn = 2;
                    lock.notifyAll();
                }
            }
        }

        public void printSpace() {
            for (int i = 0; i < MAX; i++) {
                synchronized (lock) {
                    while (turn != 2) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.print(" ");
                    turn = 0;
                    lock.notifyAll();
                }
            }
        }

        public void run() throws InterruptedException {
            System.out.println("Approach 3: Using synchronized & wait/notify");
            Thread t1 = new Thread(this::printLetter, "Letter");
            Thread t2 = new Thread(this::printNumber, "Number");
            Thread t3 = new Thread(this::printSpace, "Space");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
            System.out.println("\n");
        }
    }

    // APPROACH 4: Using BlockingQueue
    static class UsingBlockingQueue {
        private final BlockingQueue<String> letterQueue = new LinkedBlockingQueue<>();
        private final BlockingQueue<String> numberQueue = new LinkedBlockingQueue<>();
        private final BlockingQueue<String> spaceQueue = new LinkedBlockingQueue<>();
        private int counter = 1;
        private static final int MAX = 26;

        public void printLetter() {
            try {
                letterQueue.put("START");
                for (int i = 0; i < MAX; i++) {
                    letterQueue.take();
                    System.out.print((char) ('A' + i));
                    numberQueue.put("GO");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void printNumber() {
            try {
                for (int i = 0; i < MAX; i++) {
                    numberQueue.take();
                    System.out.print(counter++);
                    spaceQueue.put("GO");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void printSpace() {
            try {
                for (int i = 0; i < MAX; i++) {
                    spaceQueue.take();
                    System.out.print(" ");
                    letterQueue.put("GO");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void run() throws InterruptedException {
            System.out.println("Approach 4: Using BlockingQueue");
            Thread t1 = new Thread(this::printLetter, "Letter");
            Thread t2 = new Thread(this::printNumber, "Number");
            Thread t3 = new Thread(this::printSpace, "Space");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
            System.out.println("\n");
        }
    }

    // APPROACH 5: Using CyclicBarrier (Different pattern)
    static class UsingCyclicBarrier {
        private final CyclicBarrier barrier = new CyclicBarrier(3);
        private volatile int stage = 0;
        private int counter = 1;
        private static final int MAX = 26;

        public void printLetter() {
            for (int i = 0; i < MAX; i++) {
                try {
                    while (stage != 0) {
                        Thread.yield();
                    }
                    System.out.print((char) ('A' + i));
                    stage = 1;
                    barrier.await();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void printNumber() {
            for (int i = 0; i < MAX; i++) {
                try {
                    while (stage != 1) {
                        Thread.yield();
                    }
                    System.out.print(counter++);
                    stage = 2;
                    barrier.await();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void printSpace() {
            for (int i = 0; i < MAX; i++) {
                try {
                    while (stage != 2) {
                        Thread.yield();
                    }
                    System.out.print(" ");
                    stage = 0;
                    barrier.await();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void run() throws InterruptedException {
            System.out.println("Approach 5: Using CyclicBarrier");
            Thread t1 = new Thread(this::printLetter, "Letter");
            Thread t2 = new Thread(this::printNumber, "Number");
            Thread t3 = new Thread(this::printSpace, "Space");

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
            System.out.println("\n");
        }
    }


}
