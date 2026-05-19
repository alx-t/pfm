package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Category;
import com.alxt.pfmservice.entity.OperationType;
import com.alxt.pfmservice.entity.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDTO> findAllUserCategories(String userId, String filter);

    CategoryDTO createCategory(String userId, String title, OperationType operationType);

    Optional<CategoryDTO> findUserCategory(String userId, Long categoryId);

    void updateCategory(String userId, Long categoryId, String title);

    void deleteCategory(String userId, Long categoryId);
}
