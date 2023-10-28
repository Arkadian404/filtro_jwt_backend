package com.ark.security.controller.user;

import com.ark.security.auth.AuthenticationService;
import com.ark.security.dto.CartItemDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductImage;
import com.ark.security.models.request.RequestCartItemData;
import com.ark.security.models.response.CartItemListAPIResponse;
import com.ark.security.models.user.User;
import com.ark.security.service.CartItemService;
import com.ark.security.service.CartService;
import com.ark.security.service.ProductImageService;
import com.ark.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'USER')")
public class CartController {
    private final CartItemService cartItemService;

    private final ProductImageService productImageService;

    private final CartService cartService;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @GetMapping("/getList")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read', 'user:read' )")
    public ResponseEntity<?> getList(){
        try{
            CartItemListAPIResponse cartItemListAPIResponse = new CartItemListAPIResponse();
            List<CartItem> cartItemList = cartItemService.getAllCartItems(1);
            List<ProductImage> productImages = productImageService.getProductImagesFromListCartItem(cartItemList);
            if (cartItemList != null){
                System.out.println("cart item list ton tai");
                cartItemListAPIResponse.setCartItemList(cartItemService.convertCartItemToDto(cartItemList));
                cartItemListAPIResponse.setProductImages(productImages);
                return ResponseEntity.ok(cartItemListAPIResponse);
            }
            else {
                System.out.println("cart item list khong ton tai ton tai");
                cartItemListAPIResponse.setCartItemList(new ArrayList<>());
                cartItemListAPIResponse.setProductImages(new ArrayList<>());
                return ResponseEntity.ok(new CartItemListAPIResponse());
            }

        } catch(Exception exception){
            CartItemListAPIResponse cartItemListAPIResponse = new CartItemListAPIResponse();
            cartItemListAPIResponse.setCartItemList(new ArrayList<>());
            cartItemListAPIResponse.setProductImages(new ArrayList<>());
            return ResponseEntity.ok(cartItemListAPIResponse);
        }

    }

    @GetMapping("/getCart")
    @PreAuthorize("hasAnyAuthority('admin:read','employee:read', 'user:read' )")
    public ResponseEntity<?> getCart(@RequestParam String username){
        Cart cart = cartService.getCartByUserId(userService.getByUsername(username).getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/saveCartItem")
    @PreAuthorize("hasAnyAuthority('admin:create','employee:create', 'user:create' )")
    public ResponseEntity<?> createCartItem(@RequestBody Map<String, List<Integer>> requestMap,
                                            HttpServletRequest request,
                                            HttpServletResponse response){
        List<Integer> quantities = requestMap.get("quantities");
        List<Integer> productDetailIds = requestMap.get("productDetailIds");
        Cart cart = cartItemService.getCartFromUserId(authenticationService.getCurrentUser(request, response).getId());
        System.out.println("Cart: " + cart.getUser().getEmail());
//        System.out.println("createCartItem" +  requestCartItemData);
//        cartItemService.saveListCartItem(requestCartItemData);
        System.out.println("quanties: " +  quantities.get(0));
        System.out.println("productDetailId: " + productDetailIds.get(0));
        cartItemService.saveListCartItem(quantities, productDetailIds, cart);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Them cart item thanh cong")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/delete/{cartItemID}")
    @PreAuthorize("hasAnyAuthority('admin:delete', 'employee:delete', 'user: delete')")
    public ResponseEntity<?> delete(@PathVariable int cartItemID){
        cartItemService.deleteCartItem(cartItemID);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

//    @GetMapping("/checkCartItemExist/{cartItemID}")
//    @PreAuthorize("hasAnyAuthority('admin:read', 'employee:read', 'user: read')")
//    public ResponseEntity<?> checkCartItemExist(@PathVariable int cartItemID,
//                                                HttpServletRequest request,
//                                                HttpServletResponse response){
//        boolean exist =  cartItemService.checkCartItemExist(cartItemID, authenticationService.getCurrentUser(request, response).getId());
//        return ResponseEntity.ok(exist);
//    }


}
