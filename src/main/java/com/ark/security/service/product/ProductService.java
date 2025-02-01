package com.ark.security.service.product;

import com.ark.security.dto.*;
import com.ark.security.dto.request.ProductRequest;
import com.ark.security.dto.response.ProductResponse;
import com.ark.security.exception.*;
import com.ark.security.mapper.ProductMapper;
import com.ark.security.models.product.Category;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.ark.security.repository.product.*;
import com.ark.security.service.CacheService;
import com.github.slugify.Slugify;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    BrandRepository brandRepository;
    FlavorRepository flavorRepository;
    CategoryRepository categoryRepository;
    ProductOriginRepository productOriginRepository;
    VendorRepository vendorRepository;
    ProductMapper productMapper;
    CacheService cacheService;

    static String PRODUCTS_KEY = "products:";


    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(int id){
        return productMapper.toProductResponse(
                productRepository
                        .findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    public List<ProductResponse> getProductsByName(String name){
        List<ProductResponse> list = productRepository.searchByName(name)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        return cacheService.getOrSetCacheList(
                PRODUCTS_KEY+"search:"+name,
                list,
                4,
                TimeUnit.HOURS
        );
    }

    public ProductResponse getProductBySlug(String slug){
        return productMapper.toProductResponse(
                productRepository.findBySlug(slug)
                        .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    public List<ProductResponse> getProductsByCategory(int id){
        List<ProductResponse> list = productRepository.findAllByCategoryId(id)
                                        .orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND))
                                        .stream()
                                        .map(productMapper::toProductResponse)
                                        .toList();
        return cacheService.getOrSetCacheList(
                PRODUCTS_KEY+":category:"+id, list, 4, TimeUnit.HOURS);
    }

    public List<ProductResponse> getProductsByOrigin(int id){
        List<ProductResponse> list = productRepository.findByOriginId(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_ORIGIN_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        return cacheService.getOrSetCacheList(
                PRODUCTS_KEY+":origin:"+id,
                list,
                4,
                TimeUnit.HOURS
        );
    }

    public List<ProductResponse> getProductsByFlavor(int id){
        List<ProductResponse> list = productRepository.findAllByFlavorId(id)
                .orElseThrow(()-> new AppException(ErrorCode.FLAVOR_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        return cacheService.getOrSetCacheList(
                PRODUCTS_KEY+":flavor:"+id,
                list,
                4,
                TimeUnit.HOURS
        );
    }

    public List<ProductResponse> getTop3LatestProducts(){
        return productRepository.findTop3LatestProducts()
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getTop3BestSellerProducts(){
        return productRepository.findTop3BestSellerProducts()
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getTop3SpecialProducts(){
        return productRepository.findTop3SpecialProducts()
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getColombiaTop10Products(){
        List<Product> list = productRepository.findByOriginName(2).orElse(List.of());
        return list.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getRoastedBeanTop10Products(){
        List<Product> list = productRepository.findTop10ProductsInSpecific(2).orElse(List.of());
        return list.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getBottledCoffeeTop10Products(){
        List<Product> list = productRepository.findTop10ProductsInSpecific(4).orElse(List.of());
        return list.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getTop10RelatedProductsByFlavor(int id, int flavorId){
        List<Product> list = productRepository.findTop10RelatedProductsByFlavor(id, flavorId).orElse(List.of());
        List<ProductResponse> responses = list.stream()
                                .map(productMapper::toProductResponse)
                                .toList();
        return cacheService.getOrSetCacheList(
                PRODUCTS_KEY+":relatedFlavor"+flavorId,
                responses,
                4,
                TimeUnit.HOURS
        );
    }

    public List<ProductResponse> getProductsByVendor(int id){
        List<ProductResponse> list = productRepository.findAllByVendorId(id)
                .orElseThrow(()-> new AppException(ErrorCode.VENDOR_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        return cacheService.getOrSetCacheList(
                PRODUCTS_KEY+":vendor:"+id,
                list,
                4,
                TimeUnit.HOURS
        );
    }


    public List<ProductResponse> getSpecialProducts(){
        return productRepository.findByIsSpecial(true)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }


    public List<ProductResponse> getLimitedProducts(){
        return productRepository.findByIsLimited(true)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse createProduct(ProductRequest request){
        Product product = productMapper.toProduct(request);
        passProductRefs(request, product);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setSold(0);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(int id, ProductRequest request){
        var product = productRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        passProductRefs(request, product);
        product.setUpdatedAt(LocalDateTime.now());
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public void deleteProduct(int id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.deleteById(product.getId());
    }

    private void passProductRefs(ProductRequest request, Product product) {
        int brandId = request.getBrandId();
        int flavorId = request.getFlavorId();
        int categoryId = request.getCategoryId();
        int productOriginId = request.getOriginId();
        int vendorId = request.getVendorId();
        product.setBrand(brandRepository.findById(brandId)
                .orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_FOUND)));
        product.setFlavor(flavorRepository.findById(flavorId)
                .orElseThrow(()-> new AppException(ErrorCode.FLAVOR_NOT_FOUND)));
        product.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_FOUND)));
        product.setOrigin(productOriginRepository.findById(productOriginId)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_ORIGIN_NOT_FOUND)));
        product.setVendor(vendorRepository.findById(vendorId)
                .orElseThrow(()-> new AppException(ErrorCode.VENDOR_NOT_FOUND)));
        Slugify slug = Slugify.builder().transliterator(true).build();
        product.setSlug(slug.slugify(product.getName()));
    }

    public PageResponse<ProductResponse> getProductsPaging(int page, ProductFilterRequest request){
        List<ProductResponse> products = productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
        return doPagination(page, products, request);
    }

    private PageResponse<ProductResponse> pagingByCategory(int id, int page, ProductFilterRequest request){
        List<Product> list = productRepository.findAllByCategoryId(id).orElse(List.of());
        List<ProductResponse> products = list.stream()
                .map(productMapper::toProductResponse)
                .toList();
        return doPagination(page, products, request);
    }

    public PageResponse<ProductResponse> getInstantCoffeePaging(int page, ProductFilterRequest request){
        return pagingByCategory(1, page, request);
    }

    public PageResponse<ProductResponse> getRoastedBeanCoffeePaging(int page, ProductFilterRequest request){
        return pagingByCategory(2, page, request);
    }

    public PageResponse<ProductResponse> getCoffeeBallPaging(int page, ProductFilterRequest request){
        return pagingByCategory(3, page, request);
    }

    public PageResponse<ProductResponse> getBottledCoffeePaging(int page, ProductFilterRequest request){
        return pagingByCategory(4, page, request);
    }


    public PageResponse<ProductResponse> getProductsPagingByContinent(String name, int page, ProductFilterRequest request){
        List<Product> list = productRepository.findByOriginContinent(name).orElse(List.of());
        List<ProductResponse> products = list.stream()
                .map(productMapper::toProductResponse)
                .toList();
        return doPagination(page, products, request);
    }

    public PageResponse<ProductResponse> getProductsPagingBySpecial(int page, ProductFilterRequest request){
        List<Product> list = productRepository.findByIsSpecial(true).orElse(List.of());
        List<ProductResponse> products = list.stream()
                .map(productMapper::toProductResponse)
                .toList();
        return doPagination(page, products, request);
    }

    public PageResponse<ProductResponse> getProductsPagingByLimited(int page, ProductFilterRequest request){
        List<Product> list = productRepository.findByIsLimited(true).orElse(List.of());
        List<ProductResponse> products = list.stream()
                .map(productMapper::toProductResponse)
                .toList();
        return doPagination(page, products, request);
    }

    public PageResponse<ProductResponse> getProductsPagingByBestSeller(int page, ProductFilterRequest request){
        List<Product> list = productRepository.findBestSellerProducts().orElse(List.of());
        List<ProductResponse> products = list.stream()
                .map(productMapper::toProductResponse)
                .toList();
        return doPagination(page, products, request);
    }

    private PageResponse<ProductResponse> doPagination(int page, List<ProductResponse> list ,ProductFilterRequest request){
        int pageSize = 12;
        request = request == null ? new ProductFilterRequest() : request;
        List<ProductResponse> filteredProducts = applyFilters(list, request);
        applySorting(filteredProducts, request.getSort());
        int totalElements = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        int start = Math.min((page - 1) * pageSize, totalElements);
        int end = Math.min(start + pageSize, totalElements);
        List<ProductResponse> data = filteredProducts.subList(start, end);
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .data(data)
                .build();
    }

    private List<ProductResponse> applyFilters(List<ProductResponse> list, ProductFilterRequest request){
        log.info("Request: {}", request.toString());
        Predicate<ProductResponse> flavorFilter = product -> request.getFlavor().isEmpty()
                || request.getFlavor().contains(product.getFlavor().getName());
        Predicate<ProductResponse> categoryFilter = product -> request.getCategory().isEmpty()
                || request.getCategory().contains(product.getCategory().getName());
        Predicate<ProductResponse> brandFilter = product -> request.getBrand().isEmpty()
                || request.getBrand().contains(product.getBrand().getName());
        Predicate<ProductResponse> originFilter = product -> request.getOrigin().isEmpty()
                || request.getOrigin().contains(product.getProductOrigin().getName());
        Predicate<ProductResponse> vendorFilter = product -> request.getVendor().isEmpty()
                || request.getVendor().contains(product.getVendor().getName());

        return list.stream()
                .filter(flavorFilter
                        .and(categoryFilter)
                        .and(brandFilter)
                        .and(originFilter)
                        .and(vendorFilter))
                .collect(Collectors.toList());

    }

    private void applySorting(List<ProductResponse> list, String sort){
        log.info("Sort: {}", sort);
        Comparator<ProductResponse> comparator = switch (sort){
            case "product_name_asc" -> Comparator.comparing(ProductResponse::getName);
            case "product_name_desc" -> Comparator.comparing(ProductResponse::getName).reversed();
            case "price_asc" -> Comparator.comparing(product-> product.getProductDetails().get(0).getPrice());
            case "price_desc" -> Comparator.comparing((ProductResponse product)-> product.getProductDetails().get(0).getPrice()).reversed();
            default -> null;
        };
        if(comparator != null){
            list.sort(comparator);

        }
    }

}


