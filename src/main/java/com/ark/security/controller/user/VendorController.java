package com.ark.security.controller.user;

import com.ark.security.service.product.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/vendor")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @GetMapping("/getList")
    public ResponseEntity<?> getAllVendor(){
        return ResponseEntity.ok(vendorService.getAllVendorDto());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

}
