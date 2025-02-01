package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.ProductOriginRequest;
import com.ark.security.dto.response.ProductOriginResponse;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.ProductOrigin;
import com.ark.security.service.product.ProductOriginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product-origin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminProductOriginController {
    private final ProductOriginService productOriginService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<ProductOriginResponse>> getAllProductOrigin(){
        return ApiResponse.<List<ProductOriginResponse>>builder()
                .result(productOriginService.getAllProductOrigins())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<ProductOriginResponse> find(@PathVariable int id){
        return ApiResponse.<ProductOriginResponse>builder()
                .result(productOriginService.getProductOriginById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<ProductOriginResponse> create(@Valid @RequestBody ProductOriginRequest productOrigin){
        return ApiResponse.<ProductOriginResponse>builder()
                .message("Tạo xuất xứ sản phẩm thành công")
                .result(productOriginService.createProductOrigin(productOrigin))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<ProductOriginResponse> update(@PathVariable int id, @Valid @RequestBody ProductOriginRequest productOrigin){
        return ApiResponse.<ProductOriginResponse>builder()
                .message("Cập nhật xuất xứ sản phẩm thành công")
                .result(productOriginService.updateProductOrigin(id, productOrigin))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String> delete(@PathVariable int id){
        productOriginService.deleteProductOrigin(id);
        return ApiResponse.<String>builder()
                .message("Xóa xuất xứ sản phẩm thành công")
                .build();
    }


}
