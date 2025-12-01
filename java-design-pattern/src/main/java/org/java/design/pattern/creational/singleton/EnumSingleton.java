package org.java.design.pattern.creational.singleton;

// ===== ENUM SINGLETON (Thread-Safe & Serialization Safe) =====
public enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Enum Singleton in action");
    }
}
