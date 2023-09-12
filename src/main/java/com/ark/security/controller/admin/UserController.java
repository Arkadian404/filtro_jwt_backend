package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping ("/api/v1/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class UserController {
    private final UserService userService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getList(){
        List<User> users = userService.getUsersByRole(Role.USER);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ResponseEntity<?> create(@RequestBody User user){
        userService.saveUser(user);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tạo tài khoản thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody User user){
        userService.updateUser(id, user);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật tài khoản thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        userService.deleteUser(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa tài khoản thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

}
