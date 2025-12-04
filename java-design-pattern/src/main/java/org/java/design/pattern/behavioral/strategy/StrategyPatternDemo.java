package org.java.design.pattern.behavioral.strategy;

// Strategy interface - defines family of algorithms
interface PaymentStrategy {
    void pay(double amount);
}

// Concrete strategies - different payment algorithms
class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cvv;

    public CreditCardStrategy(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
        System.out.println("Card: " + cardNumber.substring(cardNumber.length() - 4));
        System.out.println("Payment successful!");
    }
}

class PayPalStrategy implements PaymentStrategy {
    private String email;

    public PayPalStrategy(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Processing PayPal payment of $" + amount);
        System.out.println("Account: " + email);
        System.out.println("Payment successful!");
    }
}

class BitcoinStrategy implements PaymentStrategy {
    private String walletAddress;

    public BitcoinStrategy(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Processing Bitcoin payment of $" + amount);
        System.out.println("Wallet: " + walletAddress);
        System.out.println("Payment successful!");
    }
}

// Context - uses a strategy
class ShoppingCart {
    private double total;
    private PaymentStrategy paymentStrategy;

    public ShoppingCart(double total) {
        this.total = total;
    }

    // Set strategy at runtime
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout() {
        if (paymentStrategy == null) {
            System.out.println("Please select a payment method!");
            return;
        }
        paymentStrategy.pay(total);
    }
}

/**
 * * Strategy Pattern Demo
 * * This demo shows how to use the Strategy Pattern to select different payment methods
 * * at runtime in a shopping cart scenario.
 */
public class StrategyPatternDemo {
    public static void main(String[] args) {
        // Create shopping cart
        ShoppingCart cart = new ShoppingCart(99.99);

        // Pay with credit card
        System.out.println("=== Payment with Credit Card ===");
        cart.setPaymentStrategy(new CreditCardStrategy("1234567890123456", "123"));
        cart.checkout();

        System.out.println();

        // Same cart, different payment method
        System.out.println("=== Payment with PayPal ===");
        cart.setPaymentStrategy(new PayPalStrategy("user@example.com"));
        cart.checkout();

        System.out.println();

        // Same cart, yet another payment method
        System.out.println("=== Payment with Bitcoin ===");
        cart.setPaymentStrategy(new BitcoinStrategy("1A1z7agoat"));
        cart.checkout();

        System.out.println();

        // Can easily switch back
        System.out.println("=== Back to Credit Card ===");
        cart.setPaymentStrategy(new CreditCardStrategy("9876543210987654", "456"));
        cart.checkout();
    }
}