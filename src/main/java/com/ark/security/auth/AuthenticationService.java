package com.ark.security.auth;

import com.ark.security.exception.PasswordNotMatchException;
import com.ark.security.exception.BadCredentialsException;
import com.ark.security.models.token.Token;
import com.ark.security.service.TokenService;
import com.ark.security.models.token.TokenType;
import com.ark.security.models.user.User;
import com.ark.security.config.JwtService;
import com.ark.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request){
        if(userService.existsUserByUsername(request.getUsername())){
            throw new BadCredentialsException("Username already exists: " + request.getUsername());
        }
        if(userService.existsUserByEmail(request.getEmail())){
            throw new BadCredentialsException("Email already exists: " + request.getEmail());
        }
        if(!userService.matchPassword(request.getPassword(), request.getConfirmPassword())){
            throw new PasswordNotMatchException("Password and confirm password does not match");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userService.saveUser(user);
//        var token = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        saveUserToken(savedUser, token);
//        return AuthenticationResponse.builder()
//                .accessToken(token)
//                .refreshToken(refreshToken)
//                .build();
        return "User registered successfully";
    }


    public  AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = this.userService.getByUsername(request.getUsername());
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, token);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String token) {
        var savedToken = Token.builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(savedToken);
    }

    public User getCurrentUser(HttpServletRequest request, HttpServletResponse response){
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            var username = jwtService.extractUsername(jwt);
            if(username!=null){
                return userService.getByUsername(username);
            }
        }
        return null;
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenService.getAllValidTokenByUserId(user.getId());
        if(validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token ->{
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenService.saveAll(validUserTokens);
    }

//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        final String authHeader = request.getHeader(AUTHORIZATION);
//        final String refreshToken;
//        final String username;
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        username = jwtService.extractUsername(refreshToken);
//        if(username!=null){
//            var user = this.userService.getByUsername(username).orElseThrow();
//            if(jwtService.isValidToken(refreshToken, user)){
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }

    public AuthenticationResponse refreshToken(String refreshToken){
        final String username;
        username = jwtService.extractUsername(refreshToken);
        if(username!=null){
            var user = userService.getByUsername(username);
            if(jwtService.isValidToken(refreshToken, user)){
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        return null;
    }


    public void logout(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenService.getByToken(jwt).orElse(null);
        if(storedToken != null){
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenService.save(storedToken);
        }

    }

}
