package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.dto.OrderDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.order.Order;

import com.ark.security.models.payment.momo.MomoResponse;
import com.ark.security.models.payment.vnpay.VNPResponse;
import com.ark.security.models.user.User;
import com.ark.security.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Date;

@RestController
@RequestMapping("/api/v1/user/order")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ShippingMethodService shippingMethodService;
    private final AuthenticationService authenticationService;
    private final MomoService momoService;
    private final VNPayService vnpayService;



    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable int userId){
        return ResponseEntity.ok(orderService.getOrdersDtoByUserId(userId));
    }


    @GetMapping("/get/orderCode/{orderCode}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getOrderByOrderCode(@PathVariable String orderCode){
        return ResponseEntity.ok(orderService.getOrderByOrderCode(orderCode));
    }

    @GetMapping("/get/orderDetail/{orderId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable int orderId){
        return ResponseEntity.ok(orderDetailService.getOrderDetailDtoByOrderId(orderId));
    }

    @GetMapping("/get/shippingMethods")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getShippingMethods(){
        return ResponseEntity.ok(shippingMethodService.getAllShippingMethods());
    }

    @PostMapping("/cancelOrder/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> cancelOrder(@PathVariable int id){
        orderService.cancelOrder(id);
        SuccessMessage successMessage = SuccessMessage.builder()
                .statusCode(200)
                .message("Hủy đơn hàng thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(successMessage);
    }

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create', 'user:create')")
    public ResponseEntity<?> placeOrder(@RequestBody Order order,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        User user = authenticationService.getCurrentUser(request, response);
        OrderDto userOrder = orderService.createOrder(user, order);
        return ResponseEntity.ok(userOrder);
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
