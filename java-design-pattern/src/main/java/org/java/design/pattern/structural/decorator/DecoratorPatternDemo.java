package org.java.design.pattern.structural.decorator;

// Component interface
interface Coffee {
    String getDescription();

    double getCost();
}

// Concrete component - base coffee
class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Simple Coffee";
    }

    @Override
    public double getCost() {
        return 2.0;
    }
}

// Decorator abstract class - implements same interface, wraps a Coffee
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

// Concrete decorators - each adds a feature
class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.50;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.25;
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Whipped Cream";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.75;
    }
}

/**
 * Decorator Pattern Demo
 * This demo showcases the Decorator Design Pattern by allowing dynamic addition of features
 * to a Coffee object. We start with a simple coffee and then wrap it with various decorators
 * to add milk, sugar, and whipped cream.
 * Each decorator modifies the description and cost of the coffee
 * without altering the original Coffee class.
 */
// we can use the decorator pattern to add functionalities to an object dynamically.
// common example would be third party library
public class DecoratorPatternDemo {
    public static void main(String[] args) {
        // Start with simple coffee
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " - $" + coffee.getCost());

        System.out.println();

        // Add milk
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " - $" + coffee.getCost());

        // Add sugar
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " - $" + coffee.getCost());

        // Add whipped cream
        coffee = new WhippedCreamDecorator(coffee);
        System.out.println(coffee.getDescription() + " - $" + coffee.getCost());

        System.out.println();

        // Different combination - start fresh
        Coffee fancyCoffee = new SimpleCoffee();
        fancyCoffee = new WhippedCreamDecorator(new MilkDecorator(new MilkDecorator(fancyCoffee)));
        System.out.println(fancyCoffee.getDescription() + " - $" + fancyCoffee.getCost());
    }
}
