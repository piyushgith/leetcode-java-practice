package org.java.design.pattern.solid.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Single Responsibility Principle (SRP)
@Data
@NoArgsConstructor
class Product {
    private String id;
    private String name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

@Data
@AllArgsConstructor
class OrderItem {
    private Product product;
    private int quantity;

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}

// Open/Closed & Dependency Inversion: Payment Strategy
interface PaymentProcessor {
    boolean processPayment(double amount);
}

class CreditCardPayment implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
        return true;
    }
}

class PayPalPayment implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing PayPal payment: $" + amount);
        return true;
    }
}

class CryptoCurrencyPayment implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing cryptocurrency payment: $" + amount);
        return true;
    }
}

// Single Responsibility: Order calculation
interface OrderCalculator {
    double calculateTotal(List<OrderItem> items);
}

class SimpleOrderCalculator implements OrderCalculator {
    @Override
    public double calculateTotal(List<OrderItem> items) {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }
}

// Open/Closed: Different discount strategies
interface DiscountStrategy {
    double applyDiscount(double totalPrice);
}

class NoDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double totalPrice) {
        return totalPrice;
    }
}

class PercentageDiscount implements DiscountStrategy {
    private double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double totalPrice) {
        return totalPrice - (totalPrice * percentage / 100);
    }
}

class FlatDiscount implements DiscountStrategy {
    private double amount;

    public FlatDiscount(double amount) {
        this.amount = amount;
    }

    @Override
    public double applyDiscount(double totalPrice) {
        return Math.max(0, totalPrice - amount);
    }
}

// Single Responsibility: Notification
interface NotificationService {
    void sendNotification(String message);
}

class EmailNotification implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("[EMAIL] " + message);
    }
}

class SMSNotification implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("[SMS] " + message);
    }
}

// Interface Segregation: Order operations
interface OrderProcessor {
    void processOrder(Order order);
}

interface OrderValidator {
    boolean validateOrder(Order order);
}

interface OrderRepository {
    void saveOrder(Order order);
}

@RequiredArgsConstructor
class OrderService implements OrderProcessor, OrderValidator {
    private final PaymentProcessor paymentProcessor;
    private final OrderCalculator calculator;
    private final DiscountStrategy discountStrategy;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

    @Override
    public boolean validateOrder(Order order) {
        if (order.getItems().isEmpty()) {
            notificationService.sendNotification("Order is empty");
            return false;
        }
        return true;
    }

    @Override
    public void processOrder(Order order) {
        if (!validateOrder(order)) {
            return;
        }

        double baseTotal = calculator.calculateTotal(order.getItems());
        double finalTotal = discountStrategy.applyDiscount(baseTotal);

        System.out.println("Order Total: $" + baseTotal);
        System.out.println("After Discount: $" + finalTotal);

        if (paymentProcessor.processPayment(finalTotal)) {
            order.setTotal(finalTotal);
            order.setStatus("COMPLETED");
            orderRepository.saveOrder(order);
            notificationService.sendNotification("Order processed successfully");
        } else {
            order.setStatus("FAILED");
            notificationService.sendNotification("Order processing failed");
        }
    }
}

@Data
class Order {
    private String orderId;
    private List<OrderItem> items;
    private double total;
    private String status = "PENDING";
}

class InMemoryOrderRepository implements OrderRepository {
    private List<Order> orders = new ArrayList<>();

    @Override
    public void saveOrder(Order order) {
        orders.add(order);
        System.out.println("Order saved: " + order.getOrderId());
    }
}

// Demo - Liskov Substitution Principle in Action
public class ECommerceDemo {

    // Liskov: Any PaymentProcessor implementation works here
    static void processOrderWithPayment(Order order, PaymentProcessor processor, String method) {
        System.out.println("\n======== Processing with " + method + " =================");
        double total = order.getItems().stream().mapToDouble(OrderItem::getTotalPrice).sum();
        processor.processPayment(total);
    }

    // Liskov: Any DiscountStrategy implementation works here
    static void demonstrateDiscount(double price, DiscountStrategy strategy) {
        double finalPrice = strategy.applyDiscount(price);
        System.out.println("Original: $" + price + " -> After Discount: $" + finalPrice);
    }

    // Liskov: Any NotificationService implementation works here
    static void notifyUser(NotificationService service) {
        service.sendNotification("Your order has been processed");
    }

    public static void main(String[] args) {
        // Setup products
        Product laptop = new Product("P1", "Laptop", 1000);
        Product mouse = new Product("P2", "Mouse", 25);

        // Create order
        Order order = new Order();
        order.setOrderId("ORD-001");
        order.setItems(new ArrayList<>());
        order.getItems().add(new OrderItem(laptop, 1));
        order.getItems().add(new OrderItem(mouse, 2));

        // LISKOV SUBSTITUTION: PaymentProcessor implementations can be swapped
        // All these are PaymentProcessor implementations and work interchangeably
        PaymentProcessor processor1 = new CreditCardPayment();
        PaymentProcessor processor2 = new PayPalPayment();
        PaymentProcessor processor3 = new CryptoCurrencyPayment();

        System.out.println("=====> LISKOV SUBSTITUTION: PaymentProcessor implementations can be swapped=====");
        // Any PaymentProcessor can be used - they are substitutable
        processOrderWithPayment(order, processor1, "Credit Card");
        processOrderWithPayment(order, processor2, "PayPal");
        processOrderWithPayment(order, processor3, "Crypto");

        System.out.println();

        // LISKOV SUBSTITUTION: DiscountStrategy implementations can be swapped
        DiscountStrategy discount1 = new NoDiscount();
        DiscountStrategy discount2 = new PercentageDiscount(10);
        DiscountStrategy discount3 = new FlatDiscount(50);

        System.out.println("===============DISCOUNT EXAMPLE==================\n");
        // Any DiscountStrategy works the same way
        demonstrateDiscount(1000, discount1);
        demonstrateDiscount(1000, discount2);
        demonstrateDiscount(1000, discount3);

        System.out.println("\n===============SEND NOTIFICATIONS================");

        // LISKOV SUBSTITUTION: NotificationService implementations can be swapped
        NotificationService notify1 = new EmailNotification();
        NotificationService notify2 = new SMSNotification();

        // Both work the same way
        notifyUser(notify1);
        notifyUser(notify2);
    }


}