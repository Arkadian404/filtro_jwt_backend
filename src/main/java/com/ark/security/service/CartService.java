package com.ark.security.service;
import com.ark.security.dto.response.CartResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.exception.NotFoundException;
import com.ark.security.mapper.CartMapper;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartRepository;
import com.ark.security.repository.UserVoucherRepository;
import com.ark.security.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final CartMapper cartMapper;

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public Cart getCart(){
        User user = getCurrentUser();
        Cart cart = getOrCreateActiveCart(user);

        if(cart !=null && cart.getCartItems().isEmpty() && cart.getVoucher() != null) {
            removeVoucherFromCart(cart, user);
        }

        return cart;
    }

    public Cart getCartById(int id){
        return cartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }

    public CartResponse getCurrentUserCart(){
        return cartMapper.toCartResponse(getCart());
    }

    private void removeVoucherFromCart(Cart cart, User user) {
        UserVoucher userVoucher = userVoucherRepository.findByUserIdAndVoucherId(
                user.getId(), cart.getVoucher().getId()
        ).orElse(null);

        cart.setVoucher(null);
        cartRepository.save(cart);

        if (userVoucher != null) {
            userVoucherRepository.delete(userVoucher);
        }
    }

    private Cart getOrCreateActiveCart(User user) {
        return cartRepository.findByUserUsername(user.getUsername())
                .orElseGet(() -> createNewCart(user));
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setStatus(true);
        cart.setTotal(0);
        return cartRepository.save(cart);
    }


    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
