package com.ark.security.exception;

public class JwtExpiredToken extends RuntimeException{
    public JwtExpiredToken(String message) {
        super(message);
    }
}
