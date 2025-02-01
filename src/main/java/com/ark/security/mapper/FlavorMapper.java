package com.ark.security.mapper;

import com.ark.security.dto.request.FlavorRequest;
import com.ark.security.dto.response.FlavorResponse;
import com.ark.security.models.product.Flavor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FlavorMapper {
    Flavor toFlavor(FlavorRequest request);
    void updateFlavor(@MappingTarget Flavor brand, FlavorRequest request);
    FlavorResponse toFlavorResponse(Flavor brand);
}
