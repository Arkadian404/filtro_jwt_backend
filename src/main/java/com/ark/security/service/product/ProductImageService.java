package com.ark.security.service.product;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.ProductImage;
import com.ark.security.repository.product.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final String NOT_FOUND = "Không tìm thấy ảnh: ";
    private final String EMPTY = "Không có ảnh nào";
    public List<ProductImage> getAllProductImages(){
        List<ProductImage> list =  productImageRepository.findAll();
        if(list.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return list;
    }

    public List<ProductImage> getProductImagesByProductId(int id){
        List<ProductImage> list =  productImageRepository.findAllByProductId(id);
        if(list.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return list;
    }

    public ProductImage getProductImageById(int id){
        return productImageRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }

    public void saveProductImage(ProductImage productImage){
        productImage.setCreatedAt(new Date());
        productImage.setStatus(true);
        productImageRepository.save(productImage);
    }

    public void updateImage(int id, ProductImage productImage){
        ProductImage oldImage = getProductImageById(id);
//        if(productImage == null){
//            throw new NullException("Không được để trống");
//        }
        if(oldImage != null){
            oldImage.setImageName(productImage.getImageName());
            oldImage.setUrl(productImage.getUrl());
            oldImage.setProduct(productImage.getProduct());
            oldImage.setUpdatedAt(new Date());
            productImageRepository.save(oldImage);
        }else{
            throw new NotFoundException(NOT_FOUND+ id);
        }
    }

    public void deleteImage(int id){
        ProductImage productImage = getProductImageById(id);
        if(productImage == null){
            throw new NotFoundException(NOT_FOUND+ id);
        }
        productImageRepository.deleteById(id);
    }

    public List<ProductImage> getProductImagesFromListCartItem(List<CartItem> cartItems){
        List<ProductImage> productImages = new ArrayList<>();
        cartItems.forEach(item -> {
            productImages.add(getProductImagesByProductId(item.getProductDetail().getProduct().getId()).get(0));
        });
        return productImages;
    }
}
