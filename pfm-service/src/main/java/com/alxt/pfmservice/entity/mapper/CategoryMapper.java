package com.alxt.pfmservice.entity.mapper;

import com.alxt.pfmservice.entity.Category;
import com.alxt.pfmservice.entity.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    Category toEntity(CategoryDTO categoryDTO);
    CategoryDTO toDTO(Category category);
}
