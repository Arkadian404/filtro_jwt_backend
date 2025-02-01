package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.UserInfoRequest;
import com.ark.security.dto.request.UserRequest;
import com.ark.security.dto.response.UserResponse;
import com.ark.security.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/user-info")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
public class UserInfoController {
    private final UserService userTestService;

    @GetMapping("/current-user")
    public ApiResponse<UserResponse> getCurrentUser(){
        return ApiResponse.<UserResponse>builder()
                .result(userTestService.getCurrentUser())
                .build();
    }

    @PostMapping("/update")
    public ApiResponse<UserResponse> updateCurrentUser(@RequestBody @Valid UserInfoRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Cập nhật tài khoản thành công")
                .result(userTestService.updateCurrentUser(request))
                .build();
    }

    @PostMapping("/change-password/{id}")
    public ApiResponse<String> changePassword(
            @PathVariable int id,
            @Valid  @RequestBody Map<String, String> body
    ){
        userTestService.changePassword(id, body.get("oldPassword"), body.get("newPassword"));
        return ApiResponse.<String>builder()
                .result("Đổi mật khẩu thành công")
                .build();
    }

}
