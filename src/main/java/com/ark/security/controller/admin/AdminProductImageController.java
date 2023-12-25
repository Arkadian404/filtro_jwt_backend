package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.ProductImage;
import com.ark.security.service.product.ProductImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/admin/product-image")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminProductImageController {
    private final ProductImageService productImageService;


    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read')")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(productImageService.getAllProductImages());
    }

    @GetMapping("/getListByProductId/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read')")
    public ResponseEntity<?> getListByProductId(@PathVariable int id){
        return ResponseEntity.ok(productImageService.getProductImagesByProductId(id));
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        return ResponseEntity.ok(productImageService.getProductImageById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ResponseEntity<?> create(@Valid @RequestBody ProductImage productImage){
        productImageService.saveProductImage(productImage);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo ảnh thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ResponseEntity<?> update(@PathVariable int id,@Valid @RequestBody ProductImage productImage){
        productImageService.updateImage(id, productImage);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật hình ảnh thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        productImageService.deleteImage(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa ảnh thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }
}
