package com.ark.security.config;

import com.ark.security.auth.LogoutService;
import com.ark.security.dto.ApiResponse;
import com.ark.security.exception.ErrorCode;
import com.ark.security.service.user.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final LogoutService logoutService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getServletPath());
        try{
            String jwt =parseJwt(request);
            if(logoutService.isTokenBlacklisted(jwt)){
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ApiResponse<?> api = ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build();
                response.getWriter().write(objectMapper.writeValueAsString(api));
                response.flushBuffer();
            }
            String username = jwtService.extractUsername(jwt);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                var isTokenValid = tokenService.getByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if(jwtService.isValidToken(jwt, userDetails) && isTokenValid){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch (IllegalArgumentException ex) {
            logger.error("Unable to get JWT Token");
        }catch (ExpiredJwtException ex) {
            logger.error("JWT Token has expired");
        }catch (MalformedJwtException ex) {
            logger.error("JWT Token has been tampered");
        }catch (SignatureException ex) {
            logger.error("JWT Signature does not match locally computed signature");
        }
        filterChain.doFilter(request, response);
    }



    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
