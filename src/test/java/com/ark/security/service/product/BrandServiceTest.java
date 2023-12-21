package com.ark.security.service.product;

import com.ark.security.dto.BrandDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Brand;
import com.ark.security.repository.product.BrandRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BrandServiceTest {


    @Test
    void testGetAllBrands() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        List<Brand> mockBrands = new ArrayList<>();
        mockBrands.add(mock(Brand.class));
        when(mockBrandRepository.findAll()).thenReturn(mockBrands);

        // Act
        List<Brand> brands = brandService.getAllBrands();

        // Assert
        verify(mockBrandRepository, times(1)).findAll();
        assertEquals(mockBrands, brands);
    }

    @Test
    void testGetBrandById() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        Brand mockBrand = mock(Brand.class);
        when(mockBrandRepository.findById(1)).thenReturn(Optional.of(mockBrand));

        // Act
        Brand brand = brandService.getBrandById(1);

        // Assert
        verify(mockBrandRepository, times(1)).findById(1);
        assertEquals(mockBrand, brand);
    }

    @Test
    void testSaveBrand() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        Brand mockBrand = mock(Brand.class);
        when(mockBrand.getName()).thenReturn("Test Brand");
        when(mockBrandRepository.existsBrandByName("Test Brand")).thenReturn(false);

        // Act
        brandService.saveBrand(mockBrand);

        // Assert
        verify(mockBrandRepository, times(1)).existsBrandByName("Test Brand");
        verify(mockBrandRepository, times(1)).save(mockBrand);
    }

    @Test
    void testUpdateBrand() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        Brand mockBrand = mock(Brand.class);
        when(mockBrandRepository.findById(1)).thenReturn(Optional.of(mockBrand));

        // Act
        brandService.updateBrand(1, mockBrand);

        // Assert
        verify(mockBrandRepository, times(1)).findById(1);
        verify(mockBrandRepository, times(1)).save(mockBrand);
    }

    @Test
    void testDeleteBrand() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);

        // Act
        brandService.deleteBrand(1);

        // Assert
        verify(mockBrandRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAllBrandsDto() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        List<Brand> mockBrands = new ArrayList<>();
        Brand mockBrand = mock(Brand.class);
        mockBrands.add(mockBrand);
        when(mockBrandRepository.findAll()).thenReturn(mockBrands);
        when(mockBrand.convertToDto()).thenReturn(new BrandDto());

        // Act
        List<BrandDto> brandDtos = brandService.getAllBrandsDto();

        // Assert
        verify(mockBrandRepository, times(1)).findAll();
        verify(mockBrand, times(1)).convertToDto();
        assertEquals(1, brandDtos.size());
    }

    @Test
    void testSaveBrandDuplicate() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        Brand mockBrand = mock(Brand.class);
        when(mockBrand.getName()).thenReturn("Test Brand");
        when(mockBrandRepository.existsBrandByName("Test Brand")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateException.class, () -> brandService.saveBrand(mockBrand));
    }

    @Test
    void testGetBrandByIdNotFound() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        when(mockBrandRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> brandService.getBrandById(1));
    }

    @Test
    void testGetAllBrandsNotFound() {
        // Arrange
        BrandRepository mockBrandRepository = mock(BrandRepository.class);
        BrandService brandService = new BrandService(mockBrandRepository);
        when(mockBrandRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NotFoundException.class, brandService::getAllBrands);
    }
}