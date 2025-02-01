package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.response.ProductResponse;
import com.ark.security.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/search")
@RequiredArgsConstructor
public class SearchController {
    private final ProductService productTestService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> search(@RequestParam String query){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getProductsByName(query))
                .build();
    }

}
