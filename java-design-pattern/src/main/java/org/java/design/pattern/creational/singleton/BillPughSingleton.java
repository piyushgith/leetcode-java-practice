package org.java.design.pattern.creational.singleton;


/**
 * Bill Pugh Singleton Implementation
 * This implementation uses a static inner helper class to hold the singleton instance.
 * The instance is created only when the getInstance() method is called for the first time.
 * This approach is thread-safe without requiring synchronized blocks and is efficient.
 */
// ===== BILL PUGH SINGLETON (Best Practice) =====
public class BillPughSingleton {
    private BillPughSingleton() {
    }

    // Static inner class loaded only when getInstance() is called
    private static class SingletonHelper {
        private static final BillPughSingleton instance = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.instance;
    }
}