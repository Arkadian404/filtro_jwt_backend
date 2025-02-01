package com.ark.security.service.product;


import com.ark.security.dto.request.BrandRequest;
import com.ark.security.dto.response.BrandResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.BrandMapper;
import com.ark.security.models.product.Brand;
import com.ark.security.repository.product.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public List<BrandResponse> getAllBrands(){
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toBrandResponse)
                .toList();
    }


    public BrandResponse getBrandById(int id){
        return brandMapper.toBrandResponse(
                brandRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_FOUND))
        );
    }



    public BrandResponse saveBrand(BrandRequest request){
        Brand brand = brandMapper.toBrand(request);
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    public BrandResponse updateBrand(int id, BrandRequest request){
        Brand brand = brandRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_FOUND));
        brandMapper.updateBrand(brand, request);
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }

    public void deleteBrand(int id){
        Brand brand = brandRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_FOUND));
        brandRepository.delete(brand);
    }


}
