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

import com.ark.security.dto.request.BrandRequest;
import com.ark.security.dto.response.BrandResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.BrandMapper;
import com.ark.security.models.product.Brand;
import com.ark.security.repository.product.BrandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {BrandService.class})
@ExtendWith(SpringExtension.class)
class BrandServiceTest {
    @MockBean
    private BrandMapper brandMapper;

    @MockBean
    private BrandRepository brandRepository;

    @Autowired
    private BrandService brandService;

    /**
     * Test {@link BrandService#getAllBrands()}.
     * <ul>
     *   <li>Given {@link BrandMapper}.</li>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#getAllBrands()}
     */
    @Test
    @DisplayName("Test getAllBrands(); given BrandMapper; then return Empty")
    void testGetAllBrands_givenBrandMapper_thenReturnEmpty() {
        // Arrange
        when(brandRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<BrandResponse> actualAllBrands = brandService.getAllBrands();

        // Assert
        verify(brandRepository).findAll();
        assertTrue(actualAllBrands.isEmpty());
    }

    /**
     * Test {@link BrandService#getBrandById(int)}.
     * <p>
     * Method under test: {@link BrandService#getBrandById(int)}
     */
    @Test
    @DisplayName("Test getBrandById(int)")
    void testGetBrandById() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(brandMapper.toBrandResponse(Mockito.<Brand>any()))
                .thenThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.getBrandById(1));
        verify(brandMapper).toBrandResponse(isA(Brand.class));
        verify(brandRepository).findById(eq(1));
    }

    /**
     * Test {@link BrandService#getBrandById(int)}.
     * <ul>
     *   <li>Given {@link BrandRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#getBrandById(int)}
     */
    @Test
    @DisplayName("Test getBrandById(int); given BrandRepository findById(Object) return empty; then throw AppException")
    void testGetBrandById_givenBrandRepositoryFindByIdReturnEmpty_thenThrowAppException() {
        // Arrange
        Optional<Brand> emptyResult = Optional.empty();
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.getBrandById(1));
        verify(brandRepository).findById(eq(1));
    }

    /**
     * Test {@link BrandService#getBrandById(int)}.
     * <ul>
     *   <li>Then return {@code Name}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#getBrandById(int)}
     */
    @Test
    @DisplayName("Test getBrandById(int); then return 'Name'")
    void testGetBrandById_thenReturnName() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        BrandResponse buildResult = BrandResponse.builder()
                .description("The characteristics of someone or something")
                .id(1)
                .name("Name")
                .status(true)
                .build();
        when(brandMapper.toBrandResponse(Mockito.<Brand>any())).thenReturn(buildResult);

        // Act
        BrandResponse actualBrandById = brandService.getBrandById(1);

        // Assert
        verify(brandMapper).toBrandResponse(isA(Brand.class));
        verify(brandRepository).findById(eq(1));
        assertEquals("Name", actualBrandById.getName());
        assertEquals("The characteristics of someone or something", actualBrandById.getDescription());
        assertEquals(1, actualBrandById.getId().intValue());
        assertTrue(actualBrandById.getStatus());
    }

    /**
     * Test {@link BrandService#saveBrand(BrandRequest)}.
     * <ul>
     *   <li>Then return {@code Name}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#saveBrand(BrandRequest)}
     */
    @Test
    @DisplayName("Test saveBrand(BrandRequest); then return 'Name'")
    void testSaveBrand_thenReturnName() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        when(brandRepository.save(Mockito.<Brand>any())).thenReturn(brand);

        Brand brand2 = new Brand();
        brand2.setDescription("The characteristics of someone or something");
        brand2.setId(1);
        brand2.setName("Name");
        brand2.setProducts(new ArrayList<>());
        brand2.setStatus(true);
        BrandResponse buildResult = BrandResponse.builder()
                .description("The characteristics of someone or something")
                .id(1)
                .name("Name")
                .status(true)
                .build();
        when(brandMapper.toBrandResponse(Mockito.<Brand>any())).thenReturn(buildResult);
        when(brandMapper.toBrand(Mockito.<BrandRequest>any())).thenReturn(brand2);

        // Act
        BrandResponse actualSaveBrandResult = brandService.saveBrand(new BrandRequest());

        // Assert
        verify(brandMapper).toBrand(isA(BrandRequest.class));
        verify(brandMapper).toBrandResponse(isA(Brand.class));
        verify(brandRepository).save(isA(Brand.class));
        assertEquals("Name", actualSaveBrandResult.getName());
        assertEquals("The characteristics of someone or something", actualSaveBrandResult.getDescription());
        assertEquals(1, actualSaveBrandResult.getId().intValue());
        assertTrue(actualSaveBrandResult.getStatus());
    }

    /**
     * Test {@link BrandService#saveBrand(BrandRequest)}.
     * <ul>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#saveBrand(BrandRequest)}
     */
    @Test
    @DisplayName("Test saveBrand(BrandRequest); then throw AppException")
    void testSaveBrand_thenThrowAppException() {
        // Arrange
        when(brandMapper.toBrand(Mockito.<BrandRequest>any()))
                .thenThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.saveBrand(new BrandRequest()));
        verify(brandMapper).toBrand(isA(BrandRequest.class));
    }

    /**
     * Test {@link BrandService#updateBrand(int, BrandRequest)}.
     * <p>
     * Method under test: {@link BrandService#updateBrand(int, BrandRequest)}
     */
    @Test
    @DisplayName("Test updateBrand(int, BrandRequest)")
    void testUpdateBrand() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)).when(brandMapper)
                .updateBrand(Mockito.<Brand>any(), Mockito.<BrandRequest>any());

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.updateBrand(1, new BrandRequest()));
        verify(brandMapper).updateBrand(isA(Brand.class), isA(BrandRequest.class));
        verify(brandRepository).findById(eq(1));
    }

    /**
     * Test {@link BrandService#updateBrand(int, BrandRequest)}.
     * <p>
     * Method under test: {@link BrandService#updateBrand(int, BrandRequest)}
     */
    @Test
    @DisplayName("Test updateBrand(int, BrandRequest)")
    void testUpdateBrand2() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);
        when(brandRepository.save(Mockito.<Brand>any())).thenThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(brandMapper).updateBrand(Mockito.<Brand>any(), Mockito.<BrandRequest>any());
        BrandRequest request = BrandRequest.builder()
                .description("The characteristics of someone or something")
                .name("Name")
                .status(true)
                .build();

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.updateBrand(1, request));
        verify(brandMapper).updateBrand(isA(Brand.class), isA(BrandRequest.class));
        verify(brandRepository).findById(eq(1));
        verify(brandRepository).save(isA(Brand.class));
    }

    /**
     * Test {@link BrandService#updateBrand(int, BrandRequest)}.
     * <ul>
     *   <li>Given {@link BrandRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#updateBrand(int, BrandRequest)}
     */
    @Test
    @DisplayName("Test updateBrand(int, BrandRequest); given BrandRepository findById(Object) return empty; then throw AppException")
    void testUpdateBrand_givenBrandRepositoryFindByIdReturnEmpty_thenThrowAppException() {
        // Arrange
        Optional<Brand> emptyResult = Optional.empty();
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.updateBrand(1, new BrandRequest()));
        verify(brandRepository).findById(eq(1));
    }

    /**
     * Test {@link BrandService#updateBrand(int, BrandRequest)}.
     * <ul>
     *   <li>Given {@link BrandRepository} {@link CrudRepository#save(Object)} return
     * {@link Brand#Brand()}.</li>
     *   <li>Then return {@code Name}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#updateBrand(int, BrandRequest)}
     */
    @Test
    @DisplayName("Test updateBrand(int, BrandRequest); given BrandRepository save(Object) return Brand(); then return 'Name'")
    void testUpdateBrand_givenBrandRepositorySaveReturnBrand_thenReturnName() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);

        Brand brand2 = new Brand();
        brand2.setDescription("The characteristics of someone or something");
        brand2.setId(1);
        brand2.setName("Name");
        brand2.setProducts(new ArrayList<>());
        brand2.setStatus(true);
        when(brandRepository.save(Mockito.<Brand>any())).thenReturn(brand2);
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        BrandResponse buildResult = BrandResponse.builder()
                .description("The characteristics of someone or something")
                .id(1)
                .name("Name")
                .status(true)
                .build();
        when(brandMapper.toBrandResponse(Mockito.<Brand>any())).thenReturn(buildResult);
        doNothing().when(brandMapper).updateBrand(Mockito.<Brand>any(), Mockito.<BrandRequest>any());

        // Act
        BrandResponse actualUpdateBrandResult = brandService.updateBrand(1, new BrandRequest());

        // Assert
        verify(brandMapper).toBrandResponse(isA(Brand.class));
        verify(brandMapper).updateBrand(isA(Brand.class), isA(BrandRequest.class));
        verify(brandRepository).findById(eq(1));
        verify(brandRepository).save(isA(Brand.class));
        assertEquals("Name", actualUpdateBrandResult.getName());
        assertEquals("The characteristics of someone or something", actualUpdateBrandResult.getDescription());
        assertEquals(1, actualUpdateBrandResult.getId().intValue());
        assertTrue(actualUpdateBrandResult.getStatus());
    }

    /**
     * Test {@link BrandService#deleteBrand(int)}.
     * <p>
     * Method under test: {@link BrandService#deleteBrand(int)}
     */
    @Test
    @DisplayName("Test deleteBrand(int)")
    void testDeleteBrand() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);
        doThrow(new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION)).when(brandRepository).delete(Mockito.<Brand>any());
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.deleteBrand(1));
        verify(brandRepository).delete(isA(Brand.class));
        verify(brandRepository).findById(eq(1));
    }

    /**
     * Test {@link BrandService#deleteBrand(int)}.
     * <ul>
     *   <li>Given {@link BrandRepository} {@link CrudRepository#delete(Object)} does
     * nothing.</li>
     *   <li>Then calls {@link CrudRepository#delete(Object)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#deleteBrand(int)}
     */
    @Test
    @DisplayName("Test deleteBrand(int); given BrandRepository delete(Object) does nothing; then calls delete(Object)")
    void testDeleteBrand_givenBrandRepositoryDeleteDoesNothing_thenCallsDelete() {
        // Arrange
        Brand brand = new Brand();
        brand.setDescription("The characteristics of someone or something");
        brand.setId(1);
        brand.setName("Name");
        brand.setProducts(new ArrayList<>());
        brand.setStatus(true);
        Optional<Brand> ofResult = Optional.of(brand);
        doNothing().when(brandRepository).delete(Mockito.<Brand>any());
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        brandService.deleteBrand(1);

        // Assert that nothing has changed
        verify(brandRepository).delete(isA(Brand.class));
        verify(brandRepository).findById(eq(1));
    }

    /**
     * Test {@link BrandService#deleteBrand(int)}.
     * <ul>
     *   <li>Given {@link BrandRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     *   <li>Then throw {@link AppException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BrandService#deleteBrand(int)}
     */
    @Test
    @DisplayName("Test deleteBrand(int); given BrandRepository findById(Object) return empty; then throw AppException")
    void testDeleteBrand_givenBrandRepositoryFindByIdReturnEmpty_thenThrowAppException() {
        // Arrange
        Optional<Brand> emptyResult = Optional.empty();
        when(brandRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(AppException.class, () -> brandService.deleteBrand(1));
        verify(brandRepository).findById(eq(1));
    }
}
