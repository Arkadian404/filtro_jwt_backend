package com.ark.security.service;

import com.ark.security.models.Wishlist;
import com.ark.security.models.user.User;
import com.ark.security.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public Wishlist getByUsername(String username){
        return wishlistRepository.findByUserUsername(username).orElse(null);
    }

    public void saveWishlist(Wishlist wishlist){
        wishlistRepository.save(wishlist);
    }

    public Wishlist createWishlist(User user){
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlist.setStatus(true);
        wishlistRepository.save(wishlist);
        return wishlist;
    }

}
