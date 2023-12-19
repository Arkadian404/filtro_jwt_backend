package com.ark.security.service.product;


import com.ark.security.dto.BrandDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Brand;
import com.ark.security.repository.product.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<BrandDto> getAllBrandsDto(){
        List<BrandDto> brands = brandRepository.findAll()
                .stream()
                .map(Brand::convertToDto)
                .collect(Collectors.toList());
        if(brands.isEmpty()){
            throw new NotFoundException("Không tìm thấy thương hiệu nào");
        }
        return brands;
    }

    public Brand getBrandById(int id){
        return brandRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy thương hiệu có id = " + id));
    }



    public void saveBrand(Brand brand){
        //check if duplicate
        if(brandRepository.existsBrandByName(brand.getName())){
            throw new DuplicateException("Thương hiệu đã tồn tại");
        }
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
