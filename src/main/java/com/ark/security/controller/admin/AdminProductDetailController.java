package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.ProductDetailRequest;
import com.ark.security.dto.response.ProductDetailResponse;
import com.ark.security.service.product.ProductDetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product-detail")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminProductDetailController {
    private final ProductDetailService productDetailService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductDetailResponse>> getAllProductDetail(){
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .result(productDetailService.getAllProductDetails())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<ProductDetailResponse> find(@PathVariable int id){
        return ApiResponse.<ProductDetailResponse>builder()
                .result(productDetailService.getProductDetailById(id))
                .build();
    }

    @GetMapping("/product/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductDetailResponse>> getListByProduct(@PathVariable int id){
        return ApiResponse.<List<ProductDetailResponse>>builder()
                .result(productDetailService.getAllByProductId(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<ProductDetailResponse> create(@Valid @RequestBody ProductDetailRequest productDetail){
        return ApiResponse.<ProductDetailResponse>builder()
                .message("Tạo chi tiết sản phẩm thành công")
                .result(productDetailService.createProductDetail(productDetail))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<ProductDetailResponse> update(@PathVariable int id,@Valid @RequestBody ProductDetailRequest productDetail){
        return ApiResponse.<ProductDetailResponse>builder()
                .message("Cập nhật chi tiết sản phẩm thành công")
                .result(productDetailService.updateProductDetail(id, productDetail))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String> delete(@PathVariable int id){
        productDetailService.deleteProductDetail(id);
        return ApiResponse.<String>builder()
                .message("Xóa chi tiết sản phẩm thành công")
                .build();
    }

}
