package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.ProductRequest;
import com.ark.security.dto.response.ProductResponse;
import com.ark.security.service.product.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/product")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getProductList(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<ProductResponse> find(@PathVariable int id){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(id))
                .build();
    }



    @GetMapping("/category/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getListByCategory(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsByCategory(id))
                .build();
    }

    @GetMapping("/origin/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getListByOrigin(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsByOrigin(id))
                .build();
    }


    @GetMapping("/flavor/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getListByFlavor(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsByFlavor(id))
                .build();
    }

    @GetMapping("/special")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getListByIsSpecial(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getSpecialProducts())
                .build();
    }

    @GetMapping("/limited")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getListByIsLimited(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getLimitedProducts())
                .build();
    }

    @GetMapping("/vendor/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductResponse>> getListByVendor(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProductsByVendor(id))
                .build();
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest product){
        return ApiResponse.<ProductResponse>builder()
                .message("Tạo sản phẩm thành công")
                .result(productService.createProduct(product))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<ProductResponse> update(@PathVariable int id, @Valid @RequestBody ProductRequest product){
        return ApiResponse.<ProductResponse>builder()
                .message("Cập nhật sản phẩm thành công")
                .result(productService.updateProduct(id, product))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String > delete(@PathVariable int id){
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .message("Xóa sản phẩm thành công")
                .build();
    }

}
