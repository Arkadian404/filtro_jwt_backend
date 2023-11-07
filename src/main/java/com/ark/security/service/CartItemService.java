package com.ark.security.service;
import com.ark.security.dto.CartItemDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartItemRepository;
import com.ark.security.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductDetailService productDetailService;

    private final CartService cartService;

    private final String CART_ITEM_NOT_FOUND = "Không tìm thấy item trong gio hang: ";

    public List<CartItemDto> getCartItemsByCartId(int id){
        List<CartItem> list =  cartItemRepository.findAllByCartId(id);
        List<CartItemDto> listDto = new ArrayList<>();
        list.forEach(li-> listDto.add(li.convertToDto()));
//        if(list.isEmpty()){
//            listDto = ;
//        }
        return listDto;
    }

    private void add(CartItemDto cartItemDto, Cart cart){
        ProductDetail productDetail = productDetailService.getProductDetailById(cartItemDto.getProductDetail().getId());
        Optional<CartItem> existingItems = cart.getCartItems()
                .stream()
                .filter(item-> item.getProductDetail().getId().equals(productDetail.getId()))
                .findFirst();
        if(existingItems.isPresent()){
            existingItems.get().setQuantity(existingItems.get().getQuantity() + cartItemDto.getQuantity());
            existingItems.get().setTotal(existingItems.get().getTotal() + cartItemDto.getQuantity() * existingItems.get().getPrice());
            cartItemRepository.save(existingItems.get());
        }
        else {
            CartItem cartItem  = cartItemDto.convertToEntity();
            cartItem.setCart(cart);
            cartItem.setProductDetail(productDetail);
            cartItem.setPrice(productDetail.getPrice());
            cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
            cartItem.setPurchaseDate(LocalDateTime.now());
            cartItemRepository.save(cartItem);
            cart.getCartItems().add(cartItem);
            cart.setUpdatedAt(LocalDateTime.now());
            cart.setTotal(cart.getTotal() + cartItem.getTotal());
            cartService.saveCart(cart);
        }
    }

    public void addCartItemToCart(CartItemDto cartItemDto, User user){
       if(user!=null){
           Cart cart = cartService.getCartByUsername(user.getUsername());
           if(cart!=null){
              add(cartItemDto, cart);
           }else{
               cart = cartService.createCart(user);
               add(cartItemDto, cart);
           }
       }
    }

    public void updateCartItemQuantity(int cartItemId, User user, int amount){
        Cart cart = cartService.getCartByUsername(user.getUsername());
        CartItem cartItem = getCartItemById(cartItemId);
        Optional<CartItem> item = cart.getCartItems().stream()
                .filter(i-> i.getId().equals(cartItem.getId()))
                .findFirst();
        if(item.isPresent()){
            CartItem ci = item.get();
            ci.setQuantity(ci.getQuantity() + amount);
            ci.setTotal(ci.getQuantity() * ci.getPrice());
            cartItemRepository.save(ci);
            cart.setTotal(cart.getTotal() + ci.getTotal());
            cartService.saveCart(cart);
        }else{
            throw new NotFoundException(CART_ITEM_NOT_FOUND + cartItem.getId());
        }
    }



    public CartItem getCartItemById(int id){
        return cartItemRepository.findById(id).orElseThrow(()-> new NotFoundException(CART_ITEM_NOT_FOUND+ id));
    }

    public void deleteCartItem(int id){
        CartItem cartItem = getCartItemById(id);
        if (cartItem == null){
            throw new NotFoundException(CART_ITEM_NOT_FOUND+ id);
        }
        cartItem.getCart().setTotal(cartItem.getCart().getTotal() - cartItem.getTotal());
        cartItemRepository.deleteById(id);
    }

}
