package com.alxt.pfmservice.entity.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для обновления категории")
public record UpdateCategoryPayload(

        @Schema(description = "Название категории")
        @NotNull(message = "{account.update.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{account.update.errors.title_size_is_invalid}")
        String title
) {
}
