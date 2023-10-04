package com.ark.security.service;


import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Brand;
import com.ark.security.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> getAllBrands(){
        List<Brand> brands = brandRepository.findAll();
        if(brands.isEmpty()){
            throw new NotFoundException("Không tìm thấy thương hiệu nào");
        }
        return brands;
    }

    public Brand getBrandById(int id){
        return brandRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy thương hiệu có id = " + id));
    }



    public void saveBrand(Brand brand){
        brandRepository.save(brand);
    }

    public void updateBrand(int id, Brand brand){
        Brand oldBrand = getBrandById(id);
        oldBrand.setName(brand.getName());
        oldBrand.setDescription(brand.getDescription());
        oldBrand.setStatus(brand.getStatus());
        oldBrand.setProducts(brand.getProducts());
        brandRepository.save(oldBrand);
    }

    public void deleteBrand(int id){

        brandRepository.deleteById(id);
    }


}
