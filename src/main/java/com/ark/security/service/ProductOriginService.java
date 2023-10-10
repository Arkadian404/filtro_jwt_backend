package com.ark.security.service;

import com.ark.security.dto.ProductOriginDto;
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
    private final String NOT_FOUND = "Không tìm thấy nguồn gốc sản phẩm: ";
    private final String EMPTY = "Không có nguồn gốc sản phẩm nào";

    public void saveProductOrigin(ProductOrigin productOrigin) {
        productOriginRepository.save(productOrigin);
    }


    public List<ProductOrigin> getAllProductOrigin() {
        if(productOriginRepository.findAll().isEmpty())
            throw new NotFoundException(EMPTY;
        return productOriginRepository.findAll();
    }

    public List<ProductOriginDto> getAllProductOriginDto() {
        List<ProductOriginDto> productOrigins = productOriginRepository.findAll()
                .stream()
                .map(ProductOrigin::convertToDto)
                .toList();
        if(productOrigins.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return productOrigins;
    }

    public ProductOrigin getProductOriginById(int id) {
        return productOriginRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND + id));
    }

    public void updateProductOrigin(int id, ProductOrigin productOrigin) {
        ProductOrigin productOriginUpdate = getProductOriginById(id);
        productOriginUpdate.setName(productOrigin.getName());
        productOriginUpdate.setDescription(productOrigin.getDescription());
        productOriginRepository.save(productOriginUpdate);
    }

    public void deleteProductOrigin(int id) {
        if (!productOriginRepository.existsById(id))
            throw new NotFoundException(NOT_FOUND + id);
        productOriginRepository.deleteById(id);
    }

}
