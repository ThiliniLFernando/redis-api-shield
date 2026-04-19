package dev.thilinifernando.payment_service.model;

public record PaymentMethod(
        PaymentMethodType type,
        String cardToken) {
}
