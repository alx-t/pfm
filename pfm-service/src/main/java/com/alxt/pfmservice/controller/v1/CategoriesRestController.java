package com.alxt.pfmservice.controller.v1;

import com.alxt.pfmservice.entity.dto.CategoryDTO;
import com.alxt.pfmservice.entity.payload.NewCategoryPayload;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(
        name = "Категории",
        description = "Ручки для работы с категориями"
)
public class CategoriesRestController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Получить информацию о категориях")
    public List<CategoryDTO> findUserCategories(
            @Parameter(description = "фильтр") @RequestParam(name = "filter", required = false) String filter,
            JwtAuthenticationToken auth
    ) {
        return categoryService.findAllUserCategories(UserUtils.getUserId(auth), filter);
    }

    @PostMapping
    @Operation(summary = "Создать новую категорию")
    public ResponseEntity<?> createCategory(
            @Parameter(description = "Данные для создания новой") @Valid @RequestBody NewCategoryPayload payload,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder,
            JwtAuthenticationToken auth
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            var category = categoryService.createCategory(
                    UserUtils.getUserId(auth), payload.title(), payload.operationType()
            );
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("api/v1/categories/{categoryId}")
                            .build(Map.of("categoryId", category.id())))
                    .body(category);
        }
    }
}
