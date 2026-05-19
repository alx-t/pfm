package com.alxt.pfmservice.service;

import com.alxt.pfmservice.entity.Category;
import com.alxt.pfmservice.entity.OperationType;
import com.alxt.pfmservice.entity.dto.CategoryDTO;
import com.alxt.pfmservice.entity.mapper.CategoryMapper;
import com.alxt.pfmservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> findAllUserCategories(String userId, String filter) {
        List<Category> result;

        if (filter != null && !filter.isBlank()) {
            result =  categoryRepository.findAllByUserIdAndTitleLikeIgnoreCase(userId, "%"+filter+"%");
        } else {
            result =  categoryRepository.findAllByUserId(userId);
        }

        return result
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public CategoryDTO createCategory(String userId, String title, OperationType operationType) {
        return categoryMapper.toDTO(
                categoryRepository.save(
                    new Category(
                            null,
                            title,
                            operationType,
                            userId
                    )
                )
        );
    }

    @Override
    public Optional<CategoryDTO> findUserCategory(String userId, Long categoryId) {
        return Optional.ofNullable(categoryMapper.toDTO(
                categoryRepository.findByUserIdAndId(userId, categoryId).orElse(null)
        ));
    }

    @Override
    @Transactional
    public void updateCategory(String userId, Long categoryId, String title) {
        categoryRepository.findByUserIdAndId(userId, categoryId)
                .ifPresentOrElse(category -> {
                    category.setTitle(title);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    public void deleteCategory(String userId, Long categoryId) {
        categoryRepository.deleteByUserIdAndId(userId, categoryId);
    }
}
