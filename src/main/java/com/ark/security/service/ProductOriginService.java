package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.ProductOrigin;
import com.ark.security.repository.ProductOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOriginService {
    private final ProductOriginRepository productOriginRepository;

    public void saveProductOrigin(ProductOrigin productOrigin) {
        productOriginRepository.save(productOrigin);
    }


    public List<ProductOrigin> getAllProductOrigin() {
        if(productOriginRepository.findAll().isEmpty())
            throw new NotFoundException("Không có nguồn gốc sản phẩm nào");
        return productOriginRepository.findAll();
    }

    public ProductOrigin getProductOriginById(int id) {
        return productOriginRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy nguồn gốc sản phẩm: " + id));
    }

    public void updateProductOrigin(int id, ProductOrigin productOrigin) {
        ProductOrigin productOriginUpdate = getProductOriginById(id);
        productOriginUpdate.setName(productOrigin.getName());
        productOriginUpdate.setDescription(productOrigin.getDescription());
        productOriginRepository.save(productOriginUpdate);
    }

    public void deleteProductOrigin(int id) {
        if (!productOriginRepository.existsById(id))
            throw new NotFoundException("Không tìm thấy nguồn gốc sản phẩm: " + id);
        productOriginRepository.deleteById(id);
    }

}
