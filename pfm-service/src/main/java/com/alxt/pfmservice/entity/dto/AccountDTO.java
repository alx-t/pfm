package com.alxt.pfmservice.entity.dto;

import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;

import java.math.BigDecimal;

public record AccountDTO(
        Integer id,
        String title,
        AccountType accountType,
        Currency currency,
        BigDecimal amount,
        BigDecimal amountCurrency
) {
}
