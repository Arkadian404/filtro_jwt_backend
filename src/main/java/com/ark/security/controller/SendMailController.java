package com.ark.security.controller;

import com.ark.security.exception.BadCredentialsException;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.service.EmailDetailsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class SendMailController {
    private final EmailDetailsService emailDetailsService;


    @PostMapping("/send-order-mail")
    public ResponseEntity<?> processSendOrderMail(@RequestParam("email") String email, @RequestBody String template){
        try{
            emailDetailsService.sendOrderInformationEmail(email, template);
        } catch (UnsupportedEncodingException | MessagingException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new BadCredentialsException("Lỗi khi gửi mail");
        }
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Gửi mail thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }
}
