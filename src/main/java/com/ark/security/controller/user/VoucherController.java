package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.response.VoucherResponse;
import com.ark.security.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherTestService;

    @GetMapping
    public ApiResponse<List<VoucherResponse>> getAllVouchers(){
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherTestService.getAllVouchers())
                .build();
    }


    @GetMapping("/available/{id}")
    public ApiResponse<List<VoucherResponse>> getAvailableVouchersByProductId(@PathVariable int id){
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherTestService.availableVouchersByProductId(id))
                .build();
    }

    @GetMapping("/available/all")
    public ApiResponse<List<VoucherResponse>> getAvailableVouchers(){
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherTestService.availableVouchersToAllProducts())
                .build();
    }

    @GetMapping("/check/{id}")
    public ApiResponse<Boolean> checkVoucher(@PathVariable int id){
        return ApiResponse.<Boolean>builder()
                .result(voucherTestService.isVoucherExpired(id))
                .build();
    }

    @PostMapping("/apply")
    public ApiResponse<String> applyVoucher(@RequestBody String code){
        voucherTestService.applyVoucher(code);
        return ApiResponse.<String>builder()
                .result("Áp dụng mã giảm giá thành công")
                .build();
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse<String> removeVoucher(@PathVariable int id){
        voucherTestService.removeVoucher(id);
        return ApiResponse.<String>builder()
                .result("Xóa mã giảm giá thành công")
                .build();
    }



}
