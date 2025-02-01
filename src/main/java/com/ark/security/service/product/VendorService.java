package com.ark.security.service.product;

import com.ark.security.dto.request.VendorRequest;
import com.ark.security.dto.response.VendorResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.VendorMapper;
import com.ark.security.models.product.Vendor;
import com.ark.security.repository.product.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public List<VendorResponse> getAllVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(vendorMapper::toVendorResponse)
                .toList();
    }

    public VendorResponse getVendorById(int id) {
        return vendorMapper.toVendorResponse(
                vendorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_FOUND))
        );
    }

    public VendorResponse createVendor(VendorRequest request) {
        Vendor vendor = vendorMapper.toVendor(request);
        return vendorMapper.toVendorResponse(vendorRepository.save(vendor));
    }

    public VendorResponse updateVendor(int id, VendorRequest request) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_FOUND));
        vendorMapper.updateVendor(vendor, request);
        return vendorMapper.toVendorResponse(vendorRepository.save(vendor));
    }

    public void deleteVendor(int id) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_FOUND));
        vendorRepository.delete(vendor);
    }

}
