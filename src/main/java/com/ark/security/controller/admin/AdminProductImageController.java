package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.ProductImageRequest;
import com.ark.security.dto.response.ProductImageResponse;
import com.ark.security.service.product.ProductImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product-image")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminProductImageController {
    private final ProductImageService productImageService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read')")
    public ApiResponse<List<ProductImageResponse>> getList(){
        return ApiResponse.<List<ProductImageResponse>>builder()
                .result(productImageService.getAllProductImages())
                .build();
    }

    @GetMapping("/product/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read')")
    public ApiResponse<List<ProductImageResponse>> getListByProductId(@PathVariable int id){
        return ApiResponse.<List<ProductImageResponse>>builder()
                .result(productImageService.getAllByProductId(id))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<ProductImageResponse> find(@PathVariable int id){
        return ApiResponse.<ProductImageResponse>builder()
                .result(productImageService.getProductImageById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<ProductImageResponse> create(@Valid @RequestBody ProductImageRequest productImage){
        return ApiResponse.<ProductImageResponse>builder()
                .message("Tạo hình ảnh thành công")
                .result(productImageService.createProductImage(productImage))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<ProductImageResponse> update(@PathVariable int id,@Valid @RequestBody ProductImageRequest productImage){
        return ApiResponse.<ProductImageResponse>builder()
                .message("Cập nhật hình ảnh thành công")
                .result(productImageService.updateProductImage(id, productImage))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String> delete(@PathVariable int id){
        productImageService.deleteProductImage(id);
        return ApiResponse.<String>builder()
                .message("Xóa hình ảnh thành công")
                .build();
    }
}
