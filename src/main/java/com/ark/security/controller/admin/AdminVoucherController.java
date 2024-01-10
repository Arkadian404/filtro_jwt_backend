package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Voucher;

import com.ark.security.service.VoucherService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/voucher")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminVoucherController {
    private final VoucherService voucherService;


    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    @GetMapping("/getList")
    public ResponseEntity<?> getAllVoucher(){
        return ResponseEntity.ok(voucherService.getAllVoucher());
    }

    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable int id){
        return ResponseEntity.ok(voucherService.getVoucherById(id));
    }

    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody Voucher voucher){
        voucherService.saveVoucher(voucher);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo voucher thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody Voucher voucher){
        voucherService.updateVoucher(id, voucher);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật voucher thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        voucherService.deleteVoucher(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa voucher thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

}
