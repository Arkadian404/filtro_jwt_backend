package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.BrandRequest;
import com.ark.security.dto.response.BrandResponse;
import com.ark.security.service.product.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandTestService;


    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands(){
        return ApiResponse.<List<BrandResponse>>builder()
                .message("Get all brands successfully")
                .result(brandTestService.getAllBrands())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandResponse> getBrandById(@PathVariable int id){
        return ApiResponse.<BrandResponse>builder()
                .message("Get brand by id successfully")
                .result(brandTestService.getBrandById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<BrandResponse> saveBrand(@RequestBody @Valid BrandRequest request){
        return ApiResponse.<BrandResponse>builder()
                .message("Save brand successfully")
                .result(brandTestService.saveBrand(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable int id, @RequestBody @Valid BrandRequest request){
        return ApiResponse.<BrandResponse>builder()
                .message("Update brand successfully")
                .result(brandTestService.updateBrand(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBrand(@PathVariable int id){
        brandTestService.deleteBrand(id);
        return ApiResponse.<String>builder()
                .message("Delete brand successfully")
                .result("Delete brand successfully")
                .build();
    }
}
