package com.ark.security.service;

import com.ark.security.dto.CartItemDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.request.RequestCartItemData;
import com.ark.security.repository.CartItemRepository;
import com.ark.security.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductDetailService productDetailService;

    private final ProductService productService;

    private final CartRepository cartRepository;

    private final String CART_ITEM_NOT_FOUND = "Không tìm thấy item trong gio hang: ";

    public List<CartItem> getAllCartItems(int id){
        List<CartItem> list =  cartItemRepository.findAllByCartId(id);
        if(list.isEmpty()){
            throw new NotFoundException("Empty");
        }
        return list;
    }
    public List<CartItemDto> convertCartItemToDto(List<CartItem> cartItems){
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        cartItems.forEach(cartItem -> {
            CartItemDto cartItemDto = CartItemDto.builder()
                    .id(cartItem.getId())
                    .cart(cartItem.getCart())
                    .productName(cartItem.getProductDetail().getProduct().getName())
                    .productDetailDto(cartItem.getProductDetail().convertToDto())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .purchaseDate(cartItem.getPurchaseDate())
                    .build();
            cartItemDtos.add(cartItemDto);
        });
        return cartItemDtos;
    }

    public CartItem convertCartItemDtoToModelandSave(CartItemDto cartItemDto){
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemDto.getId());
        cartItem.setCart(cartItemDto.getCart());
        cartItem.setProductDetail(productDetailService.getProductDetailById(cartItemDto.getProductDetailDto().getId()));
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setPrice(cartItemDto.getPrice());
        cartItem.setPurchaseDate(cartItemDto.getPurchaseDate());
        cartItemRepository.save(cartItem);
        return cartItem;
    }

    public void convertListCartItemDtoToModelandSave(List<CartItemDto> cartItemDtos){
        cartItemDtos.forEach(cartItemDto -> {
            CartItem cartItem = convertCartItemDtoToModelandSave(cartItemDto);
        });
    }

    public void saveListCartItem(List<Integer> quantities, List<Integer> productDetailIds, Cart cart){
        for (int i = 0; i < productDetailIds.size(); i++) {
            ProductDetail productDetail = productDetailService.getProductDetailById(productDetailIds.get(i));
            CartItem newCartItem = new CartItem();
//            newCartItem.setId(cartItems.get(i).getId());
            newCartItem.setCart(cart);
            newCartItem.setProductDetail(productDetail);
            newCartItem.setQuantity(quantities.get(i));
            newCartItem.setPrice(productDetail.getPrice());
            newCartItem.setPurchaseDate(new Date());
            cartItemRepository.save(newCartItem);
        };
    }

    public CartItem getCartItemById(int id){
        return cartItemRepository.findById(id).orElseThrow(()-> new NotFoundException(CART_ITEM_NOT_FOUND+ id));
    }

    public void deleteCartItem(int id){
        CartItem cartItem = getCartItemById(id);
        if (cartItem == null){
            throw new NotFoundException(CART_ITEM_NOT_FOUND+ id);
        }
        cartItemRepository.deleteById(id);
    }

    public Cart getCartFromUserId(int userId){
        return cartRepository.findByUserId(userId).orElseThrow(()-> new NotFoundException("CART_NOT_FOUND"+ userId));
    }

    public boolean checkCartItemExist(int productDetailId, int userId){
        List<CartItem> cartItems= cartItemRepository.checkCartItemExist(productDetailId, userId);
        if( cartItems!= null || !cartItems.isEmpty()){
            return true;
        }
        return false;
    }
}
