package com.ark.security.controller.admin;

import com.ark.security.models.Category;
import com.ark.security.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin/category")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getCategoryList(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        Category category = categoryService.getCategoryById(id);
        if(category == null){
            return ResponseEntity.badRequest().body("Không tìm thấy danh mục: "+ id);
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> create(@RequestBody Category category){
        if(category == null){
            return ResponseEntity.badRequest().body("Danh mục không được trống");
        }
        if(categoryService.isDuplicateCategoryName(category.getName())){
            return ResponseEntity.badRequest().body("Danh mục đã tồn tại");
        }
        categoryService.saveCategory(category);
        return ResponseEntity.ok("Thêm danh mục thành công");
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Category category){
        Category oldCategory = categoryService.getCategoryById(id);
        if(oldCategory == null){
            return ResponseEntity.badRequest().body("Nội dung danh mục không được để trống");
        }
        if(category == null){
            return ResponseEntity.badRequest().body("Không tìm thấy danh mục: "+id);
        }
        categoryService.updateCategory(id, category);
        return ResponseEntity.ok("Cập nhật danh mục thành công");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        Category category = categoryService.getCategoryById(id);
        if(category == null){
            return ResponseEntity.badRequest().body("Không tìm thấy danh mục: "+id);
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Xóa danh mục thành công");
    }

}
