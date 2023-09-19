package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.Category;
import com.ark.security.models.product.Product;
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
        return productRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy sản phẩm: "+ id));
    }

    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()){
            throw new NotFoundException("Không có sản phẩm nào");
        }
        return products;
    }

    public Category getCategoryByProductId(int id){
        return productRepository.findCategoryUsingId(id).orElseThrow(()-> new NotFoundException("Không tìm thấy sản phẩm: "+ id));
    }


    public List<Product> getAllProductsByCategory(int id){
        List<Product> products = productRepository.findAllByCategoryId(id);
        if(products.isEmpty()){
            throw new NotFoundException("Không có sản phẩm nào");
        }
        return products;
    }


    public boolean isExistsProductByName(String name){
        return productRepository.existsProductByName(name);
    }

    public void saveProduct(Product product) {
        if(isExistsProductByName(product.getName())) {
            throw new NotFoundException("Sản phẩm đã tồn tại");
        }
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void updateProduct(int id, Product product){
        Product oldProduct = getProductById(id);
        if(product == null){
            throw new NullException("Không được để trống");
        }
        if (oldProduct != null) {
            oldProduct.setName(product.getName());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setCategory(product.getCategory());
            oldProduct.setQuantity(product.getQuantity());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setSold(product.getSold());
            oldProduct.setSale(product.getSale());
            oldProduct.setFlavor(product.getFlavor());
            oldProduct.setUpdatedAt(LocalDateTime.now());
            oldProduct.setStatus(product.getStatus());
            productRepository.save(oldProduct);
        }else {
            throw new NotFoundException("Không tìm thấy sản phẩm: "+ id);
        }
    }

    public void deleteProduct(int id) {
        Product product = getProductById(id);
        if(product == null){
            throw new NotFoundException("Không tìm thấy sản phẩm: "+ id);
        }
        productRepository.deleteById(id);
    }

}
