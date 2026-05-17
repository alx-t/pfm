package com.alxt.pfmservice.entity.dto;

import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountDTO(
        Long id,
        String title,
        AccountType accountType,
        Currency currency,
        BigDecimal amount,
        BigDecimal amountCurrency
) {
}
