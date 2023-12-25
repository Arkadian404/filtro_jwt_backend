package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.Brand;
import com.ark.security.service.product.BrandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/admin/brand")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminBrandController {
    private final BrandService brandService;

    @GetMapping("/getList")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findBrandById(@PathVariable int id){
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBrand(@Valid @RequestBody Brand brand){
        brandService.saveBrand(brand);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Thêm thương hiệu thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok().body(success);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable int id, @Valid @RequestBody Brand brand){
        brandService.updateBrand(id, brand);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật thương hiệu thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok().body(success);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable int id){
        brandService.deleteBrand(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa thương hiệu thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok().body(success);
    }
}
