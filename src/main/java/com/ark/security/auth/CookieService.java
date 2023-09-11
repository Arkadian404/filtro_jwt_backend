//package com.ark.security.auth;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CookieService {
//
//    public Cookie createCookie(String name, String value, int maxAge, String path){
//        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setPath(path);
//        cookie.setMaxAge(maxAge);
//        return cookie;
//    }
//
//    public void removeCookie(String name, HttpServletResponse response){
//        Cookie cookie = new Cookie(name, "");
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//    }
//}
