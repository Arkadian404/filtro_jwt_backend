package com.ark.security.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ark.security.dto.request.CategoryRequest;
import com.ark.security.dto.response.CategoryResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.CategoryMapper;
import com.ark.security.models.product.Category;
import com.ark.security.repository.product.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CategoryService.class})
@ExtendWith(SpringExtension.class)
class CategoryServiceTest {
    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    /**
     * Test {@link CategoryService#getAll()}.
     * <ul>
     *   <li>Given {@link CategoryMapper}.</li>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#getAll()}
     */
    @Test
    @DisplayName("Test getAll(); given CategoryMapper; then return Empty")
    void testGetAll_givenCategoryMapper_thenReturnEmpty() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<CategoryResponse> actualAll = categoryService.getAll();

        // Assert
        verify(categoryRepository).findAll();
        assertTrue(actualAll.isEmpty());
    }

    /**
     * Test {@link CategoryService#getById(int)}.
     * <ul>
     *   <li>Given {@link CategoryRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#getById(int)}
     */
    @Test
    @DisplayName("Test getById(int); given CategoryRepository findById(Object) return empty; then throw AppException")
    void testGetById_givenCategoryRepositoryFindByIdReturnEmpty_thenThrowAppException() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(AppException.class, () -> categoryService.getById(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Test {@link CategoryService#getById(int)}.
     * <ul>
     *   <li>Then return {@code Name}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#getById(int)}
     */
    @Test
    @DisplayName("Test getById(int); then return 'Name'")
    void testGetById_thenReturnName() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        CategoryResponse buildResult = CategoryResponse.builder().id(1).name("Name").status(true).build();
        when(categoryMapper.toCategoryResponse(Mockito.<Category>any())).thenReturn(buildResult);

        // Act
        CategoryResponse actualById = categoryService.getById(1);

        // Assert
        verify(categoryMapper).toCategoryResponse(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
        assertEquals("Name", actualById.getName());
        assertEquals(1, actualById.getId().intValue());
        assertTrue(actualById.getStatus());
    }

    /**
     * Test {@link CategoryService#getById(int)}.
     * <ul>
     *   <li>Then throw {@link DataIntegrityViolationException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#getById(int)}
     */
    @Test
    @DisplayName("Test getById(int); then throw DataIntegrityViolationException")
    void testGetById_thenThrowDataIntegrityViolationException() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(categoryMapper.toCategoryResponse(Mockito.<Category>any()))
                .thenThrow(new DataIntegrityViolationException("Msg"));

        // Act and Assert
        assertThrows(DataIntegrityViolationException.class, () -> categoryService.getById(1));
        verify(categoryMapper).toCategoryResponse(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Test {@link CategoryService#create(CategoryRequest)}.
     * <ul>
     *   <li>Given {@link CategoryRepository} {@link CrudRepository#save(Object)}
     * return {@link Category#Category()}.</li>
     *   <li>Then return {@code Name}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#create(CategoryRequest)}
     */
    @Test
    @DisplayName("Test create(CategoryRequest); given CategoryRepository save(Object) return Category(); then return 'Name'")
    void testCreate_givenCategoryRepositorySaveReturnCategory_thenReturnName() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category);

        Category category2 = new Category();
        category2.setId(1);
        category2.setName("Name");
        category2.setProductList(new ArrayList<>());
        category2.setStatus(true);
        category2.setVoucherList(new ArrayList<>());
        CategoryResponse buildResult = CategoryResponse.builder().id(1).name("Name").status(true).build();
        when(categoryMapper.toCategoryResponse(Mockito.<Category>any())).thenReturn(buildResult);
        when(categoryMapper.toCategory(Mockito.<CategoryRequest>any())).thenReturn(category2);

        // Act
        CategoryResponse actualCreateResult = categoryService.create(new CategoryRequest());

        // Assert
        verify(categoryMapper).toCategory(isA(CategoryRequest.class));
        verify(categoryMapper).toCategoryResponse(isA(Category.class));
        verify(categoryRepository).save(isA(Category.class));
        assertEquals("Name", actualCreateResult.getName());
        assertEquals(1, actualCreateResult.getId().intValue());
        assertTrue(actualCreateResult.getStatus());
    }

    /**
     * Test {@link CategoryService#create(CategoryRequest)}.
     * <ul>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#create(CategoryRequest)}
     */
    @Test
    @DisplayName("Test create(CategoryRequest); then throw AppException")
    void testCreate_thenThrowAppException() {
        // Arrange
        when(categoryMapper.toCategory(Mockito.<CategoryRequest>any()))
                .thenThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        // Act and Assert
        assertThrows(AppException.class, () -> categoryService.create(new CategoryRequest()));
        verify(categoryMapper).toCategory(isA(CategoryRequest.class));
    }

    /**
     * Test {@link CategoryService#create(CategoryRequest)}.
     * <ul>
     *   <li>Then throw {@link DataIntegrityViolationException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#create(CategoryRequest)}
     */
    @Test
    @DisplayName("Test create(CategoryRequest); then throw DataIntegrityViolationException")
    void testCreate_thenThrowDataIntegrityViolationException() {
        // Arrange
        when(categoryRepository.save(Mockito.<Category>any())).thenThrow(new DataIntegrityViolationException("Msg"));

        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        when(categoryMapper.toCategory(Mockito.<CategoryRequest>any())).thenReturn(category);
        CategoryRequest request = CategoryRequest.builder().name("Name").status(true).build();

        // Act and Assert
        assertThrows(DataIntegrityViolationException.class, () -> categoryService.create(request));
        verify(categoryMapper).toCategory(isA(CategoryRequest.class));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Test {@link CategoryService#update(int, CategoryRequest)}.
     * <p>
     * Method under test: {@link CategoryService#update(int, CategoryRequest)}
     */
    @Test
    @DisplayName("Test update(int, CategoryRequest)")
    void testUpdate() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)).when(categoryMapper)
                .updateCategory(Mockito.<Category>any(), Mockito.<CategoryRequest>any());

        // Act and Assert
        assertThrows(AppException.class, () -> categoryService.update(1, new CategoryRequest()));
        verify(categoryMapper).updateCategory(isA(Category.class), isA(CategoryRequest.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Test {@link CategoryService#update(int, CategoryRequest)}.
     * <p>
     * Method under test: {@link CategoryService#update(int, CategoryRequest)}
     */
    @Test
    @DisplayName("Test update(int, CategoryRequest)")
    void testUpdate2() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.save(Mockito.<Category>any())).thenThrow(new DataIntegrityViolationException("Msg"));
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(categoryMapper).updateCategory(Mockito.<Category>any(), Mockito.<CategoryRequest>any());
        CategoryRequest request = CategoryRequest.builder().name("Name").status(true).build();

        // Act and Assert
        assertThrows(AppException.class, () -> categoryService.update(1, request));
        verify(categoryMapper).updateCategory(isA(Category.class), isA(CategoryRequest.class));
        verify(categoryRepository).findById(eq(1));
        verify(categoryRepository).save(isA(Category.class));
    }

    /**
     * Test {@link CategoryService#update(int, CategoryRequest)}.
     * <ul>
     *   <li>Given {@link CategoryRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#update(int, CategoryRequest)}
     */
    @Test
    @DisplayName("Test update(int, CategoryRequest); given CategoryRepository findById(Object) return empty; then throw AppException")
    void testUpdate_givenCategoryRepositoryFindByIdReturnEmpty_thenThrowAppException() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(AppException.class, () -> categoryService.update(1, new CategoryRequest()));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Test {@link CategoryService#update(int, CategoryRequest)}.
     * <ul>
     *   <li>Given {@link CategoryRepository} {@link CrudRepository#save(Object)}
     * return {@link Category#Category()}.</li>
     *   <li>Then return {@code Name}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#update(int, CategoryRequest)}
     */
    @Test
    @DisplayName("Test update(int, CategoryRequest); given CategoryRepository save(Object) return Category(); then return 'Name'")
    void testUpdate_givenCategoryRepositorySaveReturnCategory_thenReturnName() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);

        Category category2 = new Category();
        category2.setId(1);
        category2.setName("Name");
        category2.setProductList(new ArrayList<>());
        category2.setStatus(true);
        category2.setVoucherList(new ArrayList<>());
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category2);
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        CategoryResponse buildResult = CategoryResponse.builder().id(1).name("Name").status(true).build();
        when(categoryMapper.toCategoryResponse(Mockito.<Category>any())).thenReturn(buildResult);
        doNothing().when(categoryMapper).updateCategory(Mockito.<Category>any(), Mockito.<CategoryRequest>any());

        // Act
        CategoryResponse actualUpdateResult = categoryService.update(1, new CategoryRequest());

        // Assert
        verify(categoryMapper).toCategoryResponse(isA(Category.class));
        verify(categoryMapper).updateCategory(isA(Category.class), isA(CategoryRequest.class));
        verify(categoryRepository).findById(eq(1));
        verify(categoryRepository).save(isA(Category.class));
        assertEquals("Name", actualUpdateResult.getName());
        assertEquals(1, actualUpdateResult.getId().intValue());
        assertTrue(actualUpdateResult.getStatus());
    }

    /**
     * Test {@link CategoryService#delete(int)}.
     * <ul>
     *   <li>Given {@link CategoryRepository} {@link CrudRepository#delete(Object)}
     * does nothing.</li>
     *   <li>Then calls {@link CrudRepository#delete(Object)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#delete(int)}
     */
    @Test
    @DisplayName("Test delete(int); given CategoryRepository delete(Object) does nothing; then calls delete(Object)")
    void testDelete_givenCategoryRepositoryDeleteDoesNothing_thenCallsDelete() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);
        doNothing().when(categoryRepository).delete(Mockito.<Category>any());
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        categoryService.delete(1);

        // Assert that nothing has changed
        verify(categoryRepository).delete(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Test {@link CategoryService#delete(int)}.
     * <ul>
     *   <li>Given {@link CategoryRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#delete(int)}
     */
    @Test
    @DisplayName("Test delete(int); given CategoryRepository findById(Object) return empty; then throw AppException")
    void testDelete_givenCategoryRepositoryFindByIdReturnEmpty_thenThrowAppException() {
        // Arrange
        Optional<Category> emptyResult = Optional.empty();
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(AppException.class, () -> categoryService.delete(1));
        verify(categoryRepository).findById(eq(1));
    }

    /**
     * Test {@link CategoryService#delete(int)}.
     * <ul>
     *   <li>Then throw {@link DataIntegrityViolationException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CategoryService#delete(int)}
     */
    @Test
    @DisplayName("Test delete(int); then throw DataIntegrityViolationException")
    void testDelete_thenThrowDataIntegrityViolationException() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setName("Name");
        category.setProductList(new ArrayList<>());
        category.setStatus(true);
        category.setVoucherList(new ArrayList<>());
        Optional<Category> ofResult = Optional.of(category);
        doThrow(new DataIntegrityViolationException("Msg")).when(categoryRepository).delete(Mockito.<Category>any());
        when(categoryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(DataIntegrityViolationException.class, () -> categoryService.delete(1));
        verify(categoryRepository).delete(isA(Category.class));
        verify(categoryRepository).findById(eq(1));
    }
}
