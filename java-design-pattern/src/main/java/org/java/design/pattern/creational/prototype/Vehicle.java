package org.java.design.pattern.creational.prototype;

interface Vehicle extends Cloneable {
    Vehicle clone();
    void display();
}

// Concrete Prototype: Car
class Car implements Vehicle {
    private String brand;
    private String model;
    private String color;

    public Car(String brand, String model, String color) {
        this.brand = brand;
        this.model = model;
        this.color = color;
    }

    // Copy constructor for cloning
    private Car(Car original) {
        this.brand = original.brand;
        this.model = original.model;
        this.color = original.color;
    }

    @Override
    public Car clone() {
        return new Car(this);
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void display() {
        System.out.println("Car: " + brand + " " + model + " (" + color + ")");
    }
}

// Concrete Prototype: Bike
class Bike implements Vehicle {
    private String brand;
    private String model;
    private int engineCC;

    public Bike(String brand, String model, int engineCC) {
        this.brand = brand;
        this.model = model;
        this.engineCC = engineCC;
    }

    private Bike(Bike original) {
        this.brand = original.brand;
        this.model = original.model;
        this.engineCC = original.engineCC;
    }

    @Override
    public Bike clone() {
        return new Bike(this);
    }

    public void setEngineCC(int engineCC) {
        this.engineCC = engineCC;
    }

    @Override
    public void display() {
        System.out.println("Bike: " + brand + " " + model + " (" + engineCC + "cc)");
    }
}
