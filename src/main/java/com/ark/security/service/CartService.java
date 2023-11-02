package com.ark.security.service;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {
    private final String CART_NOT_FOUND = "Không tìm thấy giỏ hàng: ";
    private final CartRepository cartRepository;

    public Cart getCartById(int id){
        return cartRepository.findById(id).orElseThrow(()-> new NotFoundException(CART_NOT_FOUND+ id));
    }

    public Cart getCartByUserId(int userId){
        return cartRepository.findByUserId(userId).orElseThrow(()-> new NotFoundException(CART_NOT_FOUND+ userId));
    }

    public Cart getCartByUsername(String username){
        return cartRepository.findByUserUsername(username).orElse(null);
    }

    public void saveCart(Cart cart){
        cartRepository.save(cart);
    }

    public Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setStatus(true);
        cartRepository.save(cart);
        return cart;
    }


}
