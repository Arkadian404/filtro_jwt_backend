package com.ark.security.controller.admin;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.OrderRequest;
import com.ark.security.dto.response.OrderDetailResponse;
import com.ark.security.dto.response.OrderResponse;
import com.ark.security.service.OrderDetailService;
import com.ark.security.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminOrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ApiResponse<List<OrderResponse>> getAllOrders(){
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    @GetMapping("/{orderId}/details")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ApiResponse<List<OrderDetailResponse>> getOrderDetailByOrderId(@PathVariable int orderId){
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .result(orderDetailService.getOrderDetailResponseByOrderId(orderId))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update', 'user:update')")
    public ApiResponse<OrderResponse> updateOrderStatus(@PathVariable int id,
                                               @RequestBody OrderRequest order){
        orderService.updateOrder(id, order);
        return ApiResponse.<OrderResponse>builder()
                .message("Cập nhật đơn hàng thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete', 'user:delete')")
    public ApiResponse<String> deleteOrder(@PathVariable int id){
        orderService.deleteOrder(id);
        return ApiResponse.<String>builder()
                .message("Xóa đơn hàng thành công")
                .build();
    }
}
