package com.ark.security.controller.user;

import com.ark.security.dto.ProductDto;
import com.ark.security.models.product.Product;
import com.ark.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/search")
@RequiredArgsConstructor
public class SearchController {
    private final ProductService productService;


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("query") String name){
        List<ProductDto> productList = productService.getProductDtoByName(name);
        return ResponseEntity.ok(productList);
    }

}
