package com.ark.security.service;
import com.ark.security.dto.request.CartItemRequest;
import com.ark.security.dto.response.CartItemResponse;
import com.ark.security.dto.response.ProductDetailResponse;
import com.ark.security.dto.response.ProductImageResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.mapper.CartItemMapper;
import com.ark.security.mapper.ProductDetailMapper;
import com.ark.security.mapper.ProductImageMapper;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.Voucher;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.repository.CartItemRepository;
import com.ark.security.repository.CartRepository;
import com.ark.security.repository.product.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;
    private final CartService cartTestService;
    private final UserVoucherService userVoucherService;

    public List<CartItemResponse> getCartItemsByCartId(int id){
        List<CartItem> list =  cartItemRepository.findAllByCartId(id);
        return list.stream()
                .map(item -> {
                    CartItemResponse response = cartItemMapper.toCartItemResponse(item);
                    ProductImageResponse imageResponse = productImageMapper.toProductImageResponse(item.getProductDetail()
                            .getProduct()
                            .getImages()
                            .get(0));
                    ProductDetailResponse productDetailResponse = productDetailMapper.toProductDetailResponse(item.getProductDetail());
                    response.setProductImage(imageResponse);
                    response.setProductDetail(productDetailResponse);
                    return response;
                })
                .toList();
    }

    public void addCartItemToCart(CartItemRequest request){
        log.info(request.toString());
        Cart cart = cartTestService.getCartById(request.getCartId());
        CartItem cartItem = cartItemMapper.toCartItem(request);
        cartItem.setProductDetail(productDetailRepository.findById(request.getProductDetailId())
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND)));
        addCartItem(cartItem, cart);
    }

    public void updateCartItemQuantity(int cartItemId, int amount){
        Cart cart = cartTestService.getCart();
        CartItem cartItem = getCartItemById(cartItemId);
        if(!cart.getCartItems().contains(cartItem)) throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        int newQuantity = cartItem.getQuantity() + amount;
        if(newQuantity <= 0) throw new AppException(ErrorCode.CART_ITEM_QUANTITY_INVALID);
        int priceChange = cartItem.getPrice() * newQuantity;
        cartItem.setQuantity(newQuantity);
        cartItem.setTotal(priceChange);
        cartItem.setPurchaseDate(LocalDateTime.now());
        cartItemRepository.save(cartItem);
        updateCartTotal(cart, priceChange);
    }

    public void deleteCartItem(int id){
        CartItem cartItem = getCartItemById(id);
        Cart cart = cartItem.getCart();
        Voucher voucher = cart.getVoucher();
        if(voucher != null && isLastCartItemInCart(cart, cartItem, voucher)){
            removeVoucherFromCart(cart, voucher);
        }
        updateCartAfterRemoval(cart, cartItem);
        cartItemRepository.delete(cartItem);
    }

    private CartItem getCartItemById(int id){
        return cartItemRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
    }


    private void addCartItem(CartItem cartItem, Cart cart){
        ProductDetail productDetail = cartItem.getProductDetail();
        Optional<CartItem> existingCartItem = findExistingCartItem(cart, productDetail);
        if(existingCartItem.isPresent()){
            updateExistingCartItem(existingCartItem.get(), cartItem, cart);
        }else{
            createNewCartItem(cartItem, cart, productDetail);
        }
    }

    private void createNewCartItem(CartItem cartItem, Cart cart, ProductDetail productDetail){
        cartItem.setCart(cart);
        cartItem.setProductDetail(productDetail);
        int price = calculatePriceWithVoucher(cart, productDetail, cartItem.getPrice());
        cartItem.setPrice(price);
        cartItem.setTotal(price * cartItem.getQuantity());
        cartItem.setPurchaseDate(LocalDateTime.now());
        cartItemRepository.save(cartItem);
        cart.getCartItems().add(cartItem);
        updateCartTotal(cart, cartItem.getTotal());
    }

    private void updateExistingCartItem(CartItem exsitedCartItem, CartItem cartItem, Cart cart){
        int additionalQuantity = cartItem.getQuantity();
        log.info("additional quantity: {}", additionalQuantity);
        int additionalPrice = cartItem.getPrice() * additionalQuantity;
        log.info("cart item quantity: {}", cartItem.getQuantity());
        exsitedCartItem.setQuantity(exsitedCartItem.getQuantity() + additionalQuantity);
        exsitedCartItem.setTotal(exsitedCartItem.getTotal() + additionalPrice);
        exsitedCartItem.setPurchaseDate(LocalDateTime.now());
        cartItemRepository.save(exsitedCartItem);
        updateCartTotal(cart, additionalPrice);
    }

    private int calculatePriceWithVoucher(Cart cart, ProductDetail productDetail, int price){
        Voucher voucher = cart.getVoucher();
        if(voucher == null){
            return price;
        }
        boolean isVoucherApplicable = voucher.getCategory() == null
                || productDetail.getProduct().getCategory().equals(voucher.getCategory());
        if(isVoucherApplicable){
            return (int) (price * (1- voucher.getDiscount()));
        }
        return price;
    }

    private Optional<CartItem> findExistingCartItem(Cart cart, ProductDetail productDetail){
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProductDetail().getId().equals(productDetail.getId()))
                .findFirst();
    }

    private void updateCartTotal(Cart cart, int amount){
        cart.setTotal(cart.getTotal() + amount);
        cart.setUpdatedAt(cart.getUpdatedAt());
        cartRepository.save(cart);
    }



    private boolean isLastCartItemInCart(Cart cart, CartItem cartItem, Voucher voucher){
        if(voucher.getCategory() == null){
            return cart.getCartItems().size() == 1;
        }else{
            return cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProductDetail().getProduct().getCategory().equals(voucher.getCategory()))
                    .count() == 1;
        }
    }

    private void removeVoucherFromCart(Cart cart, Voucher voucher){
        UserVoucher uv = userVoucherService.getUserVoucherByUserIdAndVoucherId(cart.getUser().getId(), voucher.getId());
        cart.setVoucher(null);
        userVoucherService.deleteUserVoucher(uv.getId());
    }

    private void updateCartAfterRemoval(Cart cart, CartItem cartItem){
        int updateTotal = cart.getTotal() - cartItem.getTotal();
        cart.setTotal(updateTotal);
        cartRepository.save(cart);
    }

}
