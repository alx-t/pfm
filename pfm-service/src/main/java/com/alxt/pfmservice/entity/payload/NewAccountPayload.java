package com.alxt.pfmservice.entity.payload;

import com.alxt.pfmservice.entity.Account;
import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Данные для создания нового счета")
public record NewAccountPayload(

        @Schema(description = "Название счета")
        @NotNull(message = "{account.create.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{account.create.errors.title_size_is_invalid}")
        String title,

        @Schema(description = "Тип счета", example = "0")
        @NotNull
        AccountType accountType,

        @Schema(description = "Валюта счета", example = "RUB")
        @NotNull
        Currency currency,

        @Schema(description = "Остаток")
        @NotNull
        @PositiveOrZero
        BigDecimal amountCurrency
) {}
