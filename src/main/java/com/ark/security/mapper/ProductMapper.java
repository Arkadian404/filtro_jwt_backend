package com.ark.security.mapper;

import com.ark.security.dto.request.ProductRequest;
import com.ark.security.dto.response.ProductResponse;
import com.ark.security.models.product.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "productRequest.brandId", ignore = true)
    @Mapping(target = "productRequest.flavorId", ignore = true)
    @Mapping(target = "productRequest.categoryId", ignore = true)
    @Mapping(target = "productRequest.productOriginId", ignore = true)
    @Mapping(target = "productRequest.vendorId", ignore = true)
    Product toProduct(ProductRequest productRequest);

    void updateProduct(@MappingTarget Product product, ProductRequest productRequest);

    @Mapping(source = "origin", target = "productOrigin")
    ProductResponse toProductResponse(Product product);

    @AfterMapping
    default void setProductId(@MappingTarget ProductResponse productResponse, Product product){
        int productId = product.getId();
        if(productResponse.getProductDetails() != null){
            productResponse.getProductDetails().forEach(pd-> pd.setProductId(productId));
        }
        if(productResponse.getImages() != null){
            productResponse.getImages().forEach(img-> img.setProductId(productId));
        }
    }
}
