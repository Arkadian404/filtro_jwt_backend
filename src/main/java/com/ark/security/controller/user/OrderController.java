package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.OrderRequest;
import com.ark.security.dto.response.OrderDetailResponse;
import com.ark.security.dto.response.OrderResponse;

import com.ark.security.models.payment.momo.MomoResponse;
import com.ark.security.models.payment.vnpay.VNPResponse;
import com.ark.security.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/user/order")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
public class OrderController {
    private final OrderService orderTestService;
    private final OrderDetailService orderDetailTestService;
    private final MomoService momoService;
    private final VNPayService vnpayService;


    @GetMapping("/user/{id}")
    public ApiResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable int id){
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderTestService.getOrdersByUserId(id))
                .build();
    }

    @GetMapping("/code/{orderCode}")
    public ApiResponse<OrderResponse> getOrderByOrderCode(@PathVariable String orderCode){
        return ApiResponse.<OrderResponse>builder()
                .result(orderTestService.getOrderByOrderCode(orderCode))
                .build();
    }

    @GetMapping("/{id}/items")
    public ApiResponse<List<OrderDetailResponse>> getOrderDetailByOrderId(@PathVariable int id){
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailTestService.getOrderDetailResponseByOrderId(id))
                .build();
    }


    @PostMapping("/place/order")
    public ApiResponse<OrderResponse> placeOrder(@RequestBody OrderRequest request){
        return ApiResponse.<OrderResponse>builder()
                .result(orderTestService.createOrder(request))
                .build();
    }


    @PostMapping("/place/MomoOrder")
    public ResponseEntity<MomoResponse> placeMomoOrder(@RequestBody OrderResponse order){
        return ResponseEntity.ok(momoService.createMomoOrder(order));
    }


    @PostMapping("/place/VNPayOrder")
    public ResponseEntity<VNPResponse> placeMomoOrder(@RequestBody OrderResponse orderDto, HttpServletRequest request){
        return ResponseEntity.ok(vnpayService.createVNPayOrder(orderDto, request));
    }

    @PostMapping("/cancel/{id}")
    public ApiResponse<String> cancelOrder(@PathVariable int id){
        orderTestService.cancelOrder(id);
        return ApiResponse.<String>builder()
                .result("Order canceled successfully")
                .build();
    }

}
