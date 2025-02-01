package com.ark.security.mapper;

import com.ark.security.dto.request.ProductDetailRequest;
import com.ark.security.dto.response.ProductDetailResponse;
import com.ark.security.models.product.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetail toProductDetail(ProductDetailRequest productDetailRequest);
    void updateProductDetail(@MappingTarget ProductDetail productDetail, ProductDetailRequest productDetailRequest);
    @Mapping(source = "productDetail.product.id", target = "productId")
    @Mapping(source = "productDetail.product.category.id", target = "categoryId")
    @Mapping(source = "productDetail.product.name", target = "productName")
    ProductDetailResponse toProductDetailResponse(ProductDetail productDetail);
}
