package com.ark.security.service.product;

import com.ark.security.dto.CategoryDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Category;
import com.ark.security.repository.product.CategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    @Test
    void testGetAllCategories() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(mock(Category.class));
        when(mockCategoryRepository.findAll()).thenReturn(mockCategories);

        // Act
        List<Category> categories = categoryService.getAllCategories();

        // Assert
        verify(mockCategoryRepository, times(1)).findAll();
        assertEquals(mockCategories, categories);
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        Category mockCategory = mock(Category.class);
        when(mockCategoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));

        // Act
        Category category = categoryService.getCategoryById(1);

        // Assert
        verify(mockCategoryRepository, times(1)).findById(1);
        assertEquals(mockCategory, category);
    }

    @Test
    void testSaveCategory() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        Category mockCategory = mock(Category.class);
        when(mockCategory.getName()).thenReturn("Test Category");
        when(mockCategoryRepository.findCategoryByName("Test Category")).thenReturn(Optional.empty());

        // Act
        categoryService.saveCategory(mockCategory);

        // Assert
        verify(mockCategoryRepository, times(1)).findCategoryByName("Test Category");
        verify(mockCategoryRepository, times(1)).save(mockCategory);
    }

    @Test
    void testUpdateCategory() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        Category mockCategory = mock(Category.class);
        when(mockCategoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));

        // Act
        categoryService.updateCategory(1, mockCategory);

        // Assert
        verify(mockCategoryRepository, times(1)).findById(1);
        verify(mockCategoryRepository, times(1)).save(mockCategory);
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);


        // Act

        // Assert
        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1));
    }

    @Test
    void testGetAllCategoriesDto() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        List<Category> mockCategories = new ArrayList<>();
        Category mockCategory = mock(Category.class);
        mockCategories.add(mockCategory);
        when(mockCategoryRepository.findAll()).thenReturn(mockCategories);
        when(mockCategory.convertToDto()).thenReturn(new CategoryDto());

        // Act
        List<CategoryDto> categoryDtos = categoryService.getAllCategoriesDto();

        // Assert
        verify(mockCategoryRepository, times(1)).findAll();
        verify(mockCategory, times(1)).convertToDto();
        assertEquals(1, categoryDtos.size());
    }

    @Test
    void testSaveCategoryDuplicate() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        Category mockCategory = mock(Category.class);
        when(mockCategory.getName()).thenReturn("Test Category");
        when(mockCategoryRepository.findCategoryByName("Test Category")).thenReturn(Optional.of(mockCategory));

        // Act & Assert
        assertThrows(DuplicateException.class, () -> categoryService.saveCategory(mockCategory));
    }

    @Test
    void testGetCategoryByIdNotFound() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        when(mockCategoryRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1));
    }

    @Test
    void testGetAllCategoriesNotFound() {
        // Arrange
        CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(mockCategoryRepository);
        when(mockCategoryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> categoryService.getAllCategories());
    }

}