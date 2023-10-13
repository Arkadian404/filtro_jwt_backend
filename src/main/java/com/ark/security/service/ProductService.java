package com.ark.security.service;

import com.ark.security.dto.ProductDetailDto;
import com.ark.security.dto.ProductDto;
import com.ark.security.dto.ProductImageDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.product.Category;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.ark.security.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final ProductDetailService productDetailService;
    private final String PRODUCT_NOT_FOUND = "Không tìm thấy sản phẩm: ";
    private final String PRODUCT_EMPTY = "Không có sản phẩm nào";
    private final String PRODUCT_NAME_EXISTS = "Sản phẩm đã tồn tại";
    private final String NON_BLANK = "Không được để trống";


    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
    }


    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<ProductDto> getAllProductsDto(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }
    Page<ProductDto> doPagination(List<ProductDto> list, int page, String sort, String flavor,
                                  String category, String brand, String origin, String vendor){
        Pageable pageable = PageRequest.of(page, 12);
//        if(
//                (!flavor.isEmpty() && !category.isEmpty())||
//                (!category.isEmpty() && !brand.isEmpty())||
//                (!brand.isEmpty() && !origin.isEmpty())||
//                (!origin.isEmpty() && !vendor.isEmpty())
//        ){
//            list = list.stream()
//                    .filter(product ->
//                            (flavor.contains(product.getFlavor().getName()) && category.contains(product.getCategory().getName()))||
//                            (category.contains(product.getCategory().getName()) && brand.contains(product.getBrand().getName()))||
//                            (brand.contains(product.getBrand().getName()) && origin.contains(product.getOrigin().getName()))||
//                            (origin.contains(product.getOrigin().getName()) && vendor.contains(product.getVendor().getName()))||
//                            (flavor.contains(product.getFlavor().getName()) && category.contains(product.getCategory().getName()) && brand.contains(product.getBrand().getName()))||
//                            (category.contains(product.getCategory().getName()) && brand.contains(product.getBrand().getName()) && origin.contains(product.getOrigin().getName()))||
//                            (brand.contains(product.getBrand().getName()) && origin.contains(product.getOrigin().getName()) && vendor.contains(product.getVendor().getName()))||
//                            (flavor.contains(product.getFlavor().getName()) && category.contains(product.getCategory().getName()) && brand.contains(product.getBrand().getName()) && origin.contains(product.getOrigin().getName())) ||
//                            (category.contains(product.getCategory().getName()) && brand.contains(product.getBrand().getName()) && origin.contains(product.getOrigin().getName()) && vendor.contains(product.getVendor().getName()))||
//                            (flavor.contains(product.getFlavor().getName() )&& category.contains(product.getCategory().getName()) && brand.contains(product.getBrand().getName()) && origin.contains(product.getOrigin().getName()) && vendor.contains(product.getVendor().getName()))
//                    ).collect(Collectors.toList());
//        }else if(!flavor.isEmpty() || !category.isEmpty() || !brand.isEmpty() || !origin.isEmpty() || !vendor.isEmpty()){
//            list = list.stream()
//                    .filter(product ->
//                            flavor.contains(product.getFlavor().getName())||
//                                    category.contains(product.getCategory().getName())||
//                                    brand.contains(product.getBrand().getName())||
//                                    origin.contains(product.getOrigin().getName())||
//                                    vendor.contains(product.getVendor().getName())
//                    ).collect(Collectors.toList());
//        }
        List<Predicate<ProductDto>> filters = new ArrayList<>();
        if(!flavor.isEmpty()){
            filters.add(product -> flavor.contains(product.getFlavor().getName()));
        }
        if(!category.isEmpty()){
            filters.add(product -> category.contains(product.getCategory().getName()));
        }
        if(!brand.isEmpty()){
            filters.add(product -> brand.contains(product.getBrand().getName()));
        }
        if(!origin.isEmpty()){
            filters.add(product -> origin.contains(product.getOrigin().getName()));
        }
        if(!vendor.isEmpty()){
            filters.add(product -> vendor.contains(product.getVendor().getName()));
        }

        Predicate<ProductDto> filter = filters.stream().reduce(x -> true, Predicate::and);
        list = list.stream().filter(filter).collect(Collectors.toList());
        switch (sort){
            case "product_name_asc":
                list.sort(Comparator.comparing(ProductDto::getName));
                break;
            case "product_name_desc":
                list.sort(Comparator.comparing(ProductDto::getName).reversed());
                break;
            case "price_asc":
                list.sort(Comparator.comparing(product -> product.getProductDetails().get(0).getPrice()));
                break;
            case "price_desc":
                Comparator<ProductDto> comparator =  Comparator.comparing(product -> product.getProductDetails().get(0).getPrice());
                list.sort(comparator.reversed());
                break;
            default:
                break;
        }
        if(list.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return convertListDtoToPage(list, pageable);
    }

    public Page<ProductDto> getAllProductsDtoPaging(int page, String sort, String flavor, String category,
                                                    String brand, String origin, String vendor
                                                    ){
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        List<ProductDto> list = convertProductToDto(products);
        return doPagination(list, page, sort, flavor, category, brand, origin, vendor);
    }

    public Page<ProductDto> getAllProductsDtoByCategoryPaging(int id, int page, String sort, String flavor, String category,
                                                              String brand, String origin, String vendor){
        List<Product> products = getAllProductsByCategory(id);
        List<ProductDto> list = convertProductToDto(products);
        return doPagination(list, page, sort, flavor, category, brand, origin, vendor);
    }

    public Page<ProductDto> getAllProductsDtoByOriginContinentPaging(String name,int page, String sort, String flavor, String category,
                                                                     String brand, String origin, String vendor ){
        List<Product> products = productRepository.findByOriginContinent(name).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        List<ProductDto> list = convertProductToDto(products);
        return doPagination(list, page, sort, flavor, category, brand, origin, vendor);
    }

    public Page<ProductDto> getAllProductsDtoByIsSpecialPaging(Boolean isSpecial, int page, String sort, String flavor, String category,
                                                               String brand, String origin, String vendor){
        List<Product> products = productRepository.findByIsSpecial(isSpecial).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        List<ProductDto> list = convertProductToDto(products);
        return doPagination(list, page, sort, flavor, category, brand, origin, vendor);
    }

    public Page<ProductDto> getAllProductsDtoByIsLimitedPaging(Boolean isLimited, int page, String sort, String flavor, String category,
                                                               String brand, String origin, String vendor){
        List<Product> products = productRepository.findByIsLimited(isLimited).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        List<ProductDto> list = convertProductToDto(products);
        return doPagination(list, page, sort, flavor, category, brand, origin, vendor);
    }

    public Page<ProductDto> getAllProductsDtoByBestSellerPaging(int page, String sort, String flavor, String category,
                                                                String brand, String origin, String vendor){
        List<Product> products = productRepository.findBestSellerProducts().orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        List<ProductDto> list = convertProductToDto(products);
        return doPagination(list, page, sort, flavor, category, brand, origin, vendor);
    }

    public Category getCategoryByProductId(int id){
        return productRepository.findCategoryUsingId(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
    }



    public List<Product> getAllProductsByCategory(int id){
        List<Product> products =productRepository.findAllByCategoryId(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<Product> getProductsByName(String name){
        List<Product> products = productRepository.searchByName(name).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ name));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<ProductDto> getProductDtoByName(String name){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.searchByName(name)
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ name));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }

    public List<Product> getProductsBySale(int id){
        List<Product> products = productRepository.findAllBySaleId(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<Product> getProductsByOrigin(int id){
        List<Product> products = productRepository.findByOriginId(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<Product> getProductsByFlavor(int id){
        List<Product> products = productRepository.findAllByFlavorId(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<Product> getProductsByIsSpecial(Boolean isSpecial){
        List<Product> products = productRepository.findByIsSpecial(isSpecial).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }


    public List<Product> getProductsByIsLimited(Boolean isLimited){
        List<Product> products = productRepository.findByIsSpecial(isLimited).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }

    public List<Product> getProductsByVendor(int id){
        List<Product> products = productRepository.findAllByVendorId(id).orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND+ id));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        return products;
    }


    public List<ProductDto> getTop3LatestProducts(){
        List<ProductDto> productDtos; //create ProductDto list
        List<Product> products = productRepository.findTop3LatestProducts()
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND)); //get top 3 latest products
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products); //convert product list to product dto list
        return productDtos;//return product dto list
    }

    public List<ProductDto> getTop3BestSellerProducts(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findTop3BestSellerProducts()
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }

    public List<ProductDto> getTop3SpecialProducts(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findTop3SpecialProducts()
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }

    public List<ProductDto> getTop10ProductsInColombia(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findByOriginName(2)
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }

    public List<ProductDto> getTop10ProductsByRoastedCoffeeBeans(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findTop10ProductsInSpecific(2)
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }


    public List<ProductDto> getTop10ProductsByBottledCoffee(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findTop10ProductsInSpecific(4)
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }

    public List<ProductDto> getProductsFromAmericas(){
        List<ProductDto> productDtos;
        List<Product> products = productRepository.findByOriginName(1)
                .orElseThrow(()-> new NotFoundException(PRODUCT_NOT_FOUND));
        if(products.isEmpty()){
            throw new NotFoundException(PRODUCT_EMPTY);
        }
        productDtos = convertProductToDto(products);
        return productDtos;
    }


    public boolean isExistsProductByName(String name){
        return productRepository.existsProductByName(name);
    }

    public void saveProduct(Product product) {
        if(isExistsProductByName(product.getName())) {
            throw new NotFoundException(PRODUCT_NAME_EXISTS);
        }
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void updateProduct(int id, Product product){
        Product oldProduct = getProductById(id);
        if(product == null){
            throw new NullException(NON_BLANK);
        }
        if (oldProduct != null) {
            oldProduct.setName(product.getName());
            oldProduct.setBrand(product.getBrand());
            oldProduct.setCategory(product.getCategory());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setSale(product.getSale());
            oldProduct.setFlavor(product.getFlavor());
            oldProduct.setUpdatedAt(LocalDateTime.now());
            oldProduct.setIsSpecial(product.getIsSpecial());
            oldProduct.setIsLimited(product.getIsLimited());
            oldProduct.setOrigin(product.getOrigin());
            oldProduct.setVendor(product.getVendor());
            oldProduct.setStatus(product.getStatus());
            productRepository.save(oldProduct);
        }else {
            throw new NotFoundException(PRODUCT_NOT_FOUND+ id);
        }
    }

    public void deleteProduct(int id) {
        Product product = getProductById(id);
        if(product == null){
            throw new NotFoundException(PRODUCT_NOT_FOUND+ id);
        }
        productRepository.deleteById(id);
    }


    public List<ProductDto> convertProductToDto(List<Product> products){
        List<ProductDto> productDtos = new ArrayList<>();
        products.forEach(product ->{
            List<ProductImageDto> productImageDtos = new ArrayList<>();
            List<ProductDetailDto> productDetailDtos = new ArrayList<>();
            List<ProductImage> productImages = productImageService.getProductImagesByProductId(product.getId());
            productImages.forEach(productImage -> productImageDtos.add(productImage.convertToDto()));
            List<ProductDetail> productDetails = productDetailService.getProductDetailsByProductId(product.getId());
            productDetails.forEach(productDetail -> productDetailDtos.add(productDetail.convertToDto()));
            ProductDto productDto = ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand() == null ? null : product.getBrand().convertToDto())
                    .description(product.getDescription())
                    .images(productImageDtos)
                    .productDetails(productDetailDtos)
                    .category(product.getCategory() == null ? null : product.getCategory().convertToDto())
                    .flavor(product.getFlavor() == null ? null : product.getFlavor().convertToDto())
                    .sale(product.getSale() == null ? null : product.getSale().convertToDto())
                    .origin(product.getOrigin() == null ? null : product.getOrigin().convertToDto())
                    .vendor(product.getVendor() == null ? null : product.getVendor().convertToDto())
                    .build();
            productDtos.add(productDto);
        });
        return productDtos;
    }


    public Page<ProductDto> convertListDtoToPage(List<ProductDto> products, Pageable pageable){
        int totalPage = products.size() / pageable.getPageSize();
        if(pageable.getPageNumber() < 0){
            pageable = PageRequest.of(0, 12);
        } else if(pageable.getPageNumber() >= totalPage) {
            pageable = PageRequest.of(totalPage, 12);
        }
       int start = (int) pageable.getOffset(); //lay offset 0,12,24,36,...
       int end = Math.min((start + pageable.getPageSize()), products.size()); //lay phan tu luon = 12
       List<ProductDto> sublist = products.subList(start, end);//cat danh sach
       return new PageImpl<>(sublist, pageable, products.size()); // cai lol nay khong cho sort ?? deo hieu!
    }

}


