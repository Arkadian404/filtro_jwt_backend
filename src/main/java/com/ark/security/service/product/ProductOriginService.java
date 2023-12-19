package com.ark.security.service.product;

import com.ark.security.dto.ProductOriginDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.ProductOrigin;
import com.ark.security.repository.product.ProductOriginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductOriginService {
    private final ProductOriginRepository productOriginRepository;
    private final String NOT_FOUND = "Không tìm thấy nguồn gốc sản phẩm: ";
    private final String EMPTY = "Không có nguồn gốc sản phẩm nào";

    public void saveProductOrigin(ProductOrigin productOrigin) {
        if(productOriginRepository.existsProductOriginByName(productOrigin.getName()))
            throw new DuplicateException("Nguồn gốc sản phẩm đã tồn tại");
        productOriginRepository.save(productOrigin);
    }


    public List<ProductOrigin> getAllProductOrigin() {
        if(productOriginRepository.findAll().isEmpty())
            throw new NotFoundException(EMPTY);
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

    public List<ProductOriginDto> getProductOriginDtoByContinent(String continent) {
        List<ProductOriginDto> productOrigins = productOriginRepository.findByContinent(continent)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND + continent))
                .stream()
                .map(ProductOrigin::convertToDto)
                .collect(Collectors.toList());
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
        productOriginUpdate.setContinent(productOrigin.getContinent());
        productOriginUpdate.setDescription(productOrigin.getDescription());
        productOriginUpdate.setStatus(productOrigin.getStatus());
        productOriginRepository.save(productOriginUpdate);
    }

    public void deleteProductOrigin(int id) {
        if (!productOriginRepository.existsById(id))
            throw new NotFoundException(NOT_FOUND + id);
        productOriginRepository.deleteById(id);
    }

}
