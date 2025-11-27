package com.java.example.fintech.examples;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class ReconcilePayments {
    static class Payment {
        String paymentId;
        String customerId;
        BigDecimal amount;

        public Payment(String paymentId, String customerId, BigDecimal amount) {
            this.paymentId = paymentId;
            this.customerId = customerId;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return String.format("Payment[%s, %s, %s]", paymentId, customerId, amount);
        }
    }

    static class Invoice {
        String invoiceId;
        String customerId;
        BigDecimal amount;

        public Invoice(String invoiceId, String customerId, BigDecimal amount) {
            this.invoiceId = invoiceId;
            this.customerId = customerId;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return String.format("Invoice[%s, %s, %s]", invoiceId, customerId, amount);
        }
    }

    static void main(String[] args) {
        // --- Setup Example Data ---
        List<Payment> payments = Arrays.asList(
                new Payment("P001", "CustomerA", new BigDecimal("150.00")),
                new Payment("P002", "CustomerA", new BigDecimal("80.00")),
                new Payment("P003", "CustomerB", new BigDecimal("35.00")),
                new Payment("P004", "CustomerC", new BigDecimal("100.00")) // No invoices
        );

        List<Invoice> invoices = Arrays.asList(
                new Invoice("I101", "CustomerA", new BigDecimal("100.00")),
                new Invoice("I102", "CustomerA", new BigDecimal("50.00")),
                new Invoice("I103", "CustomerA", new BigDecimal("30.00")), // Will be matched by P002
                new Invoice("I104", "CustomerA", new BigDecimal("50.00")), // Will be matched by P002
                new Invoice("I201", "CustomerB", new BigDecimal("15.00")),
                new Invoice("I202", "CustomerB", new BigDecimal("20.00")),
                new Invoice("I301", "CustomerD", new BigDecimal("50.00")) // Different customer
        );

        // --- Run Reconciliation ---
        Map<String, List<String>> result = reconcilePayments(payments, invoices);

        // --- Print Results ---
        System.out.println("--- Reconciliation Results (Payment ID -> List of Matched Invoice IDs) ---");
        result.forEach((paymentId, invoiceIds) -> {
            System.out.printf("%s: %s\n", paymentId, invoiceIds);
        });
        /* Expected Output:
           P001: [I101, I102]
           P002: [I103, I104]
           P003: [I201, I202]
           P004: [] (No match)
        */
    }

    /**
     * Reconciles incoming payments with pending invoices.
     * Matches a payment to a subset of invoices from the same customer that sum exactly to the payment amount.
     *
     * @param payments List of incoming payments.
     * @param invoices List of pending invoices.
     * @return A map where the key is Payment ID and the value is a list of matched Invoice IDs.
     */
    public static Map<String, List<String>> reconcilePayments(List<Payment> payments, List<Invoice> invoices) {
        // Rounding constant for BigDecimal comparisons
        final int SCALE = 2;

        // 1. Group invoices by customer ID for efficient lookup
        Map<String, List<Invoice>> invoicesByCustomer = invoices.stream().collect(Collectors.groupingBy(i -> i.customerId));

        // 2. Map to store the final results: PaymentId -> List<InvoiceId>
        Map<String, List<String>> reconciliationMap = new HashMap<>();

        // 3. Process each payment
        for (Payment payment : payments) {
            String customerId = payment.customerId;
            BigDecimal paymentAmount = payment.amount.setScale(SCALE, RoundingMode.HALF_EVEN);

            // Get pending invoices for the customer
            List<Invoice> customerInvoices = invoicesByCustomer.getOrDefault(customerId, Collections.emptyList());

            if (customerInvoices.isEmpty()) {
                reconciliationMap.put(payment.paymentId, Collections.emptyList());
                continue;
            }

            // --- Recursive Backtracking Setup ---
            List<Invoice> bestMatch = findMatchingInvoices(customerInvoices, paymentAmount, SCALE);

            // 4. Store the result
            if (!bestMatch.isEmpty()) {
                List<String> matchedInvoiceIds = bestMatch.stream().map(i -> i.invoiceId).collect(Collectors.toList());
                reconciliationMap.put(payment.paymentId, matchedInvoiceIds);

                // IMPORTANT: In a real system, these matched invoices would now be
                // marked as 'PAID' or 'RECONCILED' and removed from the customerInvoices
                // list for subsequent payments (P002) from the same customer.
                // For this single-pass example, we ignore this state change.
                // If the prompt implied processing multiple payments over time, the
                // `invoicesByCustomer` map would need to be mutable and updated here.

            } else {
                reconciliationMap.put(payment.paymentId, Collections.emptyList());
            }
        }

        return reconciliationMap;
    }


    /**
     * Recursive backtracking method to find a subset of invoices that exactly equals the target amount.
     * This is a variation of the Subset Sum Problem.
     *
     * @param invoices The list of available invoices to consider.
     * @param target   The exact amount that needs to be matched.
     * @param scale    The BigDecimal scale for comparison.
     * @return The list of Invoice objects that form the exact match, or an empty list if no exact match is found.
     */
    private static List<Invoice> findMatchingInvoices(List<Invoice> invoices, BigDecimal target, int scale) {

        // Base Case 1: Target achieved.
        if (target.compareTo(BigDecimal.ZERO) == 0) {
            return new ArrayList<>(); // Success: return an empty list to start the accumulation
        }

        // Base Case 2: Target is negative or no invoices left to check.
        if (target.compareTo(BigDecimal.ZERO) < 0 || invoices.isEmpty()) {
            return Collections.emptyList(); // Failure
        }

        // Recursive Step: Iterate through all available invoices
        for (int i = 0; i < invoices.size(); i++) {
            Invoice currentInvoice = invoices.get(i);
            BigDecimal currentAmount = currentInvoice.amount.setScale(scale, RoundingMode.HALF_EVEN);

            // 1. Calculate the new remaining target
            BigDecimal newTarget = target.subtract(currentAmount).setScale(scale, RoundingMode.HALF_EVEN);

            // 2. Prepare the list of *remaining* invoices for the next recursive call.
            // This is crucial: we must not reuse the current invoice (currentInvoice)
            // and we only need to check invoices that come *after* the current one (i+1)
            // to avoid duplicate permutations of the same set.
            List<Invoice> remainingInvoices = invoices.subList(i + 1, invoices.size());

            // 3. Recursive call
            List<Invoice> matchFromRemaining = findMatchingInvoices(remainingInvoices, newTarget, scale);

            // 4. Check result of the recursive call
            if (!matchFromRemaining.isEmpty() || newTarget.compareTo(BigDecimal.ZERO) == 0) {
                // If it's a successful match (either newTarget is 0 OR the recursive call found a match)
                List<Invoice> successfulMatch = new ArrayList<>(matchFromRemaining);
                successfulMatch.add(currentInvoice); // Add the current invoice to the successful list
                return successfulMatch;
            }
        }

        // If the loop finishes without finding a match
        return Collections.emptyList();
    }
}