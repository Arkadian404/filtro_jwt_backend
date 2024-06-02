package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.Product;
import com.ark.security.service.product.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/product")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;


    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getProductList(){
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }



    @GetMapping("/getListByCategory/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListByCategory(@PathVariable int id){
        return ResponseEntity.ok(productService.getAllProductsByCategory(id));
    }

    @GetMapping("/getListByOrigin/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListByOrigin(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsByOrigin(id));
    }


    @GetMapping("/getListByFlavor/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListByFlavor(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsByFlavor(id));
    }

    @GetMapping("/getListByIsSpecial")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListByIsSpecial(){
        return ResponseEntity.ok(productService.getProductsByIsSpecial(true));
    }

    @GetMapping("/getListByIsLimited")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListByIsLimited(){
        return ResponseEntity.ok(productService.getProductsByIsLimited(true));
    }

    @GetMapping("/getListByVendor/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListByVendor(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsByVendor(id));
    }

    @GetMapping("/getListBySale/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getListBySale(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsBySale(id));
    }


    @GetMapping("/getTop3LatestProducts")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getTop3Latest(){
        return ResponseEntity.ok(productService.getTop3LatestProducts());
    }

    @GetMapping("/getTop3BestSellerProducts")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getTop3BestSeller(){
        return ResponseEntity.ok(productService.getTop3BestSellerProducts());
    }


    @GetMapping("/getTop3SpecialProducts")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getTop3Special(){
        return ResponseEntity.ok(productService.getTop3SpecialProducts());
    }


    @GetMapping("/getTop10ProductsInColombia")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getTop10ProductsInColombia(){
        return ResponseEntity.ok(productService.getTop10ProductsInColombia());
    }


    @GetMapping("/getTop10ProductsByRoastedCoffeeBeans")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getTop10ProductsByRoastedCoffeeBeans(){
        return ResponseEntity.ok(productService.getTop10ProductsByRoastedCoffeeBeans());
    }


    @GetMapping("/getTop10ProductsByBottledCoffee")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getTop10ProductsByBottledCoffee(){
        return ResponseEntity.ok(productService.getTop10ProductsByBottledCoffee());
    }


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ResponseEntity<?> create(@Valid @RequestBody Product product){
        productService.saveProduct(product);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Product product){
        productService.updateProduct(id, product);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        productService.deleteProduct(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

}
