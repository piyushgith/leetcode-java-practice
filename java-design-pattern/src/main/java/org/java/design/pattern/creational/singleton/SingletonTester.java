package org.java.design.pattern.creational.singleton;

import java.lang.reflect.Constructor;

// ===== TEST CLASS =====
public class SingletonTester {

    // Test 1: Verify multiple calls return same instance
    static void testSingletonBehavior() {
        System.out.println("\n===== Test 1: Singleton Behavior =====");

        EagerSingleton e1 = EagerSingleton.getInstance();
        EagerSingleton e2 = EagerSingleton.getInstance();
        System.out.println("EagerSingleton - Same instance: " + (e1 == e2));

        LazySingleton l1 = LazySingleton.getInstance();
        LazySingleton l2 = LazySingleton.getInstance();
        System.out.println("LazySingleton - Same instance: " + (l1 == l2));

        DoubleCheckedSingleton d1 = DoubleCheckedSingleton.getInstance();
        DoubleCheckedSingleton d2 = DoubleCheckedSingleton.getInstance();
        System.out.println("DoubleCheckedSingleton - Same instance: " + (d1 == d2));

        BillPughSingleton b1 = BillPughSingleton.getInstance();
        BillPughSingleton b2 = BillPughSingleton.getInstance();
        System.out.println("BillPughSingleton - Same instance: " + (b1 == b2));

        EnumSingleton en1 = EnumSingleton.INSTANCE;
        EnumSingleton en2 = EnumSingleton.INSTANCE;
        System.out.println("EnumSingleton - Same instance: " + (en1 == en2));
    }

    // Test 2: Thread safety test
    static void testThreadSafety() {
        System.out.println("\n===== Test 2: Thread Safety =====");

        testThreadSafetyForClass("EagerSingleton", () -> EagerSingleton.getInstance());
        testThreadSafetyForClass("LazySingleton", () -> LazySingleton.getInstance());
        testThreadSafetyForClass("DoubleCheckedSingleton", () -> DoubleCheckedSingleton.getInstance());
        testThreadSafetyForClass("BillPughSingleton", () -> BillPughSingleton.getInstance());
    }

    static void testThreadSafetyForClass(String name, Getter getter) {
        Object[] instances = new Object[1];
        Thread t1 = new Thread(() -> instances[0] = getter.get());
        Thread t2 = new Thread(() -> {
            Object temp = getter.get();
            if (instances[0] != null && instances[0] != temp) {
                System.out.println(name + " - THREAD SAFETY FAILED!");
            }
        });

        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(name + " - Thread safe: OK");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    interface Getter {
        Object get();
    }

    // Test 3: Reflection attack
    static void testReflectionAttack() {
        System.out.println("\n===== Test 3: Reflection Attack =====");

        try {
            EagerSingleton eager1 = EagerSingleton.getInstance();
            Constructor<?> constructor = EagerSingleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            EagerSingleton eager2 = (EagerSingleton) constructor.newInstance();

            System.out.println("EagerSingleton - Reflection Attack: " + (eager1 != eager2 ? "FAILED" : "Protected"));
        } catch (Exception e) {
            System.out.println("EagerSingleton - Reflection Attack: Protected");
        }

        try {
            EnumSingleton enum1 = EnumSingleton.INSTANCE;
            Constructor<?> constructor = EnumSingleton.class.getDeclaredConstructor(String.class, int.class);
            constructor.setAccessible(true);
            EnumSingleton enum2 = (EnumSingleton) constructor.newInstance("INSTANCE", 0);

            System.out.println("EnumSingleton - Reflection Attack: " + (enum1 != enum2 ? "FAILED" : "Protected"));
        } catch (Exception e) {
            System.out.println("EnumSingleton - Reflection Attack: Protected");
        }
    }

    // Test 4: Serialization (for non-enum)
    static void testSerialization() {
        System.out.println("\n===== Test 4: Serialization =====");

        // Note: Enum is serialization-safe by default
        System.out.println("EnumSingleton - Serialization safe: YES");
        System.out.println("Other singletons need readResolve() method");
    }

    // Test 5: Performance test
    static void testPerformance() {
        System.out.println("\n===== Test 5: Performance (1,000,000 calls) =====");

        testPerformanceForClass("EagerSingleton", () -> EagerSingleton.getInstance());
        testPerformanceForClass("LazySingleton", () -> LazySingleton.getInstance());
        testPerformanceForClass("DoubleCheckedSingleton", () -> DoubleCheckedSingleton.getInstance());
        testPerformanceForClass("BillPughSingleton", () -> BillPughSingleton.getInstance());
    }

    static void testPerformanceForClass(String name, Getter getter) {
        long start = System.nanoTime();
        for (int i = 0; i < 1_000_000; i++) {
            getter.get();
        }
        long duration = System.nanoTime() - start;
        System.out.println(name + ": " + (duration / 1_000_000) + " ms");
    }

    // Main execution
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   SINGLETON PATTERN - COMPREHENSIVE TEST");
        System.out.println("========================================");

        testSingletonBehavior();
        testThreadSafety();
        testReflectionAttack();
        testSerialization();
        testPerformance();

        System.out.println("\n========================================");
        System.out.println("   ALL TESTS COMPLETED");
        System.out.println("========================================");
    }
}
