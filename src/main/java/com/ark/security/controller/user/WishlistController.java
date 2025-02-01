package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.WishlistItemRequest;
import com.ark.security.dto.response.WishlistItemResponse;
import com.ark.security.dto.response.WishlistResponse;
import com.ark.security.service.WishlistItemService;

import com.ark.security.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/wishlist")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'EMPLOYEE')")
@Slf4j
public class WishlistController {
    private final WishlistService wishlistTestService;
    private final WishlistItemService wishlistItemTestService;


    @GetMapping("/myWishlist")
    public ApiResponse<WishlistResponse> getMyWishlist(){
        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistTestService.getMyWishlist())
                .build();
    }

    @GetMapping("/{wishlistId}/items")
    public ApiResponse<List<WishlistItemResponse>> getItemsByWishlistId(@PathVariable Integer wishlistId){
        return ApiResponse.<List<WishlistItemResponse>>builder()
                .result(wishlistItemTestService.getItemByWishlistId(wishlistId))
                .build();
    }

    @PostMapping("/items")
    public ApiResponse<String> addItemsToWishlist(@RequestBody WishlistItemRequest request){
        wishlistItemTestService.addItems(request);
        return ApiResponse.<String>builder()
                .result("Item added to wishlist")
                .build();
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<String> deleteItemFromWishlist(@PathVariable Integer id){
        wishlistItemTestService.deleteItem(id);
        return ApiResponse.<String>builder()
                .result("Item deleted from wishlist")
                .build();
    }

}
