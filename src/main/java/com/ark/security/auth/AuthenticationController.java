package com.ark.security.auth;


import com.ark.security.models.user.User;
import com.ark.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest registrationRequest
    ){
        return ResponseEntity.ok(service.register(registrationRequest));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest
    ){
        AuthenticationResponse authenticate = service.authenticate(authenticationRequest);
        return ResponseEntity.ok(authenticate);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?>getCurrentUser(Authentication authentication){
        User user = userService.getByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(user);
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
