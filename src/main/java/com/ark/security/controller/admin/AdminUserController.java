package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.UserRequest;
import com.ark.security.dto.response.UserResponse;
import com.ark.security.service.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/api/v1/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<List<UserResponse>> getList(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ApiResponse<UserResponse> find(@PathVariable int id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest user){
        return ApiResponse.<UserResponse>builder()
                .message("Tạo tài khoản thành công")
                .result(userService.createUser(user))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<UserResponse> update(@PathVariable int id,
                                    @Valid @RequestBody UserRequest user){
        return ApiResponse.<UserResponse>builder()
                .message("Cập nhật tài khoản thành công")
                .result(userService.updateUser(id, user))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ApiResponse<String > delete(@PathVariable int id){
        userService.deleteUser(id);
        return ApiResponse.<String >builder()
                .message("Xóa tài khoản thành công")
                .build();
    }

    @PostMapping("/change-password/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ApiResponse<String> changePassword(
            @PathVariable int id,
            @Valid @RequestBody Map<String, String> body
            ){
        userService.changePassword(id, body.get("oldPassword"), body.get("newPassword"));
        return ApiResponse.<String>builder()
                .message("Đổi mật khẩu thành công")
                .build();
    }

}
