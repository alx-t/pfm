package com.alxt.pfmservice.entity.payload;

import com.alxt.pfmservice.entity.AccountType;
import com.alxt.pfmservice.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// TODO: подумать над составом обновляемых полей
@Schema(description = "Данные для обновления счета")
public record UpdateAccountPayload(

        @Schema(description = "Название счета")
        @NotNull(message = "{account.update.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{account.update.errors.title_size_is_invalid}")
        String title,

        @NotNull
        AccountType accountType,

        @NotNull
        Currency currency,

        @NotNull
        @PositiveOrZero
        BigDecimal amountCurrency
) {
}
