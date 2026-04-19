package dev.thilinifernando.payment_service.model;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String currency,
        String userId,
        String orderId,
        PaymentMethod paymentMethod
) {}
