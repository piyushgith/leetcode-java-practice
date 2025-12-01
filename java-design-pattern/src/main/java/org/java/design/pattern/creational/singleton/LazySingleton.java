package org.java.design.pattern.creational.singleton;

public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {
        // Private constructor prevents instantiation
    }

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
