package com.ark.security.controller.user;

import com.ark.security.dto.CategoryDto;
import com.ark.security.models.product.Category;
import com.ark.security.service.product.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/getList")
    public ResponseEntity<?> getCategoryList(){
        List<CategoryDto> categories = categoryService.getAllCategoriesDto();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
}
