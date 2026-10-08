package com.ridewise.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record FareReceipt(String invoiceNumber, String rideId, double amount, LocalDateTime generatedAt) {
    public FareReceipt {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be blank");
        }
        if (rideId == null || rideId.isBlank()) {
            throw new IllegalArgumentException("Ride id cannot be blank");
        }
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be finite and non-negative");
        }
        Objects.requireNonNull(generatedAt, "Generated time cannot be null");
    }
}
