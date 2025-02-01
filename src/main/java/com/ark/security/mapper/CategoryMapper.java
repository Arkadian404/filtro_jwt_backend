package com.ark.security.mapper;

import com.ark.security.dto.request.CategoryRequest;
import com.ark.security.dto.response.CategoryResponse;
import com.ark.security.models.product.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
    CategoryResponse toCategoryResponse(Category category);
}
