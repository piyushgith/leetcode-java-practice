package com.java.example.fintech.examples;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FraudDetectionExample {
    public static void main(String[] args) {
        List<String> suspiciousAccounts = detectSuspiciousAccounts(getTestTransfers());
        suspiciousAccounts.forEach(System.out::println);
    }

    // The required window duration is 5 minutes
    private static final Duration WINDOW_DURATION = Duration.ofMinutes(5);


    /**
     * Detects accounts that performed suspicious transfers.
     * An account is considered suspicious if it performs more than 3 transfers
     * (i.e., >= 4) to more than 3 different destination accounts (i.e., >= 4)
     * within any 5-minute window.
     *
     * @param transfers list of all transfers, sorted chronologically by timestamp
     * @return list of source account IDs flagged as suspicious (no duplicates)
     */
    public static List<String> detectSuspiciousAccounts(List<Transfer> transfers) {

        // Group transfers by source account
        Map<String, List<Transfer>> transfersByAccount = new HashMap<>();
        Set<String> suspiciousAccounts = new HashSet<>();

        //group transfers by source account
        for (Transfer t : transfers) {
            transfersByAccount.computeIfAbsent(t.getSourceAccount(), k -> new ArrayList<>()).add(t);
        }

        // Check each account for suspicious activity
        for (Map.Entry<String, List<Transfer>> entry : transfersByAccount.entrySet()) {
            String account = entry.getKey();
            List<Transfer> accountTransfers = entry.getValue();

            // Need at least 4 transfers to be suspicious
            if (accountTransfers.size() < 4) {
                continue;
            }

            // Use sliding window to check for suspicious patterns
            for (int i = 0; i < accountTransfers.size(); i++) {
                LocalDateTime windowStart = accountTransfers.get(i).getTimestamp();
                LocalDateTime windowEnd = windowStart.plus(WINDOW_DURATION);
                //LocalDateTime windowEnd = windowStart.plusMinutes(5);

                Set<String> uniqueDestinations = new HashSet<>();
                int transferCount = 0;

                // Count transfers and unique destinations within the window
                for (int j = i; j < accountTransfers.size(); j++) {
                    Transfer t = accountTransfers.get(j);
                    // Check if transfer is within the 5-minute window
                    if (!t.getTimestamp().isAfter(windowEnd)) {
                        transferCount++;
                        uniqueDestinations.add(t.getDestinationAccount());
                    } else {
                        break; // No need to check further transfers
                    }
                }

                // Check if this window meets suspicious criteria
                if (transferCount >= 4 && uniqueDestinations.size() >= 4) {
                    suspiciousAccounts.add(account);
                    break; // No need to check more windows for this account
                }
            }
        }

        return new ArrayList<>(suspiciousAccounts);
    }


    /**
     * Detects accounts that performed suspicious transfers.
     * An account is considered suspicious if it performs more than 3 transfers
     * (i.e., >= 4) to more than 3 different destination accounts (i.e., >= 4)
     * within any 5-minute window.
     *
     * @param transfers list of all transfers, sorted chronologically by timestamp
     * @return list of source account IDs flagged as suspicious (no duplicates)
     */
    public static List<String> detectSuspiciousAccounts1(List<Transfer> transfers) {

        // 1. Map to hold the sliding window (Queue) of transfers for each source account
        Map<String, Deque<Transfer>> windowTransfers = new HashMap<>();

        // 2. Map to hold the count of UNIQUE destinations for each source account's window
        // Inner Map: Destination ID -> Count (how many times this destination appears in the window)
        Map<String, Map<String, Integer>> uniqueDestinationCounts = new HashMap<>();

        // 3. Set to store the final list of suspicious account IDs (handles duplicates)
        Set<String> suspiciousAccounts = new HashSet<>();

        // Iterate through all transfers chronologically
        for (Transfer currentTransfer : transfers) {
            String sourceId = currentTransfer.getSourceAccount();

            // Get or initialize the data structures for the source account
            Deque<Transfer> window = windowTransfers.computeIfAbsent(sourceId, k -> new LinkedList<>());
            Map<String, Integer> destCounts = uniqueDestinationCounts.computeIfAbsent(sourceId, k -> new HashMap<>());

            // --- Sliding Window Maintenance: Shrink ---

            // Calculate the required minimum timestamp for transfers to remain in the window
            LocalDateTime windowStartBoundary = currentTransfer.getTimestamp().minus(WINDOW_DURATION);

            // Remove transfers that fall outside the 5-minute window (too old)
            while (!window.isEmpty() && window.peekFirst().getTimestamp().isBefore(windowStartBoundary)) {
                Transfer oldestTransfer = window.removeFirst();
                String oldestDestId = oldestTransfer.getDestinationAccount();

                // Decrement the count for the destination of the removed transfer
                destCounts.computeIfPresent(oldestDestId, (k, v) -> v - 1);

                // Clean up: If count reaches 0, remove the destination entirely from the map
                if (destCounts.get(oldestDestId) == 0) {
                    destCounts.remove(oldestDestId);
                }
            }

            // --- Sliding Window Maintenance: Expand ---

            // Add the current transfer to the window
            window.addLast(currentTransfer);
            String currentDestId = currentTransfer.getDestinationAccount();

            // Increment the count for the destination of the current transfer
            destCounts.merge(currentDestId, 1, Integer::sum);

            // --- Suspicion Check ---
            // Condition 1: Total transfers > 3 (i.e., size >= 4)
            boolean totalTransfersMet = window.size() > 3;

            // Condition 2: Unique destination accounts > 3 (i.e., size >= 4)
            boolean uniqueDestinationsMet = destCounts.size() > 3;

            if (totalTransfersMet && uniqueDestinationsMet) {
                // Both criteria met
                suspiciousAccounts.add(sourceId);
            }
        }

        // Return the set of suspicious accounts as a List
        return new ArrayList<>(suspiciousAccounts);
    }


    public static List<Transfer> getTestTransfers() {
        // Use a dummy amount
        BigDecimal amount = BigDecimal.TEN;
        // Define a starting point for the test timestamps
        LocalDateTime START_TIME = LocalDateTime.of(2025, 11, 7, 10, 0, 0);


        return Arrays.asList(
                // --- SCENARIO 1: POSITIVE CASE (AccountA becomes Suspicious) ---
                // All 4 transfers occur between 10:00:00 and 10:04:00 (within 5 minutes).
                // T1: Start of the window (Time: 0)
                new Transfer("AccountA", "Dest1", amount, START_TIME.plusMinutes(0)),
                // T2: 2nd transfer (Time: 1)
                new Transfer("AccountA", "Dest2", amount, START_TIME.plusMinutes(1)),
                // T3: 3rd transfer (Time: 2)
                new Transfer("AccountA", "Dest3", amount, START_TIME.plusMinutes(2)),
                // T4: 4th transfer, triggers suspicion (Time: 3)

                // Window size: 4. Unique Dest: 4 (Dest1, Dest2, Dest3, Dest4).
                new Transfer("AccountA", "Dest4", amount, START_TIME.plusMinutes(3)),
                // --- SCENARIO 2: NEGATIVE CASE (Too few unique destinations - AccountB) ---
                // AccountB has 4 transfers, but only 3 unique destinations.
                // T5:
                new Transfer("AccountB", "DestX", amount, START_TIME.plusMinutes(4)),
                // T6:
                new Transfer("AccountB", "DestY", amount, START_TIME.plusMinutes(5)),
                // T7:
                new Transfer("AccountB", "DestZ", amount, START_TIME.plusMinutes(6)),
                // T8: Total transfers = 4. Unique Dest = 3 (DestX, DestY, DestZ). NOT suspicious.
                new Transfer("AccountB", "DestX", amount, START_TIME.plusMinutes(7)),

                // --- SCENARIO 3: NEGATIVE CASE (Window size violated - AccountA) ---
                // T9 will push T1 (at 10:00:00) out of the window.
                // T9: 5th transfer for AccountA (Time: 6)
                // The window now includes T2, T3, T4, T9 (4 transfers).
                // T1 (at 10:00:00) is now older than 5 minutes relative to T9 (10:06:00) and slides out.
                // The new unique destinations are: Dest2, Dest3, Dest4, Dest5. Still 4 unique.
                // AccountA remains suspicious because T2, T3, T4, T9 meet the criteria on their own.
                new Transfer("AccountA", "Dest5", amount, START_TIME.plusMinutes(6)),
                // T10: This transfer is too far out to cause suspicion (Time: 10)
                // It is exactly 10 minutes after T1, which is now long gone.
                new Transfer("AccountB", "DestA", amount, START_TIME.plusMinutes(10)),
                // --- SCENARIO 4: NEGATIVE CASE (Too few transfers - AccountB) ---
                // AccountB makes transfers to 4 different destinations, but only 3 transfers in total.
                // T11: Time: 11
                new Transfer("AccountB", "DestB", amount, START_TIME.plusMinutes(11)),
                // T12: Time: 12
                new Transfer("AccountB", "DestC", amount, START_TIME.plusMinutes(12)),
                // T13: Time: 13. Total transfers = 3. Unique Dest = 4 (DestA, DestB, DestC, DestD). NOT suspicious.
                new Transfer("AccountB", "DestD", amount, START_TIME.plusMinutes(13)));
    }
}

