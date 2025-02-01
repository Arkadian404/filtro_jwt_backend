package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.VoucherRequest;
import com.ark.security.dto.response.VoucherResponse;

import com.ark.security.service.VoucherService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/voucher")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminVoucherController {
    private final VoucherService voucherService;


    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    @GetMapping
    public ApiResponse<List<VoucherResponse>> getAllVoucher(){
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getAllVouchers())
                .build();
    }

    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    @GetMapping("/{id}")
    public ApiResponse<VoucherResponse> find(@PathVariable int id){
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucherByVoucherId(id))
                .build();
    }

    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    @PostMapping
    public ApiResponse<VoucherResponse> create(@Valid @RequestBody VoucherRequest voucher){
        return ApiResponse.<VoucherResponse>builder()
                .message("Tạo voucher thành công")
                .result(voucherService.create(voucher))
                .build();
    }

    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    @PutMapping("/{id}")
    public ApiResponse<VoucherResponse> update(@PathVariable int id, @Valid @RequestBody VoucherRequest voucher){
        return ApiResponse.<VoucherResponse>builder()
                .message("Cập nhật voucher thành công")
                .result(voucherService.update(id, voucher))
                .build();
    }

    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable int id){
        voucherService.delete(id);
        return ApiResponse.<String>builder()
                .message("Xóa voucher thành công")
                .build();
    }

}
