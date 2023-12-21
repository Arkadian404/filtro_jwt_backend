package com.ark.security.service.product;

import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Product;
import com.ark.security.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(new Product()));

        assertNotNull(productService.getAllProducts());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProductsEmpty() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> productService.getAllProducts());
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setName("Test Product");
        when(productRepository.existsProductByName(anyString())).thenReturn(false);

        productService.saveProduct(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testSaveProductDuplicate() {
        Product product = new Product();
        product.setName("Test Product");
        when(productRepository.existsProductByName(anyString())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> productService.saveProduct(product));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        productService.updateProduct(1, product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(productRepository.existsById(anyInt())).thenReturn(true);

        productService.deleteProduct(1);

        verify(productRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1));
    }

}