package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.VendorRequest;
import com.ark.security.dto.response.VendorResponse;
import com.ark.security.service.product.VendorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/vendor")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminVendorController {
    private final VendorService vendorService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<VendorResponse>> getAllVendor(){
        return ApiResponse.<List<VendorResponse>>builder()
                .result(vendorService.getAllVendors())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<VendorResponse> find(@PathVariable int id){
        return ApiResponse.<VendorResponse>builder()
                .result(vendorService.getVendorById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<VendorResponse> create(@Valid @RequestBody VendorRequest vendor){
        return ApiResponse.<VendorResponse>builder()
                .message("Tạo nhà cung cấp thành công")
                .result(vendorService.createVendor(vendor))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<VendorResponse> update(@PathVariable int id, @Valid @RequestBody VendorRequest vendor){
        return ApiResponse.<VendorResponse>builder()
                .message("Cập nhật nhà cung cấp thành công")
                .result(vendorService.updateVendor(id, vendor))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String> delete(@PathVariable int id){
        vendorService.deleteVendor(id);
        return ApiResponse.<String>builder()
                .message("Xóa nhà cung cấp thành công")
                .build();
    }
}
