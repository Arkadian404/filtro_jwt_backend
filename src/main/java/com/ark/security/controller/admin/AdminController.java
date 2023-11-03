package com.ark.security.controller.admin;

import com.ark.security.models.user.User;
import com.ark.security.service.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class AdminController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read')")
    public ResponseEntity<?> get(Authentication authentication){
        User user = userService.getByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }
}
