package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.BrandRequest;
import com.ark.security.dto.response.BrandResponse;
import com.ark.security.service.product.BrandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/brand")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminBrandController {
    private final BrandService brandService;

    @GetMapping
    public ApiResponse<List<BrandResponse>> getList(){
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.getAllBrands())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> findBrandById(@PathVariable int id){
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.getBrandById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@Valid @RequestBody BrandRequest brand){
        return ApiResponse.<BrandResponse>builder()
                .message("Tạo thương hiệu thành công")
                .result(brandService.saveBrand(brand))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable int id, @Valid @RequestBody BrandRequest brand){
        return ApiResponse.<BrandResponse>builder()
                .message("Cập nhật thương hiệu thành công")
                .result(brandService.updateBrand(id, brand))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBrand(@PathVariable int id){
        brandService.deleteBrand(id);
        return ApiResponse.<String>builder()
                .message("Xóa thương hiệu thành công")
                .build();
    }
}
