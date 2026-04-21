package dev.thilinifernando.payment_service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Currency is required")
        String currency,

        @NotNull(message = "User ID is required")
        String userId,

        @NotNull(message = "Order ID is required")
        String orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod
) {
}
