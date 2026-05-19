package com.alxt.pfmservice.entity.dto;

import com.alxt.pfmservice.entity.OperationType;
import lombok.Builder;

@Builder
public record CategoryDTO(
        Long id,
        String title,
        OperationType operationType
) {
}
