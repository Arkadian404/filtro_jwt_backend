package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.repository.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;

    public void saveProductDetail(ProductDetail productDetail){
           productDetailRepository.save(productDetail);
    }

    public ProductDetail getProductDetailById(int id){
        return productDetailRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy chi tiết sản phẩm: "+ id));
    }

    public List<ProductDetail> getAllProductDetail(){
        if(productDetailRepository.findAll().isEmpty())
            throw new RuntimeException("Không có chi tiết sản phẩm nào");
        return productDetailRepository.findAll();
    }

    public List<ProductDetail> getProductDetailsByProductId(int id){
        if(productDetailRepository.findAllByProductId(id).isEmpty())
            throw new NotFoundException("Không có chi tiết sản phẩm nào");
        return productDetailRepository.findAllByProductId(id).orElseThrow(()-> new NotFoundException("Không có sản phẩm nào"));
    }



    public void updateProductDetail(int id, ProductDetail productDetail){
        ProductDetail productDetailUpdate = getProductDetailById(id);
        if(productDetail.getProduct() != null){
            productDetailUpdate.setProduct(productDetail.getProduct());
            productDetailUpdate.setPrice(productDetail.getPrice());
            productDetailUpdate.setQuantity(productDetail.getQuantity());
            productDetailUpdate.setWeight(productDetail.getWeight());
            productDetailUpdate.setStatus(productDetail.getStatus());
            productDetailRepository.save(productDetailUpdate);
        }else{
            throw new NotFoundException("Không tìm thấy sản phẩm: " + id);
        }
    }

    public void deleteProductDetail(int id){
        if(!productDetailRepository.existsById(id))
            throw new NotFoundException("Không tìm thấy chi tiết sản phẩm: "+ id);
        productDetailRepository.deleteById(id);
    }
}
