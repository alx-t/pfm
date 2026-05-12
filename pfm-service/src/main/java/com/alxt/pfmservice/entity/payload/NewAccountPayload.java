package com.alxt.pfmservice.entity.payload;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record NewAccountPayload(
        @NotNull(message = "{account.create.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{account.create.errors.title_size_is_invalid}")
        String title,

        @NotNull
        AccountType accountType,

        @NotNull
        Currency currency,

        @NotNull
        @PositiveOrZero
        BigDecimal amountCurrency
) {

        // TODO: сделать пересчет в валюте по курсу
        public Account toAccount() {
                return new Account(null, title(), accountType(), currency(), amountCurrency(), amountCurrency());
        }
}
