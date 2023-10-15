package com.ark.security.controller.user;

import com.ark.security.dto.CartItemDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductImage;
import com.ark.security.models.response.CartItemListAPIResponse;
import com.ark.security.service.CartItemService;
import com.ark.security.service.CartService;
import com.ark.security.service.ProductImageService;
import com.ark.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'USER')")
public class CartController {
    private final CartItemService cartItemService;

    private final ProductImageService productImageService;

    private final CartService cartService;

    private final UserService userService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read', 'user:read' )")
    public ResponseEntity<?> getList(){
        List<CartItem> cartItemList = cartItemService.getAllCartItems(1);
        List<ProductImage> productImages = productImageService.getProductImagesFromListCartItem(cartItemList);
        CartItemListAPIResponse cartItemListAPIResponse = new CartItemListAPIResponse();
        cartItemListAPIResponse.setCartItemList(cartItemService.convertCartItemToDto(cartItemList));
        cartItemListAPIResponse.setProductImages(productImages);
        return ResponseEntity.ok(cartItemListAPIResponse);
    }

    @GetMapping("/getCart")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read', 'user:read' )")
    public ResponseEntity<?> getCart(@RequestParam String username){
        Cart cart = cartService.getCartByUserId(userService.getByUsername(username).getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/saveCartItem")
//    @PreAuthorize("hasAnyAuthority('admin:create','employee:create', 'user:create' )")
    public ResponseEntity<?> createCartItem(@RequestBody List<CartItemDto> cartItems){
        System.out.println("createCartItem" + cartItems);
        cartItemService.convertListCartItemDtoToModelandSave(cartItems);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Them cart item thanh cong")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete')")
    public ResponseEntity<?> delete(@PathVariable int id){
        cartItemService.deleteCartItem(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }


}
