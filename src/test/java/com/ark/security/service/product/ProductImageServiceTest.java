package com.ark.security.service.product;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductImage;
import com.ark.security.repository.product.ProductImageRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductImageServiceTest {

    @Test
    void testGetAllProductImages() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        List<ProductImage> mockProductImages = new ArrayList<>();
        mockProductImages.add(mock(ProductImage.class));
        when(mockProductImageRepository.findAll()).thenReturn(mockProductImages);

        // Act
        List<ProductImage> productImages = productImageService.getAllProductImages();

        // Assert
        assertEquals(mockProductImages, productImages);
    }

    @Test
    void testGetProductImagesByProductId() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        List<ProductImage> mockProductImages = new ArrayList<>();
        ProductImage mockProductImage = mock(ProductImage.class);
        mockProductImages.add(mockProductImage);
        when(mockProductImageRepository.findAllByProductId(1)).thenReturn(mockProductImages);

        // Act
        List<ProductImage> productImages = productImageService.getProductImagesByProductId(1);

        // Assert
        assertEquals(mockProductImages, productImages);
    }

    @Test
    void testGetProductImageById() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        ProductImage mockProductImage = mock(ProductImage.class);
        when(mockProductImageRepository.findById(1)).thenReturn(Optional.of(mockProductImage));

        // Act
        ProductImage productImage = productImageService.getProductImageById(1);

        // Assert
        assertEquals(mockProductImage, productImage);
    }

    @Test
    void testSaveProductImage() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        ProductImage mockProductImage = mock(ProductImage.class);

        // Act
        productImageService.saveProductImage(mockProductImage);

        // Assert
        verify(mockProductImageRepository, times(1)).save(mockProductImage);
    }

    @Test
    void testUpdateImage() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        ProductImage mockProductImage = mock(ProductImage.class);
        when(mockProductImageRepository.findById(1)).thenReturn(Optional.of(mockProductImage));

        // Act
        productImageService.updateImage(1, mockProductImage);

        // Assert
        verify(mockProductImageRepository, times(1)).findById(1);
        verify(mockProductImageRepository, times(1)).save(mockProductImage);
    }

    @Test
    void testDeleteImage() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        ProductImage mockProductImage = mock(ProductImage.class);
        when(mockProductImageRepository.findById(1)).thenReturn(Optional.of(mockProductImage));
        when(mockProductImageRepository.existsById(1)).thenReturn(true);

        // Act
        productImageService.deleteImage(1);

        // Assert
        verify(mockProductImageRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetProductImagesFromListCartItem() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        List<ProductImage> mockProductImages = new ArrayList<>();
        ProductImage mockProductImage = mock(ProductImage.class);
        mockProductImages.add(mockProductImage);
        when(mockProductImageRepository.findAllByProductId(1)).thenReturn(mockProductImages);

        // Act
        List<ProductImage> productImages = productImageService.getProductImagesFromListCartItem(new ArrayList<>());

        // Assert
        assertEquals(Collections.EMPTY_LIST, productImages);
    }

    @Test
    void testDeleteImageNotFound() {
        // Arrange
        ProductImageRepository mockProductImageRepository = mock(ProductImageRepository.class);
        ProductImageService productImageService = new ProductImageService(mockProductImageRepository);
        when(mockProductImageRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productImageService.deleteImage(1));
    }

}