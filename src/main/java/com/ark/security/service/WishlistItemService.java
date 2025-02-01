package com.ark.security.service;

import com.ark.security.dto.request.WishlistItemRequest;
import com.ark.security.dto.response.WishlistItemResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.WishlistItemMapper;
import com.ark.security.models.Wishlist;
import com.ark.security.models.WishlistItem;
import com.ark.security.repository.WishlistItemRepository;
import com.ark.security.repository.WishlistRepository;
import com.ark.security.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistItemService {
    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistItemMapper wishlistItemMapper;
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final WishlistService wishlistTestService;

    public WishlistItemResponse getWishlistItemById(Integer id){
        return wishlistItemMapper.toWishlistItemResponse(wishlistItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_ITEM_NOT_FOUND)));
    }

    public List<WishlistItemResponse> getItemByWishlistId(Integer id){
        List<WishlistItem> list = wishlistItemRepository.findAllByWishlistId(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_ITEM_NOT_FOUND));
        return list.stream()
                .map(wishlistItemMapper::toWishlistItemResponse)
                .toList();
    }

    private void addItemsToWishlist(WishlistItemRequest request, Wishlist wishlist){
        int productId = request.getProductId();
        Optional<WishlistItem> existingItem = wishlist.getWishlistItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        if(existingItem.isPresent()){
            WishlistItem item = existingItem.get();
            wishlistItemRepository.save(item);
        }else{
            WishlistItem wishlistItem = wishlistItemMapper.toWishlistItem(request);
            wishlistItem.setWishlist(wishlist);
            wishlistItem.setProduct(productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
            wishlistItem.setAddDate(LocalDateTime.now());
            wishlistItemRepository.save(wishlistItem);
            wishlist.getWishlistItems().add(wishlistItem);
            wishlist.setUpdatedAt(LocalDateTime.now());
            wishlistRepository.save(wishlist);
        }
    }

    public void addItems(WishlistItemRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Wishlist wishlist = wishlistRepository.findByUserUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_NOT_FOUND));
        if(wishlist!=null){
            addItemsToWishlist(request, wishlist);
        }else {
            wishlist = wishlistTestService.createUserWishlist();
            addItemsToWishlist(request, wishlist);
        }
    }


    public void deleteItem(Integer id){
        WishlistItem wishlistItem = wishlistItemRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WISHLIST_ITEM_NOT_FOUND));
        wishlistItemRepository.delete(wishlistItem);
    }

}
