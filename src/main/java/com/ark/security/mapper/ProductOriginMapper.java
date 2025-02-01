package com.ark.security.mapper;

import com.ark.security.dto.request.ProductOriginRequest;
import com.ark.security.dto.response.ProductOriginResponse;
import com.ark.security.models.product.ProductOrigin;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductOriginMapper {
    ProductOrigin toProductOrigin(ProductOriginRequest request);
    void updateProductOrigin(@MappingTarget ProductOrigin productOrigin, ProductOriginRequest request);
    ProductOriginResponse toProductOriginResponse(ProductOrigin productOrigin);
}
