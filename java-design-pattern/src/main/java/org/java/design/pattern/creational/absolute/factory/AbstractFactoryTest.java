package org.java.design.pattern.creational.absolute.factory;

// Abstract Products
interface Sofa {
    void display();
}

interface Chair {
    void display();
}

// Modern Furniture
class ModernSofa implements Sofa {
    @Override
    public void display() {
        System.out.println("Modern Sofa");
    }
}

class ModernChair implements Chair {
    @Override
    public void display() {
        System.out.println("Modern Chair");
    }
}

// Victorian Furniture
class VictorianSofa implements Sofa {
    @Override
    public void display() {
        System.out.println("Victorian Sofa");
    }
}

class VictorianChair implements Chair {
    @Override
    public void display() {
        System.out.println("Victorian Chair");
    }
}

// Abstract Factory
interface FurnitureFactory {
    Sofa createSofa();

    Chair createChair();
}

// Concrete Factories
class ModernFactory implements FurnitureFactory {
    @Override
    public Sofa createSofa() {
        return new ModernSofa();
    }

    @Override
    public Chair createChair() {
        return new ModernChair();
    }
}

class VictorianFactory implements FurnitureFactory {
    @Override
    public Sofa createSofa() {
        return new VictorianSofa();
    }

    @Override
    public Chair createChair() {
        return new VictorianChair();
    }
}

// Test Class
public class AbstractFactoryTest {
    public static void main(String[] args) {
        System.out.println("===== Abstract Factory Pattern =====\n");

        // Test 1: Modern Furniture
        System.out.println("Test 1: Modern Furniture");
        FurnitureFactory modernFactory = new ModernFactory();
        Sofa modernSofa = modernFactory.createSofa();
        Chair modernChair = modernFactory.createChair();
        modernSofa.display();
        modernChair.display();
        System.out.println();

        // Test 2: Victorian Furniture
        System.out.println("Test 2: Victorian Furniture");
        FurnitureFactory victorianFactory = new VictorianFactory();
        Sofa victorianSofa = victorianFactory.createSofa();
        Chair victorianChair = victorianFactory.createChair();
        victorianSofa.display();
        victorianChair.display();
        System.out.println();

        // Test 3: Creating Multiple Sets
        System.out.println("Test 3: Creating Furniture Sets");
        FurnitureFactory[] factories = {modernFactory, victorianFactory, modernFactory};

        for (int i = 0; i < factories.length; i++) {
            System.out.println("Set " + (i + 1) + ":");
            factories[i].createSofa().display();
            factories[i].createChair().display();
            System.out.println();
        }
    }
}