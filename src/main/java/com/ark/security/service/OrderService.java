package com.ark.security.service;

import com.ark.security.dto.request.OrderRequest;
import com.ark.security.dto.response.OrderResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.OrderMapper;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartRepository;
import com.ark.security.repository.OrderDetailRepository;
import com.ark.security.repository.OrderRepository;
import com.ark.security.repository.product.ProductDetailRepository;
import com.ark.security.repository.product.ProductRepository;
import com.ark.security.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private Cart getCart(String username){
        return cartRepository.findByUserUsername(username).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }

    public List<OrderResponse> getAllOrders(){
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse getOrderByOrderCode(String orderCode){
        Order order = orderRepository.findByOrderCode(orderCode).orElse(null);
        return orderMapper.toOrderResponse(order);
    }

    public Order getOrderByCode(String orderCode){
        return orderRepository.findByOrderCode(orderCode).orElse(null);
    }

    public OrderResponse getOrderById(int id){
        Order order = orderRepository.findById(id).orElse(null);
        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(int id){
        List<Order> orders = orderRepository.findAllByUserId(id).orElse(null);
        if(orders == null){
            return Collections.emptyList();
        }
        return orders
                .stream()
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse createOrder(OrderRequest request){
        User user = getCurrentUser();
        Cart cart = getCart(user.getUsername());
        List<CartItem> cartItems = cart.getCartItems();
        checkCartItemQuantity(cartItems);
        updateProductStockAndSold(cartItems);
        Order order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setOrderCode(RandomStringUtils.random(10, true, true));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        List<OrderDetail> list = new ArrayList<>();
        for(CartItem ci : cartItems){
            OrderDetail orderDetail = convertToOrderDetail(ci, order);
            list.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        order.setOrderDetails(list);
        orderRepository.save(order);
        cart.setStatus(false);
        cartRepository.save(cart);
        OrderResponse response = orderMapper.toOrderResponse(order);
        response.setDiscount(cart.getVoucher() != null ? cart.getVoucher().getDiscount() : 0);
        return response;
    }

    public void updateOrder(int id, OrderRequest request){
        Order oldOrder = orderRepository.findById(id).orElse(null);
        Order order = orderMapper.toOrder(request);
        log.info(order.toString());
        if(oldOrder!=null){
            Cart cart = cartRepository.findByUserUsername(oldOrder.getUser().getUsername()).orElse(null);
            List<CartItem> cartItems = cart.getCartItems();
            if(order.getStatus().equals(OrderStatus.CANCELED) || order.getStatus().equals(OrderStatus.FAILED)){
                rollbackProductStockAndSold(cartItems);
            }
            oldOrder.setStatus(order.getStatus());
            orderRepository.save(oldOrder);
        }
    }

    private static OrderDetail convertToOrderDetail(CartItem ci, Order order) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProductDetail(ci.getProductDetail());
        orderDetail.setQuantity(ci.getQuantity());
        orderDetail.setPrice(ci.getPrice());
        orderDetail.setTotal(ci.getTotal());
        orderDetail.setOrderDate(LocalDateTime.now());
        return orderDetail;
    }


    private void checkCartItemQuantity(List<CartItem> cartItems){
        cartItems.stream()
                .filter(cartItem -> cartItem.getProductDetail().getStock() < cartItem.getQuantity())
                .findFirst()
                .ifPresent(cartItem -> {
                    throw new AppException(ErrorCode.SHORTAGE_QUANTITY);
                });
    }

    private void updateProductStockAndSold(List<CartItem> cartItems){
        cartItems.forEach(cartItem ->{
            ProductDetail productDetail = cartItem.getProductDetail();
            productDetail.setStock(productDetail.getStock() - cartItem.getQuantity());
            productDetailRepository.save(productDetail);
            Product product = productDetail.getProduct();
            product.setSold(product.getSold() + cartItem.getQuantity());
            productRepository.save(product);
        });
    }

    public void rollbackProductStockAndSold(List<CartItem> cartItems){
        cartItems.forEach(ci -> {
            ProductDetail productDetail = ci.getProductDetail();
            productDetail.setStock(productDetail.getStock() + ci.getQuantity());
            productDetailRepository.save(productDetail);
            Product product = productDetail.getProduct();
            product.setSold(product.getSold() - ci.getQuantity());
            productRepository.save(product);
        });
    }

    public void deleteOrder(int id){
        orderRepository.findById(id).ifPresent(orderRepository::delete);
    }

    public void cancelOrder(int id){
        Order order = orderRepository.findById(id).orElse(null);
        if(order!=null){
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        }
    }


    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
