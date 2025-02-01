package com.ark.security.mapper;

import com.ark.security.dto.request.BrandRequest;
import com.ark.security.dto.response.BrandResponse;
import com.ark.security.models.product.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest request);
    void updateBrand(@MappingTarget Brand brand, BrandRequest request);
    BrandResponse toBrandResponse(Brand brand);
}
