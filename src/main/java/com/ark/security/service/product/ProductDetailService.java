package com.ark.security.service.product;

import com.ark.security.dto.request.ProductDetailRequest;
import com.ark.security.dto.response.ProductDetailResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.exception.NotFoundException;
import com.ark.security.mapper.ProductDetailMapper;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.repository.product.ProductDetailRepository;
import com.ark.security.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final ProductDetailMapper productDetailMapper;


    public List<ProductDetailResponse> getAllProductDetails(){
        return productDetailRepository.findAll()
                .stream()
                .map(productDetailMapper::toProductDetailResponse)
                .toList();
    }
    public List<ProductDetailResponse> getAllByProductId(int id){
        return productDetailRepository.findAllByProductId(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND))
                .stream()
                .map(productDetailMapper::toProductDetailResponse)
                .toList();
    }

    public ProductDetailResponse getProductDetailById(int id){
        return productDetailMapper.toProductDetailResponse(
                productDetailRepository
                        .findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND)));
    }

    public ProductDetailResponse createProductDetail(ProductDetailRequest request){
        ProductDetail productDetail = productDetailMapper.toProductDetail(request);
        int productId = request.getProductId();
        productDetail.setProduct(productRepository.findById(productId)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        return productDetailMapper
                .toProductDetailResponse(productDetailRepository.save(productDetail));
    }

    public ProductDetailResponse updateProductDetail(int id, ProductDetailRequest request){
        var productDetail = productDetailRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));
        productDetailMapper.updateProductDetail(productDetail, request);
        int productId = request.getProductId();
        productDetail.setProduct(productRepository.findById(productId)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        return productDetailMapper
                .toProductDetailResponse(productDetailRepository.save(productDetail));
    }

    public void deleteProductDetail(int id){
        ProductDetail productDetail = productDetailRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));
        productDetailRepository.deleteById(productDetail.getId());
    }
}
