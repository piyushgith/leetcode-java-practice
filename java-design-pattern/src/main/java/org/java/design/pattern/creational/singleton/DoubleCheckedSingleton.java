package org.java.design.pattern.creational.singleton;


// GOOD FOR INTERVIEW DISCUSSIONS
// ===== DOUBLE-CHECKED LOCKING (Optimized) =====
public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {
        // Private constructor to prevent instantiation
    }

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {  // First check (no lock)
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {  // Second check (with lock)
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}
