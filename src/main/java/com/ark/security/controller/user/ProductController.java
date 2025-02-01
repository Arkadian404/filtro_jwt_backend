package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.PageResponse;
import com.ark.security.dto.ProductFilterRequest;
import com.ark.security.dto.request.ProductRequest;
import com.ark.security.dto.response.ProductResponse;
import com.ark.security.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productTestService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAll(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getAllProducts())
                .build();
    }

    @GetMapping("/page/all")
    public ApiResponse<PageResponse<ProductResponse>> getAllPaging(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getProductsPaging(page, request))
                .build();
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<ProductResponse> getBySlug(@PathVariable String slug){
        return ApiResponse.<ProductResponse>builder()
                .result(productTestService.getProductBySlug(slug))
                .build();
    }

    @GetMapping("/page/instantCoffee")
    public ApiResponse<PageResponse<ProductResponse>> getInstantCoffee(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getInstantCoffeePaging(page, request))
                .build();
    }

    @GetMapping("/page/roastedBeanCoffee")
    public ApiResponse<PageResponse<ProductResponse>> getRoastedCoffee(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getRoastedBeanCoffeePaging(page, request))
                .build();
    }

    @GetMapping("/page/coffeeBall")
    public ApiResponse<PageResponse<ProductResponse>> getCoffeeBall(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getCoffeeBallPaging(page, request))
                .build();
    }

    @GetMapping("/page/bottledCoffee")
    public ApiResponse<PageResponse<ProductResponse>> getBottledCoffee(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getBottledCoffeePaging(page, request))
                .build();
    }

    @GetMapping("/page/special")
    public ApiResponse<PageResponse<ProductResponse>> getSpecial(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getProductsPagingBySpecial(page, request))
                .build();
    }

    @GetMapping("/page/limited")
    public ApiResponse<PageResponse<ProductResponse>> getLimited(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getProductsPagingByLimited(page, request))
                .build();
    }

    @GetMapping("/page/continent/{name}")
    public ApiResponse<PageResponse<ProductResponse>> getByContinent(
            @PathVariable String name,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getProductsPagingByContinent(name, page, request))
                .build();
    }

    @GetMapping("/page/bestSeller")
    public ApiResponse<PageResponse<ProductResponse>> getBestSeller(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            ProductFilterRequest request
    ){
        return ApiResponse.<PageResponse<ProductResponse>>builder()
                .result(productTestService.getProductsPagingByBestSeller(page, request))
                .build();
    }

    @GetMapping("/category/{id}")
    public ApiResponse<List<ProductResponse>> getByCategory(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getProductsByCategory(id))
                .build();
    }

    @GetMapping("/flavor/{id}")
    public ApiResponse<List<ProductResponse>> getByFlavor(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getProductsByFlavor(id))
                .build();
    }

    @GetMapping("/origin/{id}")
    public ApiResponse<List<ProductResponse>> getByOrigin(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getProductsByOrigin(id))
                .build();
    }

    @GetMapping("/special")
    public ApiResponse<List<ProductResponse>> getSpecial(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getSpecialProducts())
                .build();
    }

    @GetMapping("/limited")
    public ApiResponse<List<ProductResponse>> getLimited(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getLimitedProducts())
                .build();
    }

    @GetMapping("/vendor/{id}")
    public ApiResponse<List<ProductResponse>> getByVendor(@PathVariable int id){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getProductsByVendor(id))
                .build();
    }

    @GetMapping("/top/3LatestProducts")
    public ApiResponse<List<ProductResponse>> getTop3LatestProducts(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getTop3LatestProducts())
                .build();
    }

    @GetMapping("/top/3BestSeller")
    public ApiResponse<List<ProductResponse>> getTop3BestSeller(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getTop3BestSellerProducts())
                .build();
    }

    @GetMapping("/top/3Special")
    public ApiResponse<List<ProductResponse>> getTop3Special(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getTop3SpecialProducts())
                .build();
    }

    @GetMapping("/top/10Colombia")
    public ApiResponse<List<ProductResponse>> getTop10Colombia(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getColombiaTop10Products())
                .build();
    }

    @GetMapping("/top/10RoastedBean")
    public ApiResponse<List<ProductResponse>> getTop10RoastedBean(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getRoastedBeanTop10Products())
                .build();
    }

    @GetMapping("/top/10BottledCoffee")
    public ApiResponse<List<ProductResponse>> getTop10BottledCoffee(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getBottledCoffeeTop10Products())
                .build();
    }

    @GetMapping("{id}/related/{flavorId}")
    public ApiResponse<List<ProductResponse>> getRelatedProducts(@PathVariable int id, @PathVariable int flavorId){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productTestService.getTop10RelatedProductsByFlavor(id, flavorId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable int id){
        return ApiResponse.<ProductResponse>builder()
                .result(productTestService.getProductById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ProductResponse> create(@RequestBody @Valid ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productTestService.createProduct(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable int id, @RequestBody @Valid ProductRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productTestService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable int id){
        productTestService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .result("Product deleted successfully")
                .build();
    }

}
