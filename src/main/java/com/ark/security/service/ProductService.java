package com.ark.security.service;

import com.ark.security.models.Product;
import com.ark.security.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void updateProduct(int id, Product product){
        Product oldProduct = getProductById(id);
        if (oldProduct != null) {
            oldProduct.setName(product.getName());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setCategory(product.getCategory());
            oldProduct.setQuantity(product.getQuantity());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setImage(product.getImage());
            oldProduct.setSold(product.getSold());
            oldProduct.setSale(product.getSale());
            oldProduct.setFlavor(product.getFlavor());
            oldProduct.setCreatedAt(LocalDateTime.now());
            oldProduct.setStatus(product.getStatus());
            productRepository.save(oldProduct);
        }
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

}
