package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.VendorRequest;
import com.ark.security.dto.response.VendorResponse;
import com.ark.security.service.product.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/vendor")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorTestService;

    @GetMapping
    public ApiResponse<List<VendorResponse>> getAll(){
        return ApiResponse.<List<VendorResponse>>builder()
                .result(vendorTestService.getAllVendors())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<VendorResponse> getById(@PathVariable int id){
        return ApiResponse.<VendorResponse>builder()
                .result(vendorTestService.getVendorById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<VendorResponse> create(@RequestBody @Valid VendorRequest request){
        return ApiResponse.<VendorResponse>builder()
                .result(vendorTestService.createVendor(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<VendorResponse> update(@PathVariable int id, @RequestBody @Valid VendorRequest request){
        return ApiResponse.<VendorResponse>builder()
                .result(vendorTestService.updateVendor(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable int id){
        vendorTestService.deleteVendor(id);
        return ApiResponse.<String>builder()
                .result("Vendor deleted successfully")
                .build();
    }
}
