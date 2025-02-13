package com.ark.security.handler;

import com.ark.security.dto.ApiResponse;
import com.ark.security.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import javax.naming.AuthenticationException;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    private static final String MIN_ATTRIBUTE = "min";
    private static final String MAX_ATTRIBUTE = "max";

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> handlePasswordNotMatchException(PasswordNotMatchException ex, WebRequest request){
            var error = ErrorMessage.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .description(request.getDescription(false))
                    .timestamp(new Date())
                    .build();
            logger.error("Error: {}", error.getDescription());
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
            logger.error("Error: {}", error.getDescription());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> handleHttpClientErrorException(AuthenticationException ex, WebRequest request){
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        logger.error("Error: {}", error.getDescription());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<?> handleMissingRequestBody(HttpMessageNotReadableException ex, WebRequest request) {
//        var error = ErrorMessage.builder()
//                .statusCode(HttpStatus.BAD_REQUEST.value())
//                .message("Nội dung không được bỏ trống!!")
//                .timestamp(new Date())
//                .description(request.getDescription(false))
//                .build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }


//    @ExceptionHandler(value = {UnsupportedEncodingException.class, MessagingException.class})
//    public ResponseEntity<?> handleUnsupportedEncodingException(Exception ex, WebRequest request){
//        var error = ErrorMessage.builder()
//                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .message("Lỗi khi gửi mail")
//                .description(request.getDescription(false))
//                .timestamp(new Date())
//                .build();
//        logger.error("Error: {}", error.getDescription());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest request){
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        logger.error("Error: {}", error.getDescription());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(UserStatusException.class)
    public ResponseEntity<?> handleUserStatusException(UserStatusException ex, WebRequest request){
        logger.error("UserStatus error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request){
        logger.error("UsernameNotFound error: {}", ex.getMessage());
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

    @ExceptionHandler(QuantityShortageException.class)
    public ResponseEntity<?> handleQuantityShortageException(QuantityShortageException ex, WebRequest request){
        logger.error("QuantityShortage error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(VoucherException.class)
    public ResponseEntity<?> handleVoucherException(VoucherException ex, WebRequest request){
        logger.error("Voucher error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex, WebRequest request){
        logger.error("Disabled error: {}", ex.getMessage());
        var error = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Tài khoản của bạn đã bị khóa!")
                .description(request.getDescription(false))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        String enumKey = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.KEY_INVALID;
        Map<String, Object> attributes = null;
        try{
            errorCode = ErrorCode.valueOf(enumKey);
            var constrainViolation = ex.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attributes = constrainViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(
                                Objects.nonNull(attributes)
                                        ? mapAttributes(errorCode.getMessage(), attributes)
                                        : errorCode.getMessage()
                        )
                        .build());
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request){
//        String errorMessage = ex.getConstraintViolations().stream()
//                .map(ConstraintViolation::getMessage)
//                .findFirst()
//                .orElse("Lỗi không xác định");
//        logger.error("ConstraintViolation error: {}", errorMessage);
//        var error = ErrorMessage.builder()
//                .statusCode(HttpStatus.BAD_REQUEST.value())
//                .message(errorMessage)
//                .description(request.getDescription(false))
//                .timestamp(new Date())
//                .build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }


    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<?>> handlerAppException(AppException ae) {
        return ResponseEntity.status(ae.getErrorCode().getStatus())
                .body(ApiResponse.builder()
                        .code(ae.getErrorCode().getCode())
                        .message(ae.getMessage())
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiResponse<?>> handleDIVE(DataIntegrityViolationException ex){
        String rootMsg = ex.getMostSpecificCause().getMessage();
        if(rootMsg != null){
            String message = DataUniqueError.getFriendlyMessage(rootMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.builder()
                            .message(message)
                            .build());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.builder()
                        .message("Data integrity violation")
                        .build());
    }

    private String mapAttributes(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue)
                .replace("{" + MAX_ATTRIBUTE + "}", maxValue);
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception ex, WebRequest request){
//        var error = ErrorMessage.builder()
//                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .message(ex.getMessage())
//                .description(request.getDescription(false))
//                .timestamp(new Date())
//                .build();
//        log.error("Error: {}", error.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//
//    }

}

