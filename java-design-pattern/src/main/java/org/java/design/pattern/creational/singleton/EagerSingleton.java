package org.java.design.pattern.creational.singleton;

// ===== 1. EAGER INITIALIZATION (Simple) =====
public class EagerSingleton {
    // Instance created when class loads
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
        // Private constructor prevents instantiation
    }

    public static EagerSingleton getInstance() {
        return instance;
    }
}
