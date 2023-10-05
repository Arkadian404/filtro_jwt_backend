package com.ark.security.controller.user;

import com.ark.security.models.product.Product;
import com.ark.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getProductList(){
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }



    @GetMapping("/getListByCategory/{id}")
    public ResponseEntity<?> getListByCategory(@PathVariable int id){
        return ResponseEntity.ok(productService.getAllProductsByCategory(id));
    }

    @GetMapping("/getListByOrigin/{id}")
    public ResponseEntity<?> getListByOrigin(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsByOrigin(id));
    }


    @GetMapping("/getListByFlavor/{id}")
    public ResponseEntity<?> getListByFlavor(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsByFlavor(id));
    }

    @GetMapping("/getListByIsSpecial")
    public ResponseEntity<?> getListByIsSpecial(){
        return ResponseEntity.ok(productService.getProductsByIsSpecial(true));
    }

    @GetMapping("/getListByIsLimited")
    public ResponseEntity<?> getListByIsLimited(){
        return ResponseEntity.ok(productService.getProductsByIsLimited(true));
    }

    @GetMapping("/getListByVendor/{id}")
    public ResponseEntity<?> getListByVendor(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsByVendor(id));
    }

    @GetMapping("/getListBySale/{id}")
    public ResponseEntity<?> getListBySale(@PathVariable int id){
        return ResponseEntity.ok(productService.getProductsBySale(id));
    }


    @GetMapping("/getTop3LatestProducts")
    public ResponseEntity<?> getTop3Latest(){
        return ResponseEntity.ok(productService.getTop3LatestProducts());
    }

    @GetMapping("/getTop3BestSellerProducts")
    public ResponseEntity<?> getTop3BestSeller(){
        return ResponseEntity.ok(productService.getTop3BestSellerProducts());
    }


    @GetMapping("/getTop3SpecialProducts")
    public ResponseEntity<?> getTop3Special(){
        return ResponseEntity.ok(productService.getTop3SpecialProducts());
    }


    @GetMapping("/getTop10ProductsInColombia")
    public ResponseEntity<?> getTop10ProductsInColombia(){
        return ResponseEntity.ok(productService.getTop10ProductsInColombia());
    }


    @GetMapping("/getTop10ProductsByRoastedCoffeeBeans")
    public ResponseEntity<?> getTop10ProductsByRoastedCoffeeBeans(){
        return ResponseEntity.ok(productService.getTop10ProductsByRoastedCoffeeBeans());
    }


    @GetMapping("/getTop10ProductsByBottledCoffee")
    public ResponseEntity<?> getTop10ProductsByBottledCoffee(){
        return ResponseEntity.ok(productService.getTop10ProductsByBottledCoffee());
    }

}
