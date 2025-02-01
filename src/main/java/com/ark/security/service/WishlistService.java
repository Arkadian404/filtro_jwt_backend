package com.ark.security.service;

import com.ark.security.dto.response.WishlistResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.WishlistMapper;
import com.ark.security.models.Wishlist;
import com.ark.security.models.user.User;
import com.ark.security.repository.WishlistRepository;
import com.ark.security.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;
    private final UserRepository userRepository;


    public WishlistResponse getByUsername(String username){
        return wishlistMapper.toWishlistResponse(wishlistRepository.findByUserUsername(username).orElse(null));
    }

    public WishlistResponse getMyWishlist(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Wishlist wishlist = wishlistRepository.findByUserUsername(username).orElse(null);
        if(wishlist == null){
            return create(username);
        }
        return wishlistMapper.toWishlistResponse(wishlist);
    }

    public WishlistResponse create(String username){
        Wishlist wishlist = new Wishlist();
        log.info("Creating wishlist for user: {}", username);
        wishlist.setUser(userRepository.findUserByUsername(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlist.setUpdatedAt(LocalDateTime.now());
        wishlist.setStatus(true);
        return wishlistMapper.toWishlistResponse(wishlistRepository.save(wishlist));
    }

    public Wishlist createUserWishlist(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(userRepository.findUserByUsername(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND)));
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlist.setUpdatedAt(LocalDateTime.now());
        wishlist.setStatus(true);
        return wishlistRepository.save(wishlist);

    }

}
