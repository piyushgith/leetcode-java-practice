package com.java.example.fintech.examples;


import java.time.LocalDateTime;
import java.util.*;

/**
 * Detect credit cards that have been used in more than 2 different countries within any 2-hour window.
 * Return list of flagged card numbers.
 */
public class CardTransactionAnomaly {

    static class CardTransaction {
        String cardNumber;
        String country;
        LocalDateTime timestamp;

        public CardTransaction(String cardNumber, String country, LocalDateTime timestamp) {
            this.cardNumber = cardNumber;
            this.country = country;
            this.timestamp = timestamp;
        }
    }

    static void main(String[] args) {
        CardTransaction tx1 = new CardTransaction("1234-5678-9012-3456", "USA", LocalDateTime.of(2024, 6, 1, 10, 0));
        CardTransaction tx2 = new CardTransaction("1234-5678-9012-3456", "CAN", LocalDateTime.of(2024, 6, 1, 10, 30));
        CardTransaction tx3 = new CardTransaction("1234-5678-9012-3456", "IND", LocalDateTime.of(2024, 6, 1, 12, 0));

        List<CardTransaction> transactions = List.of(tx1, tx2, tx3);

        List<String> anomalyCardList = detectSuspiciousCards(transactions);

        anomalyCardList.forEach(System.out::println);
    }

    public static List<String> detectSuspiciousCards(List<CardTransaction> transactions) {
        List<String> suspiciousCards = new java.util.ArrayList<>();

        //group transactions by card number
        Map<String, List<CardTransaction>> transactionsByCard = new HashMap<>();
        for (CardTransaction tx : transactions) {
            transactionsByCard.computeIfAbsent(tx.cardNumber, k -> new java.util.ArrayList<>()).add(tx);
        }

        //check each card's transactions for anomalies
        for (Map.Entry<String, List<CardTransaction>> entry : transactionsByCard.entrySet()) {
            String cardNumber = entry.getKey();
            List<CardTransaction> cardTransactions = entry.getValue();
            // Sort transactions by timestamp
            cardTransactions.sort((a, b) -> a.timestamp.compareTo(b.timestamp));

            boolean suspiciousFlag = hasMoreThan2CountriesIn2HourWindow(cardTransactions);
            if (suspiciousFlag) suspiciousCards.add(cardNumber);
        }
        return suspiciousCards;
    }

    private static boolean hasMoreThan2CountriesIn2HourWindow(List<CardTransaction> transactions) {
        for (int i = 0; i < transactions.size(); i++) {
            LocalDateTime startTime = transactions.get(i).timestamp;
            LocalDateTime endTime = startTime.plusHours(2);

            Set<String> countriesInWindow = new HashSet<>();

            for (int j = i; j < transactions.size(); j++) {
                CardTransaction current = transactions.get(j);
                // If the transaction is beyond 2 hours from start time, break
                if (current.timestamp.isAfter(endTime)) {
                    break;
                }
                countriesInWindow.add(current.country);
            }
            if (countriesInWindow.size() > 2) {
                return true;
            }
        }
        return false;
    }
}
