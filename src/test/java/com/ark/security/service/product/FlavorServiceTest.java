package com.ark.security.service.product;

import com.ark.security.dto.FlavorDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Flavor;
import com.ark.security.repository.product.CategoryRepository;
import com.ark.security.repository.product.FlavorRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlavorServiceTest {

    @Test
    void testGetFlavorById() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        Flavor mockFlavor = mock(Flavor.class);
        when(mockFlavorRepository.findById(1)).thenReturn(Optional.of(mockFlavor));

        // Act
        Flavor flavor = flavorService.getFlavorById(1);

        // Assert
        verify(mockFlavorRepository, times(1)).findById(1);
        assertEquals(mockFlavor, flavor);
    }

    @Test
    void testGetAllFlavors() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        List<Flavor> mockFlavors = new ArrayList<>();
        mockFlavors.add(mock(Flavor.class));
        when(mockFlavorRepository.findAll()).thenReturn(mockFlavors);

        // Act
        List<Flavor> flavors = flavorService.getAllFlavors();

        // Assert
        verify(mockFlavorRepository, times(1)).findAll();
        assertEquals(mockFlavors, flavors);
    }

    @Test
    void testGetAllFlavorsDto() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        List<Flavor> mockFlavors = new ArrayList<>();
        Flavor mockFlavor = mock(Flavor.class);
        mockFlavors.add(mockFlavor);
        when(mockFlavorRepository.findAll()).thenReturn(mockFlavors);
        when(mockFlavor.convertToDto()).thenReturn(new FlavorDto());

        // Act
        List<FlavorDto> flavorDtos = flavorService.getAllFlavorsDto();

        // Assert
        verify(mockFlavorRepository, times(1)).findAll();
        verify(mockFlavor, times(1)).convertToDto();
        assertEquals(1, flavorDtos.size());
    }

    @Test
    void testExistsFlavorByName() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        when(mockFlavorRepository.existsFlavorByName("Test Flavor")).thenReturn(true);

        // Act
        boolean exists = flavorService.existsFlavorByName("Test Flavor");

        // Assert
        verify(mockFlavorRepository, times(1)).existsFlavorByName("Test Flavor");
        assertTrue(exists);
    }

    @Test
    void testSaveFlavor() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        Flavor mockFlavor = mock(Flavor.class);
        when(mockFlavor.getName()).thenReturn("Test Flavor");
        when(mockFlavorRepository.existsFlavorByName("Test Flavor")).thenReturn(false);

        // Act
        flavorService.saveFlavor(mockFlavor);

        // Assert
        verify(mockFlavorRepository, times(1)).existsFlavorByName("Test Flavor");
        verify(mockFlavorRepository, times(1)).save(mockFlavor);
    }

    @Test
    void testUpdateFlavor() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        Flavor mockFlavor = mock(Flavor.class);
        when(mockFlavorRepository.findById(1)).thenReturn(Optional.of(mockFlavor));

        // Act
        flavorService.updateFlavor(1, mockFlavor);

        // Assert
        verify(mockFlavorRepository, times(1)).findById(1);
        verify(mockFlavorRepository, times(1)).save(mockFlavor);
    }

    @Test
    void testDeleteFlavor() {
        // Arrange
        FlavorRepository mockFlavorRepository = mock(FlavorRepository.class);
        FlavorService flavorService = new FlavorService(mockFlavorRepository);
        Flavor mockFlavor = mock(Flavor.class);
        when(mockFlavorRepository.findById(1)).thenReturn(Optional.of(mockFlavor));

        // Act
        flavorService.deleteFlavor(1);

        // Assert
        verify(mockFlavorRepository, times(1)).deleteById(1);
    }


    @Test
    void testDeleteCategoryNotFound() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        when(mockCategoryRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1));
    }

}