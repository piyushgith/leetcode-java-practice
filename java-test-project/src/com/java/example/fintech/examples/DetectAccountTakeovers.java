package com.java.example.fintech.examples;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Problem 2: Account Takeover Detection (Medium)
 * Flag accounts with suspicious login patterns:
 * <p>
 * More than 5 failed login attempts within 10 minutes
 * Followed by a successful login within 30 minutes of first failed attempt
 */
public class DetectAccountTakeovers {
    static class LoginAttempt {
        String accountId;
        boolean success;
        LocalDateTime timestamp;

        public LoginAttempt(String accountId, boolean success, LocalDateTime timestamp) {
            this.accountId = accountId;
            this.success = success;
            this.timestamp = timestamp;
        }
    }


    static void main(String[] args) {
        LoginAttempt loginAttempt1 = new LoginAttempt("user123", false, LocalDateTime.now().minusMinutes(11));
        LoginAttempt loginAttempt2 = new LoginAttempt("user123", false, LocalDateTime.now().minusMinutes(10));
        LoginAttempt loginAttempt3 = new LoginAttempt("user123", false, LocalDateTime.now().minusMinutes(9));
        LoginAttempt loginAttempt4 = new LoginAttempt("user123", false, LocalDateTime.now().minusMinutes(7));
        LoginAttempt loginAttempt5 = new LoginAttempt("user123", false, LocalDateTime.now().minusMinutes(5));
        LoginAttempt loginAttempt6 = new LoginAttempt("user123", false, LocalDateTime.now().minusMinutes(3));
        LoginAttempt loginAttempt7 = new LoginAttempt("user123", true, LocalDateTime.now().minusMinutes(1));

        List<LoginAttempt> attempts = Arrays.asList(loginAttempt1, loginAttempt2, loginAttempt3, loginAttempt4, loginAttempt5, loginAttempt6, loginAttempt7);

        List<String> flaggedAccounts = detectAccountTakeovers(attempts);
        flaggedAccounts.forEach(System.out::println);
    }


    public static List<String> detectAccountTakeovers(List<LoginAttempt> attempts) {
        // Implementation goes here
        List<String> flaggedAccounts = new ArrayList<>();
        Map<String, List<LoginAttempt>> attemptsByAccount = new HashMap<>();

        // Group login attempts by account ID
        for (LoginAttempt attempt : attempts) {
            attemptsByAccount.computeIfAbsent(attempt.accountId, v -> new ArrayList<>()).add(attempt);
        }

        // Check each account's login attempts for suspicious patterns
        for (Map.Entry<String, List<LoginAttempt>> login : attemptsByAccount.entrySet()) {
            String accountId = login.getKey();
            List<LoginAttempt> accountAttempts = login.getValue();

            // Sort attempts by timestamp
            accountAttempts.sort(Comparator.comparing(a -> a.timestamp));
            if (isAccountSuspicious(accountAttempts)) {
                // Add to flagged accounts
                flaggedAccounts.add(accountId);
            }
        }
        return flaggedAccounts;
    }


    public static boolean isAccountSuspicious(List<LoginAttempt> attempts) {
        // Implementation goes here
        for (int i = 0; i < attempts.size(); i++) {
            LocalDateTime startTime = attempts.get(i).timestamp;
            LocalDateTime tenMinutesLater = startTime.plusMinutes(10);
            LocalDateTime thirtyMinutesLater = startTime.plusMinutes(30);

            int failedCount = 0;
            boolean hasSuccessfulAfterFailed = false;
            for (int j = i; j < attempts.size(); j++) {
                LoginAttempt current = attempts.get(j);
                // If the transaction is beyond 30 minutes from start time, break
                if (current.timestamp.isAfter(thirtyMinutesLater)) {
                    break;
                }

                if (!current.timestamp.isAfter(tenMinutesLater)) {
                    if (!current.success) {
                        failedCount++;
                    }
                }
                // Check for successful login within 30 minutes after failed attempts
                if (current.success
                        && current.timestamp.isAfter(startTime)
                        && current.timestamp.isBefore(thirtyMinutesLater)) {
                    hasSuccessfulAfterFailed = true;
                }
            }
            // If more than 5 failed attempts in first 10 minutes AND successful login within 30 minutes
            if (failedCount > 5 && hasSuccessfulAfterFailed) {
                return true;
            }
        }
        return false;
    }
}
