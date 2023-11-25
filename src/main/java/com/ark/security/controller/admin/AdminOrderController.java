package com.ark.security.controller.admin;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.order.Order;
import com.ark.security.service.OrderDetailService;
import com.ark.security.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class AdminOrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/get/all")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrdersByOrderDate());
    }

    @GetMapping("/get/orderDetail/{orderId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable int orderId){
        return ResponseEntity.ok(orderDetailService.getOrderDetailByOrderId(orderId));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update', 'user:update')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable int id,
                                               @RequestBody Order order){
        orderService.updateOrder(id, order);
        SuccessMessage success = SuccessMessage.builder()
                .statusCode(200)
                .message("Cập nhật trạng thái đơn hàng thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok().body(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete', 'user:delete')")
    public ResponseEntity<?> deleteOrder(@PathVariable int id){
        orderService.deleteOrder(id);
        SuccessMessage success = SuccessMessage.builder()
                .statusCode(200)
                .message("Xóa đơn hàng thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok().body(success);
    }
}
