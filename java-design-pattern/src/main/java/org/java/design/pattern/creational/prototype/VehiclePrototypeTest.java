package org.java.design.pattern.creational.prototype;

public class VehiclePrototypeTest {

    public static void main(String[] args) {
        System.out.println("===== Prototype Pattern - Vehicle Example =====\n");

        // Test 1: Car Cloning
        System.out.println("Test 1: Car Cloning");
        Car originalCar = new Car("Toyota", "Camry", "Silver");
        Car clonedCar = originalCar.clone();
        clonedCar.setColor("Blue");

        System.out.println("Original: ");
        originalCar.display();
        System.out.println("Cloned: ");
        clonedCar.display();
        System.out.println("Same object? " + (originalCar == clonedCar) + "\n");

        // Test 2: Bike Cloning
        System.out.println("Test 2: Bike Cloning");
        Bike originalBike = new Bike("Harley", "Street 750", 750);
        Bike clonedBike = originalBike.clone();
        clonedBike.setEngineCC(1200);

        System.out.println("Original: ");
        originalBike.display();
        System.out.println("Cloned: ");
        clonedBike.display();
        System.out.println("Same object? " + (originalBike == clonedBike) + "\n");

        // Test 3: Multiple Clones
        System.out.println("Test 3: Creating Fleet from Template");
        Car template = new Car("Honda", "Civic", "White");
        System.out.println("Template: ");
        template.display();

        for (int i = 1; i <= 3; i++) {
            Car fleetCar = template.clone();
            fleetCar.setColor(i == 1 ? "Red" : i == 2 ? "Blue" : "Green");
            System.out.println("Fleet Car " + i + ": ");
            fleetCar.display();
        }
    }
}
