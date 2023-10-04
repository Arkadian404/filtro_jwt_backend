package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Vendor;
import com.ark.security.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;

    public void saveVendor(Vendor vendor){
        vendorRepository.save(vendor);
    }

    public List<Vendor> getAllVendor(){
        if(vendorRepository.findAll().isEmpty()) {
            throw new NotFoundException("Không có nhà cung cấp nào");
        }
        return vendorRepository.findAll();
    }

    public Vendor getVendorById(int id){
        return vendorRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy nhà cung cấp: "+ id));
    }

    public void updateVendor(int id, Vendor vendor){
        Vendor vendorUpdate = getVendorById(id);
        vendorUpdate.setName(vendor.getName());
        vendorUpdate.setAddress(vendor.getAddress());
        vendorUpdate.setPhone(vendor.getPhone());
        vendorUpdate.setEmail(vendor.getEmail());
        vendorUpdate.setDescription(vendor.getDescription());
        vendorRepository.save(vendorUpdate);
    }

    public void deleteVendor(int id){
        if(!vendorRepository.existsById(id))
            throw new NotFoundException("Không tìm thấy nhà cung cấp: "+ id);
        vendorRepository.deleteById(id);
    }

}
