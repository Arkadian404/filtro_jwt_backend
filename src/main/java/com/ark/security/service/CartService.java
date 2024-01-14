package com.ark.security.service;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final String CART_NOT_FOUND = "Không tìm thấy giỏ hàng: ";
    private final CartRepository cartRepository;
    private final UserVoucherService userVoucherService;

    public Cart getCartByUsername(String username){
        Cart cart =  cartRepository.findByUserUsername(username).orElse(null);
        if(cart!=null){
            User user = cart.getUser();
            List<CartItem> cartItems = cart.getCartItems();
            if(cartItems.isEmpty() && (cart.getVoucher()!=null)){
                    UserVoucher userVoucher = userVoucherService.getUserVoucherByUserIdAndVoucherId(user.getId(), cart.getVoucher().getId());
                    cart.setVoucher(null);
                    saveCart(cart);
                    userVoucherService.deleteUserVoucher(userVoucher.getId());
            }
        }
        return cart;
    }

    public boolean checkActiveCart(int userId){
        Cart cart = cartRepository.findByUserIdAndStatus(userId, true);
        return cart != null;
    }


    public void saveCart(Cart cart){
        cartRepository.save(cart);
    }

    public Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setStatus(true);
        cart.setTotal(0);
        cartRepository.save(cart);
        return cart;
    }


}
