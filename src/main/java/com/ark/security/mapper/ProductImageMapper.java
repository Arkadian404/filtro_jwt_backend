package com.ark.security.mapper;

import com.ark.security.dto.request.ProductImageRequest;
import com.ark.security.dto.response.ProductImageResponse;
import com.ark.security.models.product.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImage toProductImage(ProductImageRequest request);
    void updateProductImage(@MappingTarget ProductImage productImage, ProductImageRequest request);
    @Mapping(source = "productImage.product.id", target = "productId")
    @Mapping(source = "productImage.product.name", target = "productName")
    @Mapping(source = "productImage.product.category.id", target = "categoryId")
    ProductImageResponse toProductImageResponse(ProductImage productImage);
}
