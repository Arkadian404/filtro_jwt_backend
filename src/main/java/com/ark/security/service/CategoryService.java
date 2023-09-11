package com.ark.security.service;

import com.ark.security.models.Category;
import com.ark.security.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public boolean isDuplicateCategoryName(String name) {
        return categoryRepository.findCategoryByName(name).isPresent();
    }

    List<Category> get5Categories() {
        return categoryRepository.find5Categories();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElse(null);
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(int id, Category category) {
        Category oldCategory = getCategoryById(id);
        if (oldCategory != null) {
            oldCategory.setName(category.getName());
            oldCategory.setStatus(category.getStatus());
            categoryRepository.save(oldCategory);
        }
    }

//    @Transactional
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

}
