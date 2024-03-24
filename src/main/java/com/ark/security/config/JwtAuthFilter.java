package com.ark.security.config;

import com.ark.security.service.user.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String authHeader = request.getHeader(AUTHORIZATION);
//        String jwt = null;
//        String username = null;
//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            jwt = authHeader.substring(7);
//            try{
//                username = jwtService.extractUsername(jwt);
//            }catch (IllegalArgumentException ex){
//                System.out.println("Unable to get JWT Token");
//            }catch (ExpiredJwtException ex){
//                System.out.println("JWT Token has expired");
//            }catch(MalformedJwtException ex){
//                System.out.println("JWT Token has been tampered");
//            }catch(SignatureException ex){
//                System.out.println("JWT Signature does not match locally computed signature");
//            }
//        }else{
//            logger.warn("JWT Token does not begin with Bearer String");
//
//        }

        try{
            String jwt = parseJwt(request);
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
