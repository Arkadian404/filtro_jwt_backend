package com.ark.security.service.product;

import com.ark.security.dto.request.ProductImageRequest;
import com.ark.security.dto.response.ProductImageResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.exception.NotFoundException;
import com.ark.security.mapper.ProductImageMapper;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.ProductImage;
import com.ark.security.repository.product.ProductImageRepository;
import com.ark.security.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    public List<ProductImageResponse> getAllProductImages(){
        return productImageRepository.findAll()
                .stream()
                .map(productImageMapper::toProductImageResponse)
                .toList();
    }

    public List<ProductImageResponse> getAllByProductId(int id){
        return productImageRepository.findAllByProductId(id)
                .stream()
                .map(productImageMapper::toProductImageResponse)
                .toList();
    }

    public ProductImageResponse getProductImageById(int id){
        return productImageMapper.toProductImageResponse(
                productImageRepository
                        .findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND)));
    }

    public ProductImageResponse createProductImage(ProductImageRequest productImageRequest){
        ProductImage productImage = productImageMapper.toProductImage(productImageRequest);
        int productId = productImageRequest.getProductId();
        productImage.setProduct(productRepository.findById(productId)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        productImage.setCreatedAt(LocalDateTime.now());
        productImage.setUpdatedAt(LocalDateTime.now());
        return productImageMapper
                .toProductImageResponse(productImageRepository.save(productImage));

    }

    public ProductImageResponse updateProductImage(int id, ProductImageRequest productImageRequest){
        var productImage = productImageRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND));
        productImageMapper.updateProductImage(productImage, productImageRequest);
        int productId = productImageRequest.getProductId();
        productImage.setProduct(productRepository.findById(productId)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        return productImageMapper
                .toProductImageResponse(productImageRepository.save(productImage));
    }

    public void deleteProductImage(int id){
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_FOUND));
        productImageRepository.deleteById(productImage.getId());
    }
}
