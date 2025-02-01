package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.FlavorRequest;
import com.ark.security.dto.response.FlavorResponse;
import com.ark.security.service.product.FlavorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin/flavor")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
@RequiredArgsConstructor
public class AdminFlavorController {

    private final FlavorService flavorService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<FlavorResponse>> getFlavorList(){
        return ApiResponse.<List<FlavorResponse>>builder()
                .result(flavorService.getAllFlavors())
                .build();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<FlavorResponse> find(@PathVariable int id){
        return ApiResponse.<FlavorResponse>builder()
                .result(flavorService.getFlavorById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<FlavorResponse> create(@Valid @RequestBody FlavorRequest flavor){
        return ApiResponse.<FlavorResponse>builder()
                .message("Tạo hương vị thành công")
                .result(flavorService.saveFlavor(flavor))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<FlavorResponse> update(@PathVariable int id,@Valid @RequestBody FlavorRequest flavor){
        return ApiResponse.<FlavorResponse>builder()
                .message("Cập nhật hương vị thành công")
                .result(flavorService.updateFlavor(id, flavor))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ApiResponse<String> delete(@PathVariable int id){
        flavorService.deleteFlavor(id);
        return ApiResponse.<String>builder()
                .message("Xóa hương vị thành công")
                .build();
    }
}
