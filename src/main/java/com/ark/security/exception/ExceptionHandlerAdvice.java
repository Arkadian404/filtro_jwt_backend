package com.ark.security.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import javax.naming.AuthenticationException;

import java.io.IOException;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(PasswordNotMatchException.class)
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
    public ResponseEntity<?> handleMissingRequestBody(HttpMessageNotReadableException ex, WebRequest request) {
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Nội dung không được bỏ trống!!")
                .timestamp(new Date())
                .description(request.getDescription(false))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) throws IOException {
        logger.error("AccessDenied error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .message("Bạn không có ủy quyền để thực hiện hành động này")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request){
        logger.error("NotFound error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NullException.class)
    public ResponseEntity<?> handleNotNullException(NullException ex, WebRequest request){
        logger.error("NotNull error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> handleDuplicateException(DuplicateException ex, WebRequest request){
        logger.error("Duplicate error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
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

