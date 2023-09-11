package com.ark.security.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(PasswordNotMatchException.class)
    //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handlePasswordNotMatchException(PasswordNotMatchException ex, WebRequest request){
            var error = ErrorMessage.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .description(request.getDescription(false))
                    .timestamp(new Date())
                    .build();
            log.error("Error: {}", error.getDescription());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    //@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleUserNotFound(BadCredentialsException ex, WebRequest request){
            var error = ErrorMessage.builder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message(ex.getMessage())
                    .description(request.getDescription(false))
                    .timestamp(new Date())
                    .build();
            log.error("Error: {}", error.getDescription());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest request){
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        log.error("Error: {}", error.getDescription());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        String error = "Nội dung không được bỏ trống!!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

//    @ExceptionHandler(Exception.class)
//    //@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<?> handleException(Exception ex, WebRequest request){
//        var error = ErrorMessage.builder()
//                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .message(ex.getMessage())
//                .description(request.getDescription(false))
//                .timestamp(new Date())
//                .build();
//        log.error("Error: {}", error.getDescription());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//
//    }

}

