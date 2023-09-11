package com.ark.security.controller.admin;

import com.ark.security.models.user.Role;
import com.ark.security.models.user.User;
import com.ark.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class UserController {
    private final UserService userService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(userService.getUsersByRole(Role.USER));
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> find(@PathVariable int id){
        User user = userService.getUserById(id);
        if(user == null){
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng: "+id);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create')")
    public ResponseEntity<?> create(@RequestBody User user){
        if(user == null){
            return ResponseEntity.badRequest().body("Tài khoản không được trống");
        }
        userService.saveUser(user);
        return ResponseEntity.ok("Tạo tài khoản thành công");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update')")
    public ResponseEntity<?> update(@PathVariable int id,
                                    @RequestBody User user){
        User oldUser = userService.getUserById(id);
        if(oldUser == null){
            return ResponseEntity.badRequest().body("Không tìm thấy tài khoản: "+ id);
        }
        if(user == null){
            return ResponseEntity.badRequest().body("Tài khoản không được trống");
        }
        userService.updateUser(id, user);
        return ResponseEntity.ok("Cập nhật tài khoản thành công");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        User user = userService.getUserById(id);
        if(user == null){
            return ResponseEntity.badRequest().body("Không tìm thấy tài khoản: "+ id);
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("Xóa tài khoản thành công");
    }

}
