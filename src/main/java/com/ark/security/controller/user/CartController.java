package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.dto.CartItemDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Cart;
import com.ark.security.models.user.User;
import com.ark.security.service.CartItemService;
import com.ark.security.service.CartService;
import com.ark.security.service.product.ProductImageService;
import com.ark.security.service.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class CartController {
    private final CartItemService cartItemService;

    private final CartService cartService;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @GetMapping("/get/{username}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getCart(@PathVariable String username){
        Cart cart;
        User user = userService.getByUsername(username);
        cart = cartService.getCartByUsername(user.getUsername());
        if(cart == null || !cartService.checkActiveCart(user.getId())){
            cart= cartService.createCart(user);
        }
        return ResponseEntity.ok(cart.convertToDto());
    }

    @GetMapping("/{cartId}/getCartItems")
    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user:read')")
    public ResponseEntity<?> getCartItems(@PathVariable int cartId){
        return ResponseEntity.ok(cartItemService.getCartItemsByCartId(cartId));
    }

    @PostMapping("/saveCartItem")
    @PreAuthorize("hasAnyAuthority('admin:create', 'employee:create', 'user:create')")
    public ResponseEntity<?> saveCartItem(@Valid @RequestBody CartItemDto cartItemDto,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        User user = authenticationService.getCurrentUser(request, response);
        cartItemService.addCartItemToCart(cartItemDto, user);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đã thêm sản phẩm vào giỏ hàng")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PutMapping("/update/cart/items/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'employee:update', 'user:update')")
    public ResponseEntity<?> updateCartItem(@PathVariable int id,
                                            @RequestBody int amount,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        User user = authenticationService.getCurrentUser(request, response);
        cartItemService.updateCartItemQuantity(id, user, amount);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật số lượng sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }


    @DeleteMapping("/delete/cart/items/{cartItemID}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete', 'user:delete')")
    public ResponseEntity<?> delete(@PathVariable int cartItemID){
        cartItemService.deleteCartItem(cartItemID);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }


}
