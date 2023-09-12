package com.ark.security.service;

import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.Category;
import com.ark.security.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new NotFoundException("Không có danh mục nào");
        }
        return categories;
    }

    public boolean isDuplicateCategoryName(String name) {
        return categoryRepository.findCategoryByName(name).isPresent();
    }

    List<Category> get5Categories() {
        List<Category> categories = categoryRepository.find5Categories();
        if(categories.isEmpty()){
            throw new NotFoundException("Không có danh mục nào");
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy danh mục: "+ id));
    }

    Category getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElseThrow(()-> new NotFoundException("Không tìm thấy danh mục: "+ name));
    }

    public void saveCategory(Category category) {
        if(isDuplicateCategoryName(category.getName())){
            throw new DuplicateException("Danh mục đã tồn tại");
        }
        categoryRepository.save(category);
    }

    public void updateCategory(int id, Category category) {
        Category oldCategory = getCategoryById(id);
        if(category == null){
            throw new NullException("Không được để trống");
        }
        if (oldCategory != null) {
            oldCategory.setName(category.getName());
            oldCategory.setStatus(category.getStatus());
            categoryRepository.save(oldCategory);
        }else{
            throw new NotFoundException("Không tìm thấy danh mục: "+ id);
        }
    }

//    @Transactional
    public void deleteCategory(int id) {
        Category category = getCategoryById(id);
        if(category == null){
            throw new NotFoundException("Không tìm thấy danh mục: "+ id);
        }
        categoryRepository.deleteById(id);
    }

}
