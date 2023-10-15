package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final String CART_NOT_FOUND = "Không tìm thấy gio hang: ";
    private final CartRepository cartRepository;

    public Cart getCartById(int id){
        return cartRepository.findById(id).orElseThrow(()-> new NotFoundException(CART_NOT_FOUND+ id));
    }

    public Cart getCartByUserId(int userId){
        return cartRepository.findByUserId(userId).orElseThrow(()-> new NotFoundException(CART_NOT_FOUND+ userId));
    }
}