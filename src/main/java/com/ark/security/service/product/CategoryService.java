package com.ark.security.service.product;

import com.ark.security.dto.CategoryDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.product.Category;
import com.ark.security.repository.product.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final String NOT_FOUND = "Không tìm thấy danh mục nào: ";
    private final String EMPTY = "Không có danh mục nào";
    private final String DUPLICATE = "Danh mục đã tồn tại";
    private final String NULL = "Không được để trống";


    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return categories;
    }

    public List<CategoryDto> getAllCategoriesDto() {
        List<CategoryDto> categories = categoryRepository.findAll()
                .stream()
                .map(Category::convertToDto)
                .toList();
        if(categories.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return categories;
    }

    public boolean isDuplicateCategoryName(String name) {
        return categoryRepository.findCategoryByName(name).isPresent();
    }


    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND + id));
    }

    Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElseThrow(()-> new NotFoundException(NOT_FOUND + name));
    }

    public void saveCategory(Category category) {
        if(isDuplicateCategoryName(category.getName())){
            throw new DuplicateException(DUPLICATE);
        }
        categoryRepository.save(category);
    }

    public void updateCategory(int id, Category category) {
        Category oldCategory = getCategoryById(id);
        if(category == null){
            throw new NullException(NULL);
        }
        if (oldCategory != null) {
            oldCategory.setName(category.getName());
            oldCategory.setStatus(category.getStatus());
            categoryRepository.save(oldCategory);
        }else{
            throw new NotFoundException(NOT_FOUND + id);
        }
    }

//    @Transactional
    public void deleteCategory(int id) {
        Category category = getCategoryById(id);
        if(category == null){
            throw new NotFoundException(NOT_FOUND + id);
        }
        categoryRepository.deleteById(id);
    }

}
