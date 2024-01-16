package com.ark.security.service;
import com.ark.security.dto.CartItemDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.Voucher;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartItemRepository;
import com.ark.security.service.product.ProductDetailService;
import com.ark.security.service.product.ProductService;
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
    private final UserVoucherService userVoucherService;
    private final ProductService productService;

    private final CartService cartService;

    private final String CART_ITEM_NOT_FOUND = "Không tìm thấy item trong gio hang: ";

    public List<CartItemDto> getCartItemsByCartId(int id){
        List<CartItem> list =  cartItemRepository.findAllByCartId(id);
        List<CartItemDto> listDto = new ArrayList<>();
        list.forEach(li-> listDto.add(li.convertToDto()));
        return listDto;
    }

    private void add(CartItemDto cartItemDto, Cart cart){
        ProductDetail productDetail = productDetailService.getProductDetailById(cartItemDto.getProductDetail().getId());
        Voucher voucher = cart.getVoucher();
        Optional<CartItem> existingItems = cart.getCartItems()
                .stream()
                .filter(item-> item.getProductDetail().getId().equals(productDetail.getId()))
                .findFirst();
        if(existingItems.isPresent()){
            existingItems.get().setQuantity(existingItems.get().getQuantity() + cartItemDto.getQuantity());
            existingItems.get().setTotal(existingItems.get().getTotal() + cartItemDto.getQuantity() * existingItems.get().getPrice());
            cartItemRepository.save(existingItems.get());
            cart.setTotal(cart.getTotal() + cartItemDto.getQuantity() * existingItems.get().getPrice());
            cart.setUpdatedAt(LocalDateTime.now());
            cartService.saveCart(cart);
        }
        else {
            CartItem cartItem  = cartItemDto.convertToEntity();
            Product product = productService.getProductBySlug(cartItemDto.getSlug());
            if(voucher!=null){
                double discount = voucher.getDiscount();
                if(voucher.getCategory()!=null){
                    if(product.getCategory().equals(voucher.getCategory())){
                        addCartItemWithVoucher(cart, productDetail, cartItem, discount);
                    }else{
                        addCartItemWithoutVoucher(cart, productDetail, cartItem);
                    }
                }else{
                    addCartItemWithVoucher(cart, productDetail, cartItem, discount);
                }
            }else {
                addCartItemWithoutVoucher(cart, productDetail, cartItem);
            }
        }
    }

    private void addCartItemWithoutVoucher(Cart cart, ProductDetail productDetail, CartItem cartItem) {
        cartItem.setCart(cart);
        cartItem.setProductDetail(productDetail);
        cartItem.setPrice(productDetail.getPrice());
        cartItem.setPurchaseDate(LocalDateTime.now());
        cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
        cartItemRepository.save(cartItem);
        cart.getCartItems().add(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setTotal(cart.getTotal() + cartItem.getTotal());
        cartService.saveCart(cart);
    }

    private void addCartItemWithVoucher(Cart cart, ProductDetail productDetail, CartItem cartItem, double discount) {
        cartItem.setCart(cart);
        cartItem.setProductDetail(productDetail);
        cartItem.setPrice((int) (cartItem.getPrice() - (cartItem.getPrice() *discount/100)));
        cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
        cartItem.setPurchaseDate(LocalDateTime.now());
        cartItemRepository.save(cartItem);
        cart.getCartItems().add(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setTotal(cart.getTotal() + cartItem.getTotal());
        cartService.saveCart(cart);
    }

    public void addCartItemToCart(CartItemDto cartItemDto, User user){
       if(user!=null){
           Cart cart = cartService.getCartByUsername(user.getUsername());
           if(cart!=null && cartService.checkActiveCart(user.getId())){
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
            if(amount < 0){
                cart.setTotal(cart.getTotal() - ci.getPrice());
            }else{
                cart.setTotal(cart.getTotal() + ci.getPrice());
            }
            cartService.saveCart(cart);
        }else{
            throw new NotFoundException(CART_ITEM_NOT_FOUND + cartItem.getId());
        }
    }

    public void saveCartItem(CartItem cartItem){
        cartItemRepository.save(cartItem);
    }


    public CartItem getCartItemById(int id){
        return cartItemRepository.findById(id).orElseThrow(()-> new NotFoundException(CART_ITEM_NOT_FOUND+ id));
    }

    public void deleteCartItem(int id){
        CartItem cartItem = getCartItemById(id);
        if (cartItem == null){
            throw new NotFoundException(CART_ITEM_NOT_FOUND+ id);
        }
        Cart cart = cartItem.getCart();
        Voucher voucher = cart.getVoucher();
        if(voucher!=null){
            List<CartItem> cartItems = cart.getCartItems().stream()
                    .filter(item-> item.getProductDetail().getProduct().getCategory().equals(voucher.getCategory()))
                    .toList();
            if(voucher.getCategory()!=null){
                if(cartItems.size() == 1){
                    UserVoucher userVoucher = userVoucherService.getUserVoucherByUserIdAndVoucherId(cart.getUser().getId(), voucher.getId());
                    cart.setVoucher(null);
                    cart.setTotal(cart.getTotal() - cartItem.getTotal());
                    cartService.saveCart(cart);
                    cartItemRepository.deleteById(id);
                    userVoucherService.deleteUserVoucher(userVoucher.getId());
                }else {
                    cart.setTotal(cart.getTotal() - cartItem.getTotal());
                    cartService.saveCart(cart);
                    cartItemRepository.deleteById(id);
                }
            }else{
                cart.setTotal(cart.getTotal() - cartItem.getTotal());
                cartService.saveCart(cart);
                cartItemRepository.deleteById(id);
            }
        }else{
            cartItem.getCart().setTotal(cartItem.getCart().getTotal() - cartItem.getTotal());
            cartItemRepository.deleteById(id);
        }
    }

}
