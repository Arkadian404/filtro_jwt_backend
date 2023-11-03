package com.ark.security.controller.user;

import com.ark.security.service.product.ProductOriginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/product-origin")
@RequiredArgsConstructor
public class ProductOriginController {

    private final ProductOriginService productOriginService;

    @GetMapping("/getList")
    public ResponseEntity<?> getAllProductOrigin(){
        return ResponseEntity.ok(productOriginService.getAllProductOriginDto());
    }

    @GetMapping("/getListByContinent/{continent}")
    public ResponseEntity<?> getProductOriginByContinent(@PathVariable String continent){
        return ResponseEntity.ok(productOriginService.getProductOriginDtoByContinent(continent));
    }




    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        return ResponseEntity.ok(productOriginService.getProductOriginById(id));
    }

}   
