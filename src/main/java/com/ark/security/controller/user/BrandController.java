package com.ark.security.controller.user;

import com.ark.security.service.product.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/getList")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(brandService.getAllBrandsDto());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findBrandById(@PathVariable int id){
        return ResponseEntity.ok(brandService.getBrandById(id));
    }
}
