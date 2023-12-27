package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.dto.WishlistItemDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Wishlist;
import com.ark.security.models.user.User;
import com.ark.security.service.WishlistItemService;

import com.ark.security.service.WishlistService;
import com.ark.security.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/user/wishlist")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN, USER, EMPLOYEE')")
public class WishlistController {
    private final WishlistItemService wishlistItemService;
    private final WishlistService wishlistService;
    private final UserService userService;
    private final AuthenticationService service;

    @GetMapping("/get/{username}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getWishlist(@PathVariable String username){
        Wishlist wishlist;
        User user = userService.getByUsername(username);
        System.out.println(user.getUsername());
        wishlist = wishlistService.getByUsername(user.getUsername());
        if(wishlist == null){
            wishlist= wishlistService.createWishlist(user);
        }
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/getWishlistItems/{wishlistId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getWishlistItems(@PathVariable int wishlistId){
        return ResponseEntity.ok(wishlistItemService.getItemsByWishlistId(wishlistId));
    }


    @PostMapping("/saveWishlistItem")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create', 'user:create')")
    public ResponseEntity<?> saveWishlistItem(@RequestBody WishlistItemDto wishlistItem,
                                              HttpServletRequest request,
                                              HttpServletResponse response){
        User user = service.getCurrentUser(request, response);
        wishlistItemService.addItems(wishlistItem, user);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Thêm sản phẩm vào danh sách ước thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/wishlist/items/{wishlistItemId}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete', 'user:delete')")
    public ResponseEntity<?> deleteWishlistItem(@PathVariable int wishlistItemId){
        wishlistItemService.deleteCartItem(wishlistItemId);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành ra khỏi danh sách ước")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

}
