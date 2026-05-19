package com.alxt.pfmservice.entity.payload;

import com.alxt.pfmservice.entity.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для создания новой категории")
public record NewCategoryPayload(

        @Schema(description = "Название категории")
        @NotNull(message = "{account.create.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{account.create.errors.title_size_is_invalid}")
        String title,

        @Schema(description = "Тип категории", example = "0")
        @NotNull
        OperationType operationType
) {
}
