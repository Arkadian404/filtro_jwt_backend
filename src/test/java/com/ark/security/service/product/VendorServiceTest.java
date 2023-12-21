package com.ark.security.service.product;

import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Vendor;
import com.ark.security.repository.product.VendorRepository;
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
import static org.mockito.Mockito.when;

class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveVendor() {
        Vendor vendor = new Vendor();
        vendor.setName("Test Vendor");
        when(vendorRepository.existsVendorByName(anyString())).thenReturn(false);

        vendorService.saveVendor(vendor);

        verify(vendorRepository, times(1)).save(vendor);
    }

    @Test
    void testSaveVendorDuplicate() {
        Vendor vendor = new Vendor();
        vendor.setName("Test Vendor");
        when(vendorRepository.existsVendorByName(anyString())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> vendorService.saveVendor(vendor));
    }

    @Test
    void testGetAllVendor() {
        when(vendorRepository.findAll()).thenReturn(Collections.singletonList(new Vendor()));

        assertNotNull(vendorService.getAllVendor());

        assertEquals(1, vendorService.getAllVendor().size());
//        verify(vendorRepository, times(1)).findAll();
    }

    @Test
    void testGetAllVendorEmpty() {
        when(vendorRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> vendorService.getAllVendor());
    }

    @Test
    void testGetVendorById() {
        Vendor vendor = new Vendor();
        vendor.setId(1);
        when(vendorRepository.findById(anyInt())).thenReturn(Optional.of(vendor));

        Vendor result = vendorService.getVendorById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetVendorByIdNotFound() {
        when(vendorRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vendorService.getVendorById(1));
    }

    @Test
    void testUpdateVendor() {
        Vendor vendor = new Vendor();
        vendor.setId(1);
        when(vendorRepository.findById(anyInt())).thenReturn(Optional.of(vendor));

        vendorService.updateVendor(1, vendor);

        verify(vendorRepository, times(1)).save(vendor);
    }

    @Test
    void testDeleteVendor() {
        Vendor vendor = new Vendor();
        vendor.setId(1);
        when(vendorRepository.existsById(anyInt())).thenReturn(true);

        vendorService.deleteVendor(1);

        verify(vendorRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteVendorNotFound() {
        when(vendorRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> vendorService.deleteVendor(1));
    }

}