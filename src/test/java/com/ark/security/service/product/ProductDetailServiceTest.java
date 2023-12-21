package com.ark.security.service.product;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.repository.product.ProductDetailRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDetailServiceTest {
    @Test
    void testSaveProductDetail() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        ProductDetail mockProductDetail = mock(ProductDetail.class);

        // Act
        productDetailService.saveProductDetail(mockProductDetail);

        // Assert
        verify(mockProductDetailRepository, times(1)).save(mockProductDetail);
    }

    @Test
    void testGetProductDetailById() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        ProductDetail mockProductDetail = mock(ProductDetail.class);
        when(mockProductDetailRepository.findById(1)).thenReturn(Optional.of(mockProductDetail));

        // Act
        ProductDetail productDetail = productDetailService.getProductDetailById(1);

        // Assert
        verify(mockProductDetailRepository, times(1)).findById(1);
        assertEquals(mockProductDetail, productDetail);
    }

    @Test
    void testGetAllProductDetail() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        List<ProductDetail> mockProductDetails = new ArrayList<>();
        mockProductDetails.add(mock(ProductDetail.class));
        when(mockProductDetailRepository.findAll()).thenReturn(mockProductDetails);

        // Act
        List<ProductDetail> productDetails = productDetailService.getAllProductDetail();

        // Assert
        assertEquals(mockProductDetails, productDetails);
    }

    @Test
    void testGetProductDetailsByProductId() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        List<ProductDetail> mockProductDetails = new ArrayList<>();
        ProductDetail mockProductDetail = mock(ProductDetail.class);
        mockProductDetails.add(mockProductDetail);
        when(mockProductDetailRepository.findAllByProductId(1)).thenReturn(Optional.of(mockProductDetails));

        // Act
        List<ProductDetail> productDetails = productDetailService.getProductDetailsByProductId(1);

        // Assert
        assertEquals(mockProductDetails, productDetails);
    }

    @Test
    void testUpdateProductDetail() {
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        ProductDetail mockProductDetail = mock(ProductDetail.class);
        ProductDetail updatedProductDetail = mock(ProductDetail.class);
        when(mockProductDetailRepository.findById(1)).thenReturn(Optional.of(mockProductDetail));
        when(updatedProductDetail.getProduct()).thenReturn(mock(Product.class));

        // Act
        productDetailService.updateProductDetail(1, updatedProductDetail);

        // Assert
        verify(mockProductDetailRepository, times(1)).findById(1);
        verify(mockProductDetailRepository, times(1)).save(mockProductDetail);
    }

    @Test
    void testDeleteProductDetail() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        when(mockProductDetailRepository.existsById(1)).thenReturn(true);

        // Act
        productDetailService.deleteProductDetail(1);

        // Assert
        verify(mockProductDetailRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteProductDetailNotFound() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        when(mockProductDetailRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productDetailService.deleteProductDetail(1));
    }


    @Test
    void testUpdateProductDetailNotFound() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        ProductDetail mockProductDetail = mock(ProductDetail.class);
        when(mockProductDetailRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productDetailService.updateProductDetail(1, mockProductDetail));
    }

    @Test
    void testUpdateProductDetailNullProduct() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        ProductDetail mockProductDetail = mock(ProductDetail.class);
        when(mockProductDetailRepository.findById(1)).thenReturn(Optional.of(mockProductDetail));
        when(mockProductDetail.getProduct()).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productDetailService.updateProductDetail(1, mockProductDetail));
    }

    @Test
    void testGetAllProductDetailEmpty() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        when(mockProductDetailRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NotFoundException.class, productDetailService::getAllProductDetail);
    }

    @Test
    void testGetProductDetailsByProductIdEmpty() {
        // Arrange
        ProductDetailRepository mockProductDetailRepository = mock(ProductDetailRepository.class);
        ProductDetailService productDetailService = new ProductDetailService(mockProductDetailRepository);
        when(mockProductDetailRepository.findAllByProductId(1)).thenReturn(Optional.of(new ArrayList<>()));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productDetailService.getProductDetailsByProductId(0));
    }


}