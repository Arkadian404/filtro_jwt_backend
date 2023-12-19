package com.ark.security.config;

import com.ark.security.auth.AuthenticationResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractSubject(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public ArrayList<?> extractRole(String token){
        return extractClaim(token, claims -> claims.get("role", ArrayList.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaimByObject(AuthenticationResponse authResp, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaimsByObject(authResp);
        return claimsResolver.apply(claims);
    }

    public String generatePasswordResetToken(String email){
        long expiration = System.currentTimeMillis() + 1000 * 60 * 60;
        Date expirationDate = new Date(expiration);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(Map.of("role", userDetails.getAuthorities()
                .stream().map(Object::toString).collect(Collectors.toList()),
                "username", userDetails.getUsername()), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ){
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    public String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isValidToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

   public boolean isValidPasswordResetToken(String token){
        return !isTokenExpired(token);
    }


    public boolean isTokenExpired(String token) throws ExpiredJwtException{
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException{
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigninKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Claims extractAllClaimsByObject(AuthenticationResponse authResp) throws ExpiredJwtException{
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigninKey())
            .build()
            .parseClaimsJws(authResp.getAccessToken())
            .getBody();
    }

    private Key getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
