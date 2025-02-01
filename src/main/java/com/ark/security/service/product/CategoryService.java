package com.ark.security.service.product;

import com.ark.security.dto.request.CategoryRequest;
import com.ark.security.dto.response.CategoryResponse;
import com.ark.security.exception.*;
import com.ark.security.mapper.CategoryMapper;
import com.ark.security.models.product.Category;
import com.ark.security.repository.product.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getById(int id) {
        return categoryMapper.
                toCategoryResponse(categoryRepository
                        .findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
    }

    public CategoryResponse create(CategoryRequest request){
        Category category = categoryMapper.toCategory(request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(int id, CategoryRequest request){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryMapper.updateCategory(category, request);
        try{
            category = categoryRepository.save(category);
        }catch (DataIntegrityViolationException ex){
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        return categoryMapper.toCategoryResponse(category);
    }

    public void delete(int id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

}
