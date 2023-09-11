//package com.ark.security.config;
//
//import com.ark.security.auth.AuthenticationService;
//import com.ark.security.auth.CookieService;
//import com.ark.security.token.TokenService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LogoutService implements LogoutHandler {
//    private final TokenService tokenService;
//    private final CookieService cookieService;
//
//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        jwt = authHeader.substring(7);
//        var storedToken = tokenService.getByToken(jwt).orElse(null);
//        if(storedToken != null) {
//            storedToken.setRevoked(true);
//            storedToken.setExpired(true);
//            tokenService.save(storedToken);
//            cookieService.removeCookie("accessToken", response);
//            cookieService.removeCookie("refreshToken", response);
//        }
//    }
//}
