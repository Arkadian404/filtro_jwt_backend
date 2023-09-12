package com.ark.security.auth;


import com.ark.security.config.JwtService;
import com.ark.security.models.user.User;
import com.ark.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest registrationRequest
    ){
        return ResponseEntity.ok(service.register(registrationRequest));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
        try{
            AuthenticationResponse authenticate = service.authenticate(authenticationRequest);
            return ResponseEntity.ok(authenticate);
        }
        catch (BadCredentialsException ex){
            return ResponseEntity.badRequest().body("Sai tên đăng nhập hoặc mật khẩu");
        }

    }

    @GetMapping("/current-user")
    public ResponseEntity<?>getCurrentUser(HttpServletRequest request,
                                           HttpServletResponse response){
//        Optional<User> user = userService.getByUsername(authentication.getName());
//        if(user.isPresent()){
//            return ResponseEntity.ok(user.get());
//        }
//        return ResponseEntity.badRequest().body("Không tìm thấy tài khoản");
        User user = service.getCurrentUser(request, response);
        if(user!=null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("THUA");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody RefreshToken refreshToken
    ){
       return ResponseEntity.ok(service.refreshToken(refreshToken.getRefreshToken()));
    }

    @GetMapping("/logout")
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.logout(request, response);
    }

}
