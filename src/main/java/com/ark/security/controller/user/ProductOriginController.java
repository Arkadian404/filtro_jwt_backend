package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.ProductOriginRequest;
import com.ark.security.dto.response.ProductOriginResponse;
import com.ark.security.service.product.ProductOriginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/product-origin")
@RequiredArgsConstructor
public class ProductOriginController {
    private final ProductOriginService productOriginTestService;

    @GetMapping
    public ApiResponse<List<ProductOriginResponse>> getAll(){
        return ApiResponse.<List<ProductOriginResponse>>builder()
                .result(productOriginTestService.getAllProductOrigins())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductOriginResponse> getById(@PathVariable int id){
        return ApiResponse.<ProductOriginResponse>builder()
                .result(productOriginTestService.getProductOriginById(id))
                .build();
    }


    @GetMapping("/continent/{name}")
    public ApiResponse<List<ProductOriginResponse>> getByContinent(@PathVariable String name){
        return ApiResponse.<List<ProductOriginResponse>>builder()
                .result(productOriginTestService.getByContinent(name))
                .build();
    }

    @PostMapping
    public ApiResponse<ProductOriginResponse> create(@RequestBody @Valid ProductOriginRequest request){
        return ApiResponse.<ProductOriginResponse>builder()
                .result(productOriginTestService.createProductOrigin(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductOriginResponse> update(@PathVariable int id, @RequestBody @Valid ProductOriginRequest request){
        return ApiResponse.<ProductOriginResponse>builder()
                .result(productOriginTestService.updateProductOrigin(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable int id){
        productOriginTestService.deleteProductOrigin(id);
        return ApiResponse.<String>builder()
                .result("Product Origin deleted successfully")
                .build();
    }

}   
