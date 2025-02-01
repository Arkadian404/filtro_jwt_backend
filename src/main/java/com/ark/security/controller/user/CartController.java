package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.CartItemRequest;
import com.ark.security.dto.response.CartItemResponse;
import com.ark.security.dto.response.CartResponse;
import com.ark.security.service.CartItemService;
import com.ark.security.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
@SecurityRequirement(name = "BearerAuth")
public class CartController {
    private final CartService cartTestService;
    private final CartItemService cartItemTestService;

    @GetMapping("/myCart")
    public ApiResponse<CartResponse> getUserCart(){
        return ApiResponse.<CartResponse>builder()
                .result(cartTestService.getCurrentUserCart())
                .build();
    }

    @GetMapping("/{id}/items")
    public ApiResponse<List<CartItemResponse>> getCartItemByCartId(@PathVariable int id){
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartItemTestService.getCartItemsByCartId(id))
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<String> addCartItem(@RequestBody CartItemRequest request){
        cartItemTestService.addCartItemToCart(request);
        return ApiResponse.<String>builder()
                .result("Add cart item successfully")
                .build();
    }

    @PutMapping("/items/{id}")
    public ApiResponse<String> updateCartItemQuantity(@PathVariable int id, @RequestBody int amount){
        cartItemTestService.updateCartItemQuantity(id, amount);
        return ApiResponse.<String>builder()
                .result("Update cart item quantity successfully")
                .build();
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<String> deleteCartItem(@PathVariable int id){
        cartItemTestService.deleteCartItem(id);
        return ApiResponse.<String>builder()
                .result("Delete cart item successfully")
                .build();
    }


}
