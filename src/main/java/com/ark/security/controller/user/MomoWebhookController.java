package com.ark.security.controller.user;

import com.ark.security.models.payment.MomoIPN;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/momo/webhook")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MomoWebhookController {
    private final Logger logger = LoggerFactory.getLogger(MomoWebhookController.class);

    @PostMapping("/ipn")
    public ResponseEntity<String> momoWebhook(@RequestBody String request, HttpServletRequest httpReq){
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(request);
        System.out.println(httpReq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
