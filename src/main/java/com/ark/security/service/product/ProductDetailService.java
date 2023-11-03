package com.ark.security.service.product;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;
    private final String NOT_FOUND = "Không tìm thấy chi tiết sản phẩm: ";
    private final String EMPTY = "Không có chi tiết sản phẩm nào";


    public void saveProductDetail(ProductDetail productDetail){
           productDetailRepository.save(productDetail);
    }

    public ProductDetail getProductDetailById(int id){
        return productDetailRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }

    public List<ProductDetail> getAllProductDetail(){
        if(productDetailRepository.findAll().isEmpty())
            throw new NotFoundException(EMPTY);
        return productDetailRepository.findAll();
    }

    public List<ProductDetail> getProductDetailsByProductId(int id){
        if(productDetailRepository.findAllByProductId(id).isEmpty())
            throw new NotFoundException(EMPTY);
        return productDetailRepository.findAllByProductId(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }



    public void updateProductDetail(int id, ProductDetail productDetail){
        ProductDetail productDetailUpdate = getProductDetailById(id);
        if(productDetail.getProduct() != null){
            productDetailUpdate.setProduct(productDetail.getProduct());
            productDetailUpdate.setPrice(productDetail.getPrice());
            productDetailUpdate.setStock(productDetail.getStock());
            productDetailUpdate.setWeight(productDetail.getWeight());
            productDetailUpdate.setStatus(productDetail.getStatus());
            productDetailRepository.save(productDetailUpdate);
        }else{
            throw new NotFoundException(NOT_FOUND + id);
        }
    }

    public void deleteProductDetail(int id){
        if(!productDetailRepository.existsById(id))
            throw new NotFoundException(NOT_FOUND+ id);
        productDetailRepository.deleteById(id);
    }
}
