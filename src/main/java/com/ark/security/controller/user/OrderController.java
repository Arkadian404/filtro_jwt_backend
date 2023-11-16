package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.dto.OrderDto;
import com.ark.security.models.order.Order;
import com.ark.security.models.payment.momo.MomoResponse;
import com.ark.security.models.payment.vnpay.VNPResponse;
import com.ark.security.models.user.User;
import com.ark.security.service.MomoService;
import com.ark.security.service.OrderService;
import com.ark.security.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/user/order")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
public class OrderController {
    private final OrderService orderService;
    private final AuthenticationService authenticationService;
    private final MomoService momoService;
    private final VNPayService vnpayService;



    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable int userId){
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create', 'user:create')")
    public ResponseEntity<?> placeOrder(@RequestBody Order order,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        User user = authenticationService.getCurrentUser(request, response);
        return ResponseEntity.ok(orderService.createOrder(user, order));
    }


    @PostMapping("/placeMomoOrder")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create', 'user:create')")
    public ResponseEntity<MomoResponse> placeMomoOrder(@RequestBody OrderDto order){
        return ResponseEntity.ok(momoService.createMomoOrder(order));
    }


    @PostMapping("/placeVNPayOrder")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create', 'user:create')")
    public ResponseEntity<VNPResponse> placeMomoOrder(@RequestBody OrderDto orderDto, HttpServletRequest request){
        return ResponseEntity.ok(vnpayService.createVNPayOrder(orderDto, request));
    }

}
