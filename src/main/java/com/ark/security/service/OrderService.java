package com.ark.security.service;

import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.user.User;
import com.ark.security.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;


    public boolean checkOrderCode(String orderCode){
        return orderRepository.existsByOrderCode(orderCode).orElse(false);
    }

    public Order getOrderByOrderCode(String orderCode){
        return orderRepository.findByOrderCode(orderCode).orElse(null);
    }

    public Order getOrderById(Integer id){
        return orderRepository.findById(id).orElse(null);
    }

    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(int id){
        return orderRepository.findAllByUserId(id).orElse(null);
    }

    public Order createOrder(User user, Order order){
        Cart cart = cartService.getCartByUsername(user.getUsername());
        List<CartItem> cartItems = cart.getCartItems();
        order.setOrderCode(RandomStringUtils.random(10, true, true));
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItem ci : cartItems){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductDetail(ci.getProductDetail());
            orderDetail.setQuantity(ci.getQuantity());
            orderDetail.setPrice(ci.getPrice());
            orderDetail.setTotal(ci.getTotal());
            orderDetail.setOrderDate(LocalDateTime.now());
            orderDetails.add(orderDetail);
            orderDetailService.saveOrderDetail(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        cart.setStatus(false);
        cartService.saveCart(cart);
        return order;
    }



}
