package com.ark.security.mapper;

import com.ark.security.dto.request.VoucherRequest;
import com.ark.security.dto.response.VoucherResponse;
import com.ark.security.models.Voucher;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherRequest request);
    void updateVoucher(@MappingTarget Voucher voucher, VoucherRequest request);
    VoucherResponse toVoucherResponse(Voucher voucher);
}
