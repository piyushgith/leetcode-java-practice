package com.java.example.ratelimiter.examples;

import java.time.Duration; // Import Duration for accurate time difference
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucket {

    private static final double MAX_TOKENS = 10.0;
    private static final double TOKENS_PER_SECOND = 1.0;

    // Store bucket state per user: [tokens, lastRefillTime]
    private final Map<String, BucketState> userBuckets = new ConcurrentHashMap<>();

    static class BucketState {
        double tokens;
        LocalDateTime lastRefillTime;

        BucketState(double tokens, LocalDateTime lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }
    }

    public boolean allowRequest(String userId, LocalDateTime timestamp) {
        // Use computeIfPresent/put for better concurrency control, or stick with computeIfAbsent and synchronize/Atomic variables
        // For simplicity in this example, we proceed with computeIfAbsent
        BucketState bucket = userBuckets.computeIfAbsent(userId, k -> new BucketState(MAX_TOKENS, timestamp));

        // Synchronize on the bucket object to ensure atomic check-and-consume
        synchronized (bucket) {
            refillBucket(bucket, timestamp);
            System.out.println("User: " + userId + ", Tokens after refill: " + bucket.tokens);
            // Check if we can consume one token
            if (bucket.tokens >= 1.0) {
                bucket.tokens -= 1.0;
                return true;
            } else {
                return false;
            }
        }
    }

    private void refillBucket(BucketState bucket, LocalDateTime currentTime) {
        // 1. Calculate the time elapsed in fractional seconds (high precision)
        Duration duration = Duration.between(bucket.lastRefillTime, currentTime);
        double secondsElapsed = duration.toNanos() / 1_000_000_000.0;

        if (secondsElapsed <= 0) {
            System.out.println("Warning: Non-positive time elapsed detected. CurrentTime: " + currentTime + ", LastRefillTime: " + bucket.lastRefillTime);
            return; // No time has elapsed or time went backward
        }

        // 2. Add tokens based on time elapsed (rate * time)
        double newTokens = secondsElapsed * TOKENS_PER_SECOND;

        // 3. Cap tokens at MAX_TOKENS (Burst capacity)
        bucket.tokens = Math.min(MAX_TOKENS, bucket.tokens + newTokens);

        // 4. CRITICAL FIX: Update lastRefillTime to the current request time.
        // This ensures the next request calculates elapsed time accurately from *now*.
        bucket.lastRefillTime = currentTime;
    }

    public static void main(String[] args) {
        TokenBucket rateLimiter = new TokenBucket();
        String userId = "user123";

        LocalDateTime startTime = LocalDateTime.now();

        // Simulate requests
        for (int i = 0; i < 30; i++) {
            LocalDateTime requestTime = startTime.plusSeconds(i / 5); // 5 requests per second
            boolean allowed = rateLimiter.allowRequest(userId, requestTime);
//            System.out.println("Request " + (i + 1) + " at " + requestTime.truncatedTo(ChronoUnit.SECONDS)
//                    + " =" + (allowed ? " Allowed" : "=> Blocked"));
        }
    }
}