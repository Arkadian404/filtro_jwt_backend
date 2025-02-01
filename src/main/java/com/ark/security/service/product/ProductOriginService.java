package com.ark.security.service.product;

import com.ark.security.dto.request.ProductOriginRequest;
import com.ark.security.dto.response.ProductOriginResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.ProductOriginMapper;
import com.ark.security.models.product.ProductOrigin;
import com.ark.security.repository.product.ProductOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOriginService {
    private final ProductOriginRepository productOriginRepository;
    private final ProductOriginMapper productOriginMapper;

    public List<ProductOriginResponse> getAllProductOrigins(){
        return productOriginRepository.findAll()
                .stream()
                .map(productOriginMapper::toProductOriginResponse)
                .toList();
    }

    public ProductOriginResponse getProductOriginById(int id){
        return productOriginMapper.toProductOriginResponse(
                productOriginRepository
                        .findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_ORIGIN_NOT_FOUND)));
    }

    public ProductOriginResponse createProductOrigin(ProductOriginRequest productOriginRequest){
        ProductOrigin productOrigin = productOriginMapper.toProductOrigin(productOriginRequest);
        return productOriginMapper.toProductOriginResponse(productOriginRepository.save(productOrigin));
    }

    public ProductOriginResponse updateProductOrigin(int id, ProductOriginRequest productOriginRequest){
        var productOrigin = productOriginRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_ORIGIN_NOT_FOUND));
        productOriginMapper.updateProductOrigin(productOrigin, productOriginRequest);
        return productOriginMapper.toProductOriginResponse(productOriginRepository.save(productOrigin));
    }

    public void deleteProductOrigin(int id){
        ProductOrigin productOrigin = productOriginRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_ORIGIN_NOT_FOUND));
        productOriginRepository.deleteById(productOrigin.getId());
    }

    public List<ProductOriginResponse> getByContinent(String name){
        return productOriginRepository
                .findByContinent(name)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ORIGIN_NOT_FOUND))
                .stream()
                .map(productOriginMapper::toProductOriginResponse)
                .toList();

    }

}
