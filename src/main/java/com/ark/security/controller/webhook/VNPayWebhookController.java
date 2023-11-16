package com.ark.security.controller.webhook;

import com.ark.security.models.payment.vnpay.VNPIPNResponse;
import com.ark.security.service.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/vnpay/webhook/ipn")
@RequiredArgsConstructor
public class VNPayWebhookController {
    private final VNPayService vnPayService;

    @GetMapping
    public ResponseEntity<VNPIPNResponse> getVNPayWebhook(@RequestParam Map<String , String> params){
        VNPIPNResponse response = vnPayService.processIpn(params);
        return ResponseEntity.ok(response);
    }

}
