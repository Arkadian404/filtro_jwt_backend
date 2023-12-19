package com.ark.security.auth;


import com.ark.security.config.JwtService;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Employee;
import com.ark.security.models.user.User;
import com.ark.security.service.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest registrationRequest
    ){
        service.register(registrationRequest);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đăng ký thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
            AuthenticationResponse authenticate = service.authenticate(authenticationRequest);
            return ResponseEntity.ok(authenticate);
    }
    @PostMapping("/authenticate-employee")
    public ResponseEntity<?> authenticateEmployee(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
            AuthenticationResponse authenticate = service.authenticateEmployee(authenticationRequest);
            return ResponseEntity.ok(authenticate);
    }



    @GetMapping("/current-user")
    public ResponseEntity<?>getCurrentUser(HttpServletRequest request,
                                           HttpServletResponse response){
        User user = service.getCurrentUser(request, response);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/current-employee")
    public ResponseEntity<?>getCurrentEmployee(HttpServletRequest request,
                                           HttpServletResponse response){
        Employee employee = service.getCurrentEmployee(request, response);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshToken refreshToken
    ){
       return ResponseEntity.ok(service.refreshToken(refreshToken.getRefreshToken()));
    }

}
