package com.ark.security.service.product;

import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.ProductOrigin;
import com.ark.security.repository.product.ProductOriginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class ProductOriginServiceTest {

    @Mock
    private ProductOriginRepository productOriginRepository;

    @InjectMocks
    private ProductOriginService productOriginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProductOrigin() {
        ProductOrigin productOrigin = new ProductOrigin();
        productOrigin.setName("Test Origin");
        when(productOriginRepository.existsProductOriginByName(anyString())).thenReturn(false);

        productOriginService.saveProductOrigin(productOrigin);

        verify(productOriginRepository, times(1)).save(productOrigin);
    }

    @Test
    void testSaveProductOriginDuplicate() {
        ProductOrigin productOrigin = new ProductOrigin();
        productOrigin.setName("Test Origin");
        when(productOriginRepository.existsProductOriginByName(anyString())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> productOriginService.saveProductOrigin(productOrigin));
    }

    @Test
    void testGetAllProductOrigin() {
        when(productOriginRepository.findAll()).thenReturn(Collections.singletonList(new ProductOrigin()));

        assertNotNull(productOriginService.getAllProductOrigin());

        verify(productOriginRepository, times(2)).findAll();
    }

    @Test
    void testGetAllProductOriginEmpty() {
        when(productOriginRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> productOriginService.getAllProductOrigin());
    }

    @Test
    void testGetProductOriginById() {
        when(productOriginRepository.findById(anyInt())).thenReturn(Optional.of(new ProductOrigin()));

        assertNotNull(productOriginService.getProductOriginById(1));

        verify(productOriginRepository, times(1)).findById(anyInt());
    }

    @Test
    void testGetProductOriginByIdNotFound() {
        when(productOriginRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productOriginService.getProductOriginById(1));
    }

    @Test
    void testUpdateProductOrigin() {
        when(productOriginRepository.findById(anyInt())).thenReturn(Optional.of(new ProductOrigin()));

        productOriginService.updateProductOrigin(1, new ProductOrigin());

        verify(productOriginRepository, times(1)).save(any(ProductOrigin.class));
    }

    @Test
    void testDeleteProductOrigin() {
        when(productOriginRepository.existsById(anyInt())).thenReturn(true);

        productOriginService.deleteProductOrigin(1);

        verify(productOriginRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteProductOriginNotFound() {
        when(productOriginRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productOriginService.deleteProductOrigin(1));
    }
}
