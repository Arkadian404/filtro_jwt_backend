package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.user.User;
import com.ark.security.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/user-info")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
public class UserInfoController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/current-user")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authenticationService.getCurrentUser(request, response));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update', 'user:update')")
    public ResponseEntity<?> update(@PathVariable int id,@Valid @RequestBody User user){
        userService.updateUser(id, user);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật tài khoản thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PostMapping("/change-password/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update', 'user:update')")
    public ResponseEntity<?> changePassword(
            @PathVariable int id,
            @Valid  @RequestBody Map<String, String> body
    ){
        userService.changePassword(id, body.get("oldPassword"), body.get("newPassword"));
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đổi mật khẩu thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

}
