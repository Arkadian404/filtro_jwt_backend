package com.ark.security.controller;

import com.ark.security.config.JwtService;
import com.ark.security.exception.BadCredentialsException;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.user.User;
import com.ark.security.service.EmailDetailsService;
import com.ark.security.service.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.Utility;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth/forgot-password")
@RequiredArgsConstructor
public class ResetPasswordController {
    private final UserService userService;
    private final EmailDetailsService emailDetailsService;
    private final JwtService jwtService;


    @PostMapping("/send-mail")
    public ResponseEntity<?> processForgotPassword(@RequestParam("email") String email, HttpServletRequest request){
        try{
            String token = jwtService.generatePasswordResetToken(email);
            String resetPasswordLink = Utility.CLIENT_SITE_URL + "/reset-password"+"?token=" + token;
            emailDetailsService.sendMail(email, resetPasswordLink);
        } catch (UnsupportedEncodingException | MessagingException ex){
            System.out.println(ex.getMessage());
            throw new BadCredentialsException("Lỗi khi gửi mail");
        }
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Gửi mail thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }




    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam(value ="token") String token,
                                           @RequestBody String newPassword){
        try{
            if(jwtService.isValidPasswordResetToken(token)){
                String email = jwtService.extractSubject(token);
                User user = userService.getByEmail(email);
                userService.updateUserPassword(user.getId(), newPassword);
            }
        }
        catch (ExpiredJwtException ex){
            throw new BadCredentialsException("Token hết hạn, hãy gửi lại mail");
        }
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đặt lại mật khẩu thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }


}
