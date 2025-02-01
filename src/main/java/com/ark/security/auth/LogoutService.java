package com.ark.security.auth;

import com.ark.security.config.JwtService;
import com.ark.security.service.CacheService;
import com.ark.security.service.user.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final CacheService cacheService;

    private static final String BLACKLIST_KEY = "jwt:blacklist:";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return;
            }
            jwt = authHeader.substring(7);
            if(!jwtService.isTokenExpired(jwt)){
                Date expiration = jwtService.extractExpiration(jwt);
                long ttlInMillis = expiration.getTime() - System.currentTimeMillis();
                if(ttlInMillis > 0){
                    cacheService.getOrSetCache(
                            BLACKLIST_KEY+jwt,
                            "blacklisted",
                            String.class,
                            ttlInMillis,
                            TimeUnit.MILLISECONDS
                    );
                }
            }


//            var storedToken = tokenService.getByToken(jwt).orElse(null);
//            if(storedToken != null){
//                storedToken.setRevoked(true);
//                storedToken.setExpired(true);
//                tokenService.save(storedToken);
//
//            }
    }


    public boolean isTokenBlacklisted(String token){
        return Boolean.TRUE.equals(cacheService.hasKey(token));
    }
}

