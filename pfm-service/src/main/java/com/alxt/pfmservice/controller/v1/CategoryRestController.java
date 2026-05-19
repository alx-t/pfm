package com.alxt.pfmservice.controller.v1;

import com.alxt.pfmservice.entity.dto.CategoryDTO;
import com.alxt.pfmservice.entity.payload.UpdateCategoryPayload;
import com.alxt.pfmservice.service.CategoryService;
import com.alxt.pfmservice.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories/{categoryId}")
@Tag(
        name = "Категория",
        description = "Ручки для работы с категорией"
)
public class CategoryRestController {

    private final CategoryService categoryService;

    @ModelAttribute("category")
    public CategoryDTO getCategory(
            @Parameter(description = "Категория") @PathVariable Long categoryId,
            JwtAuthenticationToken auth
    ) {
        return categoryService
                .findUserCategory(UserUtils.getUserId(auth), categoryId)
                .orElseThrow(() -> new NoSuchElementException("errors.category.not_found"));
    }

    @GetMapping
    @Operation(summary = "Получить информацию о категории")
    public CategoryDTO findCategory(@ModelAttribute("category") CategoryDTO category) {
        return category;
    }

    @PatchMapping
    @Operation(summary = "Изменить категорию")
    public ResponseEntity<?> updateCategory(
            @Parameter(description = "Номер категории") @PathVariable Long categoryId,
            @Parameter(description = "Данные обновленной категории") @Valid @RequestBody UpdateCategoryPayload payload,
            BindingResult bindingResult,
            JwtAuthenticationToken auth
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            categoryService.updateCategory(UserUtils.getUserId(auth), categoryId, payload.title());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    @Operation(summary = "Удалить категорию")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId, JwtAuthenticationToken auth) {
        categoryService.deleteCategory(UserUtils.getUserId(auth), categoryId);
        return ResponseEntity.noContent().build();
    }
}
