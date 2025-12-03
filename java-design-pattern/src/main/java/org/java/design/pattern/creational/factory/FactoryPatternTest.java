package org.java.design.pattern.creational.factory;

// Product Interface
interface Vehicle {
    void start();
    void stop();
    void drive();
    void display();
}

// Concrete Products
class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("🚗 Car engine started");
    }

    @Override
    public void stop() {
        System.out.println("🚗 Car engine stopped");
    }

    @Override
    public void drive() {
        System.out.println("🚗 Car is driving on road");
    }

    @Override
    public void display() {
        System.out.println("This is a Car");
    }
}

class Bike implements Vehicle {
    @Override
    public void start() {
        System.out.println("🏍️  Bike engine started");
    }

    @Override
    public void stop() {
        System.out.println("🏍️  Bike engine stopped");
    }

    @Override
    public void drive() {
        System.out.println("🏍️  Bike is driving on road");
    }

    @Override
    public void display() {
        System.out.println("This is a Bike");
    }
}

class Truck implements Vehicle {
    @Override
    public void start() {
        System.out.println("🚚 Truck engine started");
    }

    @Override
    public void stop() {
        System.out.println("🚚 Truck engine stopped");
    }

    @Override
    public void drive() {
        System.out.println("🚚 Truck is carrying cargo");
    }

    @Override
    public void display() {
        System.out.println("This is a Truck");
    }
}

// Simple Factory
class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        if (type == null) {
            return null;
        }

        switch (type.toLowerCase()) {
            case "car":
                return new Car();
            case "bike":
                return new Bike();
            case "truck":
                return new Truck();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}

// Test Class
public class FactoryPatternTest {
    public static void main(String[] args) {
        System.out.println("===== Factory Design Pattern =====\n");

        // Test 1: Creating Car using Factory
        System.out.println("Test 1: Creating Car");
        Vehicle car = VehicleFactory.createVehicle("car");
        car.display();
        car.start();
        car.drive();
        car.stop();
        System.out.println();

        // Test 2: Creating Bike using Factory
        System.out.println("Test 2: Creating Bike");
        Vehicle bike = VehicleFactory.createVehicle("bike");
        bike.display();
        bike.start();
        bike.drive();
        bike.stop();
        System.out.println();

        // Test 3: Creating Truck using Factory
        System.out.println("Test 3: Creating Truck");
        Vehicle truck = VehicleFactory.createVehicle("truck");
        truck.display();
        truck.start();
        truck.drive();
        truck.stop();
        System.out.println();

        // Test 4: Creating Multiple Vehicles
        System.out.println("Test 4: Creating Multiple Vehicles");
        String[] vehicleTypes = {"car", "bike", "truck", "car", "bike"};
        Vehicle[] vehicles = new Vehicle[vehicleTypes.length];

        System.out.println("Creating vehicles using Factory:");
        for (int i = 0; i < vehicleTypes.length; i++) {
            vehicles[i] = VehicleFactory.createVehicle(vehicleTypes[i]);
        }

        System.out.println("\nOperating vehicles:");
        for (Vehicle v : vehicles) {
            v.display();
            v.start();
            v.drive();
            v.stop();
            System.out.println();
        }

        // Test 5: Error Handling
        System.out.println("Test 5: Error Handling");
        try {
            Vehicle unknown = VehicleFactory.createVehicle("helicopter");
        } catch (IllegalArgumentException e) {
            System.out.println("Error caught: " + e.getMessage());
        }
    }
}
