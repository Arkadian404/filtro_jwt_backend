package com.ark.security.controller.user;

import com.ark.security.dto.ProductDto;
import com.ark.security.models.product.Product;
import com.ark.security.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @Value("${spring.fast-api.url}")
    private String testString;


    @GetMapping("/getList")
    public ResponseEntity<?> getProductList(){
        List<ProductDto> productList = productService.getAllProductsDto();
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable String  slug){
        ProductDto product = productService.getProductDtoBySlug(slug);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productList = productService.getProductDtoList();
        return ResponseEntity.ok(productList);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getList(@RequestParam Optional<Integer> page,
                                     @RequestParam Optional<String> sort,
                                     @RequestParam Optional<String> flavor,
                                     @RequestParam Optional<String> category,
                                     @RequestParam Optional<String> brand,
                                     @RequestParam Optional<String> origin,
                                     @RequestParam Optional<String> vendor
                                     ){
        Page<ProductDto> productList = productService.getAllProductsDtoPaging(page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }


    @GetMapping("/byInstantCoffee")
    public ResponseEntity<?> getInstantCoffee(@RequestParam Optional<Integer> page,
                                     @RequestParam Optional<String> sort,
                                     @RequestParam Optional<String> flavor,
                                     @RequestParam Optional<String> category,
                                     @RequestParam Optional<String> brand,
                                     @RequestParam Optional<String> origin,
                                     @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByCategoryPaging(1,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/byRoastedBeanCoffee")
    public ResponseEntity<?> getRoastedBeanCoffee(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<String> sort,
                                              @RequestParam Optional<String> flavor,
                                              @RequestParam Optional<String> category,
                                              @RequestParam Optional<String> brand,
                                              @RequestParam Optional<String> origin,
                                              @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByCategoryPaging(2,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/byCoffeeBall")
    public ResponseEntity<?> getCoffeeBall(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<String> sort,
                                              @RequestParam Optional<String> flavor,
                                              @RequestParam Optional<String> category,
                                              @RequestParam Optional<String> brand,
                                              @RequestParam Optional<String> origin,
                                              @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByCategoryPaging(3,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/byBottledCoffee")
    public ResponseEntity<?> getBottledCoffee(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<String> sort,
                                              @RequestParam Optional<String> flavor,
                                              @RequestParam Optional<String> category,
                                              @RequestParam Optional<String> brand,
                                              @RequestParam Optional<String> origin,
                                              @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByCategoryPaging(4,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/byIsSpecial")
    public ResponseEntity<?> getByIsSpecial(@RequestParam Optional<Integer> page,
                                              @RequestParam Optional<String> sort,
                                              @RequestParam Optional<String> flavor,
                                              @RequestParam Optional<String> category,
                                              @RequestParam Optional<String> brand,
                                              @RequestParam Optional<String> origin,
                                              @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByIsSpecialPaging(true,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/byIsLimited")
    public ResponseEntity<?> getByIsLimited(@RequestParam Optional<Integer> page,
                                            @RequestParam Optional<String> sort,
                                            @RequestParam Optional<String> flavor,
                                            @RequestParam Optional<String> category,
                                            @RequestParam Optional<String> brand,
                                            @RequestParam Optional<String> origin,
                                            @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByIsLimitedPaging(true,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/byContinent/{name}")
    public ResponseEntity<?> getByContinent(@PathVariable String name,
                                            @RequestParam Optional<Integer> page,
                                            @RequestParam Optional<String> sort,
                                            @RequestParam Optional<String> flavor,
                                            @RequestParam Optional<String> category,
                                            @RequestParam Optional<String> brand,
                                            @RequestParam Optional<String> origin,
                                            @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByOriginContinentPaging(name,
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/bestSeller")
    public ResponseEntity<?> getBestSeller(@RequestParam Optional<Integer> page,
                                            @RequestParam Optional<String> sort,
                                            @RequestParam Optional<String> flavor,
                                            @RequestParam Optional<String> category,
                                            @RequestParam Optional<String> brand,
                                            @RequestParam Optional<String> origin,
                                            @RequestParam Optional<String> vendor
    ){
        Page<ProductDto> productList = productService.getAllProductsDtoByBestSellerPaging(
                page.orElse(0), sort.orElse(""), flavor.orElse(""), category.orElse(""),
                brand.orElse(""), origin.orElse(""), vendor.orElse(""));
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/find/dto/{id}")
    public ResponseEntity<?> findDto(@PathVariable int id){
        ProductDto product = productService.getProductDtoById(id);
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
        System.out.println(testString);
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


    @GetMapping("/{id}/related/{flavorId}")
    public ResponseEntity<?> getTop10RelatedProducts(@PathVariable int id, @PathVariable int flavorId){
        return ResponseEntity.ok(productService.getTop10RelatedProductsByFlavor(id, flavorId));
    }

}
