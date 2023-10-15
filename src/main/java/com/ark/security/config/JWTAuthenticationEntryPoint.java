package com.ark.security.config;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        response.setContentType("application/json");
        response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
        // Create a JSON response message
//        String jsonResponse = "{\"message\":\"You have to log in to access this resource\"}";

        // Write the JSON response message to the response output stream
//        response.getWriter().write(jsonResponse);


//        // Set the response status to 401 (Unauthorized)
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        // Set the response content type to JSON
//        response.setContentType("application/json");
//
//        // Create a JSON response message
//        String jsonResponse = "{\"message\":\"JWTAuthenticationEntryPoint: You have to log in to access this resource \"}";
//
//        // Write the JSON response message to the response output stream
//        response.getWriter().write(jsonResponse);
    }


}
