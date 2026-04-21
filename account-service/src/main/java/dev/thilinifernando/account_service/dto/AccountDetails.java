package dev.thilinifernando.account_service.dto;
import java.math.BigDecimal;

public record AccountDetails(
        String userId,
        BigDecimal balance,
        String currency
) {
}
