package dev.thilinifernando.payment_service.util.response;

import java.math.BigDecimal;

public record AccountResponse(
    String userId,
    BigDecimal balance,
    String currency
) {
}
