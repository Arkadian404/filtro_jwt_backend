package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.CategoryRequest;
import com.ark.security.dto.response.CategoryResponse;
import com.ark.security.service.product.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/category")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<CategoryResponse>> getCategoryList(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<CategoryResponse> find(@PathVariable int id){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<CategoryResponse> create(@Valid @RequestBody CategoryRequest category){
        return ApiResponse.<CategoryResponse>builder()
                .message("Tạo danh mục thành công")
                .result(categoryService.create(category))
                .build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<CategoryResponse> update(@PathVariable int id,@Valid @RequestBody CategoryRequest category){
        return ApiResponse.<CategoryResponse>builder()
                .message("Cập nhật danh mục thành công")
                .result(categoryService.update(id, category))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String> delete(@PathVariable int id){
        categoryService.delete(id);
        return ApiResponse.<String>builder()
                .message("Xóa danh mục thành công")
                .build();
    }

}
