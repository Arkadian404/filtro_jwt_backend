package com.ark.security.mapper;

import com.ark.security.dto.request.VendorRequest;
import com.ark.security.dto.response.VendorResponse;
import com.ark.security.models.product.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    Vendor toVendor(VendorRequest vendorRequest);
    void updateVendor(@MappingTarget Vendor vendor, VendorRequest vendorRequest);
    VendorResponse toVendorResponse(Vendor vendor);
}
