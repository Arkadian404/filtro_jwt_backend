package com.ark.security.controller.admin;

import com.ark.security.models.Product;
import com.ark.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin/product")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getProductList(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> create(@RequestBody Product product){
        if(product == null){
            return ResponseEntity.badRequest().body("Không được để trống");
        }
        productService.saveProduct(product);
        return ResponseEntity.ok("Tạo sản phẩm thành công");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<?> update(@PathVariable int id, @Validated @RequestBody Product product){
        Product newProduct = productService.getProductById(id);
        if(newProduct == null){
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm");
        }
        if(product == null){
            return ResponseEntity.badRequest().body("Không được để trống");
        }
        productService.updateProduct(id, product);
        return ResponseEntity.ok("Cập nhật sản phẩm thành công");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        Product product = productService.getProductById(id);
        if(product == null){
            return ResponseEntity.badRequest().body("Không tìm thấy sản phẩm");
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công");
    }

}
