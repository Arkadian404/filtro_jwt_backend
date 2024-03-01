package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.user.User;
import com.ark.security.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/user/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    private final AuthenticationService authenticationService;


    @GetMapping("/availableVouchers/{productId}")
    public ResponseEntity<?> getAvailableVouchers(@PathVariable int productId){
        return ResponseEntity.ok(voucherService.showAvailableVoucherByProductId(productId));
    }


    @PostMapping("/apply")
    public ResponseEntity<?> applyVoucher(@RequestBody String code,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        User user = authenticationService.getCurrentUser(request, response);
        voucherService.applyVoucher(user.getId(), code);
        var successMessage = SuccessMessage.builder()
                .statusCode(200)
                .message("Áp dụng mã giảm giá thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(successMessage);
    }

    @DeleteMapping("/remove/{voucherId}")
    public ResponseEntity<?> removeVoucher(@PathVariable int voucherId,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        User user = authenticationService.getCurrentUser(request, response);
        voucherService.removeVoucher(user.getId(), voucherId);
        var successMessage = SuccessMessage.builder()
                .statusCode(200)
                .message("Xóa mã giảm giá thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(successMessage);
    }
}
