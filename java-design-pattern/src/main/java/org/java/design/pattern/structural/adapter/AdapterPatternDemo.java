package org.java.design.pattern.structural.adapter;

// Target interface - what the client expects
interface PaymentProcessor {
    void pay(double amount);
}

// Existing class - incompatible interface
class LegacyPaymentGateway {
    public void processPayment(double sum) {
        System.out.println("Processing payment of $" + sum + " via legacy gateway");
    }
}

// Adapter - makes LegacyPaymentGateway compatible with PaymentProcessor
class PaymentAdapter implements PaymentProcessor {
    private LegacyPaymentGateway legacyGateway;

    public PaymentAdapter(LegacyPaymentGateway gateway) {
        this.legacyGateway = gateway;
    }

    @Override
    public void pay(double amount) {
        // Adapt the interface - convert pay() call to processPayment()
        legacyGateway.processPayment(amount);
    }
}

// The Adapter pattern is used to make two incompatible interfaces work together,
// essentially acting as a translator or wrapper.
// Client code - only knows about PaymentProcessor
public class AdapterPatternDemo {
    public static void main(String[] args) {
        // Without adapter, this wouldn't work:
        // LegacyPaymentGateway gateway = new LegacyPaymentGateway();
        // PaymentProcessor processor = gateway; // Compilation error!

        // With adapter, seamless integration:
        LegacyPaymentGateway legacyGateway = new LegacyPaymentGateway();
        PaymentProcessor processor = new PaymentAdapter(legacyGateway);

        processor.pay(99.99); // Works perfectly!
    }
}





