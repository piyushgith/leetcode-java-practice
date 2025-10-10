package com.java.example.common;

import java.util.List;
import java.util.Map;

public class MyUtil {

    private static List<Employee> employeesList = List.of(
            new Employee(1, "Alice", "Engineer", 25),
            new Employee(2, "Bob", "Designer", 28),
            new Employee(3, "Charlie", "Manager", 35),
            new Employee(4, "Diana", "Engineer", 30),
            new Employee(5, "Ethan", "Designer", 26),
            new Employee(6, "Fiona", "Manager", 40)
    );

    private static Map<Integer, Employee> employeesMap = Map.of(
            1, new Employee(1, "Alice", "Engineer", 25),
            2, new Employee(2, "Bob", "Designer", 28),
            3, new Employee(3, "Charlie", "Manager", 35),
            4, new Employee(4, "Diana", "Engineer", 30),
            5, new Employee(5, "Ethan", "Designer", 26),
            6, new Employee(6, "Fiona", "Manager", 40)
    );

    public static List<Employee> getEmployees() {
        return employeesList;
    }

    public static Map<Integer, Employee> getEmployeesMap() {
        return employeesMap;
    }

    private static List<Product> productsList = List.of(
            new Product(1, "Laptop", 1200, "Electronics"),
            new Product(2, "Smartphone", 800, "Electronics"),
            new Product(3, "Desk Chair", 150, "Furniture"),
            new Product(4, "Notebook", 5, "Stationery"),
            new Product(5, "Pen", 2, "Stationery"),
            new Product(6, "Headphones", 100, "Electronics")
    );

    private static Map<Integer, Product> productsMap = Map.of(
            1, new Product(1, "Laptop", 1200, "Electronics"),
            2, new Product(2, "Smartphone", 800, "Electronics"),
            3, new Product(3, "Desk Chair", 150, "Furniture"),
            4, new Product(4, "Notebook", 5, "Stationery"),
            5, new Product(5, "Pen", 2, "Stationery"),
            6, new Product(6, "Headphones", 100, "Electronics")
    );

    public static List<Product> getProducts() {
        return productsList;
    }

    public static Map<Integer, Product> getProductsMap() {
        return productsMap;
    }
}
