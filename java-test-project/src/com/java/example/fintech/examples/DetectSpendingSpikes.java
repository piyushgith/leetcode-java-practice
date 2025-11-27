package com.java.example.fintech.examples;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Example demonstrating detection of spending spikes in transaction data.
 * Alert when any account's spending in a 1-hour window exceeds 5x their average hourly spending from the past 7 days.
 */
public class DetectSpendingSpikes {
    static class Transaction {
        String accountId;
        BigDecimal amount;
        LocalDateTime timestamp;

        public Transaction(String accountId, BigDecimal amount, LocalDateTime timestamp) {
            this.accountId = accountId;
            this.amount = amount;
            this.timestamp = timestamp;
        }
    }
    private static List<Transaction> allHistoricalTransactions = null;

    static void main(String[] args) {
        // We need an instance of DetectSpendingSpikes to call the non-static getAverageHourlySpending
        DetectSpendingSpikes detector = new DetectSpendingSpikes();

        allHistoricalTransactions = getHistoricalTransactions();
        List<Transaction> newTransactions = getNewTransactions();

        // --- D. Run the Detection ---
        System.out.println("--- Starting Spike Detection ---");
        // Pass the detector instance to the static method if needed, or make getAverageHourlySpending static.
        // For simplicity, I'll assume we can call the static detectSpendingSpikes directly.
        List<String> alertedAccounts = detectSpendingSpikes(newTransactions, allHistoricalTransactions, detector);
        System.out.println("--- Detection Complete ---");
        alertedAccounts.forEach(accountId ->
                System.out.println("ALERT: Spending spike detected for Account ID: " + accountId));

        // Expected output based on comments in getNewTransactions:
        // ALERT: Spending spike detected for Account ID: AccountB (Triggered by T2)
        // ALERT: Spending spike detected for Account ID: AccountA (Triggered by T4)
    }


    /**
     * Calculates the average hourly spending for an account based on a list of transactions.
     * Assumes the transactions cover a 7-day period.
     */
    public BigDecimal getAverageHourlySpending(List<Transaction> transactions) {
        // Total spending over the 7-day period
        BigDecimal totalSpending = BigDecimal.ZERO;
        // The number of full hours (7 * 24 = 168)
        long totalHours = 7 * 24;

        for (Transaction t : transactions) {
            totalSpending = totalSpending.add(t.amount);
        }

        if (totalHours > 0 && totalSpending.compareTo(BigDecimal.ZERO) > 0) {
            // Use 2 decimal places for currency, RoundingMode.HALF_EVEN is standard
            return totalSpending.divide(BigDecimal.valueOf(totalHours), 2, RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }


    private static List<Transaction> getNewTransactions() {
        LocalDateTime now = LocalDateTime.now();

        return Arrays.asList(
                // T1: AccountA - Normal spending (Should NOT trigger)
                new Transaction("AccountA", new BigDecimal("20.00"), now.minusMinutes(59)),

                // T2: AccountB - Spike! (Current 1hr spending: $10.00 > Threshold $5.00)
                new Transaction("AccountB", new BigDecimal("10.00"), now.minusMinutes(40)),

                // T3: AccountA - High spending, but still below threshold (1hr spending: $20 + $25 = $45.00 < $50.00)
                new Transaction("AccountA", new BigDecimal("25.00"), now.minusMinutes(30)),

                // T4: AccountA - Spike! (1hr spending: $20 + $25 + $100 = $145.00 > $50.00)
                new Transaction("AccountA", new BigDecimal("100.00"), now.minusMinutes(20)),

                // T5: AccountB - Another transaction, AccountB is already flagged, no new alert
                new Transaction("AccountB", new BigDecimal("2.00"), now.minusMinutes(10)),

                // T6: AccountA - An hour has passed since T1. T1 ($20.00) drops out of the window.
                // 1hr spending: $25 + $100 + $10 = $135.00 (Still > $50.00, no *new* alert for A)
                new Transaction("AccountA", new BigDecimal("10.00"), now.plusMinutes(1))
        );
    }

    private static List<Transaction> getHistoricalTransactions() {
        List<Transaction> allHistoricalTransactions = new ArrayList<>();
        // Define a reference time (e.g., now - 8 days)
        LocalDateTime eightDaysAgo = LocalDateTime.now().minusDays(8).truncatedTo(ChronoUnit.HOURS);

        Random random = new Random();

        // Account A: Normal, consistent spending
        List<Transaction> accountA_History = new ArrayList<>();
        // We'll set the amount to a predictable average for testing the threshold
        BigDecimal accountA_Avg = new BigDecimal("10.00");
        for (int i = 0; i < (7 * 24); i++) {
            // Random variation around $10.00
            accountA_History.add(new Transaction("AccountA",
                    accountA_Avg.add(new BigDecimal(random.nextDouble() * 2 - 1).setScale(2, RoundingMode.HALF_EVEN)),
                    eightDaysAgo.plusHours(i).plusMinutes(30)));
        }
        // Total Historical Spending: ~168 * $10.00 = $1680.00
        // **Average Hourly Spending (7 Days): ~$10.00** // **Spike Threshold (5x): $50.00**

        // Account B: Low spending (Should be easy to spike)
        List<Transaction> accountB_History = new ArrayList<>();
        BigDecimal accountB_Avg = new BigDecimal("1.00");
        for (int i = 0; i < (7 * 24); i++) {
            // Random variation around $1.00
            accountB_History.add(new Transaction("AccountB",
                    accountB_Avg.add(new BigDecimal(random.nextDouble() * 0.2 - 0.1).setScale(2, RoundingMode.HALF_EVEN)),
                    eightDaysAgo.plusHours(i).plusMinutes(30)));
        }
        // Total Historical Spending: ~168 * $1.00 = $168.00
        // **Average Hourly Spending (7 Days): ~$1.00** // **Spike Threshold (5x): $5.00**

        allHistoricalTransactions.addAll(accountA_History);
        allHistoricalTransactions.addAll(accountB_History);

        return allHistoricalTransactions;
    }

    /**
     * Detects spending spikes in a list of new transactions based on historical average.
     * The method is static, but needs the detector instance to call the non-static getAverageHourlySpending.
     */
    public static List<String> detectSpendingSpikes(List<Transaction> newTransactions,
                                                    List<Transaction> allHistoricalTransactions,
                                                    DetectSpendingSpikes detector) {
        // 1. Group historical transactions by account ID
        Map<String, List<Transaction>> historyByAccount = allHistoricalTransactions.stream()
                .collect(Collectors.groupingBy(t -> t.accountId));

        // 2. Calculate the spike threshold for each account
        Map<String, BigDecimal> spikeThresholds = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : historyByAccount.entrySet()) {
            BigDecimal averageHourly = detector.getAverageHourlySpending(entry.getValue());
            // Threshold is 5x the average hourly spending
            BigDecimal threshold = averageHourly.multiply(BigDecimal.valueOf(5));
            spikeThresholds.put(entry.getKey(), threshold.setScale(2, RoundingMode.HALF_EVEN));
        }

        // 3. Track all transactions for the "current" window
        // This map stores a list of *all* transactions (historical and new) that have occurred so far,
        // organized by account ID. A simpler approach is to track only *new* transactions in a 1hr window.
        // Since we are processing the new transactions sequentially, we only need to track the transactions
        // in the current 1-hour window for each account.
        // This will track all transactions (historical + new) that have occurred leading up to the current moment.
        List<Transaction> recentTransactions = new LinkedList<>(allHistoricalTransactions);

        // 4. Process new transactions and detect spikes
        Set<String> alertedAccounts = new HashSet<>();

        for (Transaction currentTransaction : newTransactions) {
            String accountId = currentTransaction.accountId;
            LocalDateTime now = currentTransaction.timestamp;
            BigDecimal threshold = spikeThresholds.getOrDefault(accountId, BigDecimal.ZERO);

            if (threshold.compareTo(BigDecimal.ZERO) == 0) {
                // Skip if no historical data/threshold is zero (no average can be calculated)
                continue;
            }

            // Add the current transaction to the recent list for future calculations
            recentTransactions.add(currentTransaction);

            // Define the 1-hour window end and start
            LocalDateTime windowStart = now.minusHours(1);

            // Calculate the total spending for this account within the last 1 hour
            BigDecimal oneHourSpending = recentTransactions.stream()
                    .filter(t -> t.accountId.equals(accountId))
                    // Keep only transactions within the 1-hour window [windowStart, now]
                    .filter(t -> !t.timestamp.isBefore(windowStart) && !t.timestamp.isAfter(now))
                    .map(t -> t.amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_EVEN);


            // Check for spike
            if (oneHourSpending.compareTo(threshold) > 0) {
                alertedAccounts.add(accountId);
                // The prompt implies we only alert once per account per run, so we add to a Set.
                // If we needed to alert on every spike, we'd log/alert here.
                System.out.printf("DEBUG: Account %s: 1-hr spending: %s > Threshold: %s (Triggered by T with amount %s at %s)\n",
                        accountId, oneHourSpending, threshold, currentTransaction.amount, currentTransaction.timestamp);

            } else {
                System.out.printf("DEBUG: Account %s: 1-hr spending: %s <= Threshold: %s\n",
                        accountId, oneHourSpending, threshold);
            }
        }

        return new ArrayList<>(alertedAccounts);
    }
}