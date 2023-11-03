package com.ark.security.service;

import com.ark.security.dto.CartItemDto;
import com.ark.security.dto.WishlistDto;
import com.ark.security.dto.WishlistItemDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.CartItem;
import com.ark.security.models.Wishlist;
import com.ark.security.models.WishlistItem;
import com.ark.security.models.user.User;
import com.ark.security.repository.WishlistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistItemService {
    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistService wishlistService;

    private final String WIHSLIST_ITEM_NOT_FOUND = "Không tìm thấy item trong wishlist: ";
    public WishlistItem getWishlistItemById(int id){
        return wishlistItemRepository.findById(id).orElseThrow(()-> new NotFoundException(WIHSLIST_ITEM_NOT_FOUND+ id));
    }


    public List<WishlistItemDto> getItemsByWishlistId(int id){
        List<WishlistItem> list =  wishlistItemRepository.findAllByWishlistId(id).orElse(null);
        List<WishlistItemDto> listDto = new ArrayList<>();
        if(list!=null){
            list.forEach(li-> listDto.add(li.convertToDto()));
        }
        return listDto;
    }

    public void add(WishlistItemDto wishlistItemDto, Wishlist wishlist){
        WishlistItem wishlistItem = wishlistItemDto.convertToEntity();
        wishlistItem.setWishlist(wishlist);
        wishlistItem.setAddDate(LocalDateTime.now());
        wishlistItemRepository.save(wishlistItem);
        wishlist.getWishlistItems().add(wishlistItem);
        wishlist.setUpdatedAt(LocalDateTime.now());
        wishlistService.saveWishlist(wishlist);
    }

    public void addItems(WishlistItemDto wishlistDto, User user){
        if(user!=null){
            Wishlist wishlist = wishlistService.getByUsername(user.getUsername());
            if(wishlist!=null){
                add(wishlistDto, wishlist);
            }else{
                wishlist = wishlistService.createWishlist(user);
                add(wishlistDto, wishlist);
            }
        }
    }




    public void deleteCartItem(int id){
        WishlistItem wishlistItem = getWishlistItemById(id);
        if (wishlistItem == null){
            throw new NotFoundException(WIHSLIST_ITEM_NOT_FOUND+ id);
        }
        wishlistItemRepository.deleteById(id);
    }

}
