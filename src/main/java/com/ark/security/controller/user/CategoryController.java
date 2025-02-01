package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.CategoryRequest;
import com.ark.security.dto.response.CategoryResponse;
import com.ark.security.service.product.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryTestService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll(){
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryTestService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable int id){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryTestService.getById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody @Valid CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryTestService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> update(@PathVariable int id, @RequestBody @Valid CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryTestService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id){
        categoryTestService.delete(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
