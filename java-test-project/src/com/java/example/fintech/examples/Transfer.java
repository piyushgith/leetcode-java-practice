package com.java.example.fintech.examples;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transfer {
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public Transfer(String sourceAccount, String destinationAccount, BigDecimal amount, LocalDateTime timestamp) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
