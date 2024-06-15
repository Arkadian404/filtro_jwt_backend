package com.ark.security.service;

import com.ark.security.dto.OrderDto;
import com.ark.security.exception.QuantityShortageException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.user.User;
import com.ark.security.repository.OrderRepository;
import com.ark.security.service.product.ProductDetailService;
import com.ark.security.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final ProductDetailService productDetailService;
    private final ProductService productService;
    private final CartService cartService;


    public boolean checkOrderCode(String orderCode){
        return orderRepository.existsByOrderCode(orderCode).orElse(false);
    }

    public List<Order> getAllOrdersByOrderDate(){
        // Sort by orderDate DESC
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public Order getOrderByOrderCode(String orderCode){
        return orderRepository.findByOrderCode(orderCode).orElse(null);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
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

    public List<OrderDto> getOrdersDtoByUserId(int id){
        List<Order> orders = orderRepository.findAllByUserId(id).orElse(null);
        List<OrderDto> orderDtos = new ArrayList<>();
        if(orders!=null){
            orders.stream()
                    .sorted((o1, o2)-> o2.getOrderDate().compareTo(o1.getOrderDate()))
                    .forEach(order -> orderDtos.add(order.convertToDto()));
            return orderDtos;
        }
        return Collections.emptyList();
    }

    @Transactional
    public OrderDto createOrder(User user, Order order){
        Cart cart = cartService.getCartByUsername(user.getUsername());
        List<CartItem> cartItems = cart.getCartItems();
        // check item quantity in stock
        checkCartItemQuantity(cartItems);
        // update product stock and sold
        updateProductStockAndSold(cartItems);
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
        OrderDto orderDto = order.convertToDto();
        orderDto.setDiscount(cart.getVoucher() != null ? cart.getVoucher().getDiscount() : 0);
        return orderDto;
    }


    private void checkCartItemQuantity(List<CartItem> cartItems){
        cartItems.stream()
                .filter(cartItem -> cartItem.getProductDetail().getStock() < cartItem.getQuantity())
                .findFirst()
                .ifPresent(cartItem -> {
                    throw new QuantityShortageException("Sản phẩm " + cartItem.getProductDetail().getProduct().getName() + " không đủ số lượng trong kho");
                });
    }

    private void updateProductStockAndSold(List<CartItem> cartItems){
        cartItems.forEach(ci -> {
            ProductDetail productDetail = ci.getProductDetail();
            productDetail.setStock(productDetail.getStock() - ci.getQuantity());
            productDetailService.saveProductDetail(productDetail);
            Product product = productDetail.getProduct();
            product.setSold(product.getSold() + ci.getQuantity());
            productService.save(product);
        });
    }

    public void rollbackProductStockAndSold(List<CartItem> cartItems){
        cartItems.forEach(ci -> {
            ProductDetail productDetail = ci.getProductDetail();
            productDetail.setStock(productDetail.getStock() + ci.getQuantity());
            productDetailService.saveProductDetail(productDetail);
            Product product = productDetail.getProduct();
            product.setSold(product.getSold() - ci.getQuantity());
            productService.save(product);
        });
    }

    public void updateOrder(int id, Order order){
        Order oldOrder = orderRepository.findById(id).orElse(null);
        if(oldOrder!=null){
            Cart cart = cartService.getCartByUsername(oldOrder.getUser().getUsername());
            List<CartItem> cartItems = cart.getCartItems();
            if(order.getStatus().equals(OrderStatus.CANCELED) || order.getStatus().equals(OrderStatus.FAILED)){
                rollbackProductStockAndSold(cartItems);
            }
            oldOrder.setStatus(order.getStatus());
            orderRepository.save(oldOrder);
        }
    }

    public void deleteOrder(int id){
        Order order = orderRepository.findById(id).orElse(null);
        if(order!=null){
            orderRepository.delete(order);
        }
    }

    public void cancelOrder(int id){
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }


}
