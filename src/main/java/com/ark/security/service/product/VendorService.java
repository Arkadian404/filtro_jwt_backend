package com.ark.security.service.product;

import com.ark.security.dto.VendorDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Vendor;
import com.ark.security.repository.product.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final String NOT_FOUND = "Không tìm thấy nhà cung cấp: ";
    private final String EMPTY = "Không có nhà cung cấp nào";

    public void saveVendor(Vendor vendor){
        if (vendorRepository.existsVendorByName(vendor.getName()))
            throw new DuplicateException("Nhà cung cấp đã tồn tại");
        vendorRepository.save(vendor);
    }

    public List<Vendor> getAllVendor(){
        if(vendorRepository.findAll().isEmpty()) {
            throw new NotFoundException(EMPTY);
        }
        return vendorRepository.findAll();
    }

    public List<VendorDto> getAllVendorDto() {
        List<VendorDto> vendors = vendorRepository.findAll()
                .stream()
                .map(Vendor::convertToDto)
                .toList();
        if(vendors.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return vendors;
    }

    public Vendor getVendorById(int id){
        return vendorRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }

    public void updateVendor(int id, Vendor vendor){
        Vendor vendorUpdate = getVendorById(id);
        vendorUpdate.setName(vendor.getName());
        vendorUpdate.setAddress(vendor.getAddress());
        vendorUpdate.setPhone(vendor.getPhone());
        vendorUpdate.setEmail(vendor.getEmail());
        vendorUpdate.setDescription(vendor.getDescription());
        vendorUpdate.setStatus(vendor.getStatus());
        vendorRepository.save(vendorUpdate);
    }

    public void deleteVendor(int id){
        if(!vendorRepository.existsById(id))
            throw new NotFoundException(NOT_FOUND+ id);
        vendorRepository.deleteById(id);
    }

}
