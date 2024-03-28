package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.VoucherException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.Voucher;
import com.ark.security.models.user.User;
import com.ark.security.repository.VoucherRepository;
import com.ark.security.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final UserService userService;
    private final UserVoucherService userVoucherService;
    private final CartService cartService;
    private final CartItemService cartItemService;

    public void saveVoucher(Voucher voucher){
        voucher.setCode(RandomStringUtils.random(10, true, true));
        voucher.setCreatedAt(LocalDateTime.now());
        voucherRepository.save(voucher);
    }


    public Voucher getVoucherById(Integer id){
        return voucherRepository.findById(id).orElseThrow(()-> new NotFoundException("Voucher not found"));
    }

    public List<Voucher> getAllVoucher(){
        return voucherRepository.findAll();
    }

    public void updateVoucher(int id, Voucher voucher){
        Voucher oldVoucher = getVoucherById(id);
        if(voucher == null){
            throw new NotFoundException("Voucher is null");
        }
        if (oldVoucher != null) {
            oldVoucher.setName(voucher.getName());
            oldVoucher.setDiscount(voucher.getDiscount());
            oldVoucher.setCode(oldVoucher.getCode());
            oldVoucher.setExpirationDate(voucher.getExpirationDate());
            oldVoucher.setDescription(voucher.getDescription());
            oldVoucher.setCategory(voucher.getCategory());
            voucherRepository.save(oldVoucher);
        }
    }

    public void deleteVoucher(int id){
        Voucher oldVoucher = getVoucherById(id);
        if(oldVoucher == null){
            throw new NotFoundException("Voucher is null");
        }
        voucherRepository.deleteById(id);
    }

    public void removeVoucher(int userId, int voucherId){
        Voucher voucher = getVoucherById(voucherId);
        if(voucher != null){
            User user = userService.getUserById(userId);
            UserVoucher userVoucher = userVoucherService.getUserVoucherByUserIdAndVoucherId(userId, voucherId);
            Cart cart = cartService.getCartByUsername(user.getUsername());
            List<CartItem> cartItems = cart.getCartItems();
            List<CartItem> validCartItems = cartItems.stream()
                    .filter(cartItem -> cartItem.getProductDetail().getProduct().getCategory().equals(voucher.getCategory()))
                    .toList();
            if (voucher.getCategory() != null){
                processRemoveVoucher(userVoucher, cart, validCartItems);
            }else{
                processRemoveVoucher(userVoucher, cart, cartItems);
            }
        }else{
            throw new VoucherException("Voucher is null");
        }

    }

    public boolean checkVoucherExpiration(int id){
        Voucher voucher = getVoucherById(id);
        if(voucher!=null){
            if(voucher.getExpirationDate().isAfter(LocalDateTime.now())){
                return true;
            }else{
                throw new VoucherException("Voucher hết hạn");
            }
        }
        return false;
    }

    public List<Voucher> showAvailableVoucherByProductId(int productId){
        List<Voucher> vouchers = voucherRepository.findAll();
        List<Voucher> availableVouchers = vouchers.stream()
                .filter(voucher-> {
                    if(voucher.getCategory() != null){
                        return voucher.getCategory().getProductList().stream().anyMatch(product -> product.getId() == productId)
                                && voucher.getExpirationDate().isAfter(LocalDateTime.now());
                    }
                    return false;
                })
                .toList();
        if(availableVouchers.isEmpty()){
            return Collections.emptyList();
        }
        return availableVouchers;
    }

    public List<Voucher> showAvailableVoucherToAllProducts(){
        List<Voucher> vouchers = voucherRepository.findAll();
        List<Voucher> availableVouchers = vouchers.stream()
                .filter(voucher-> {
                    if(voucher.getCategory() == null){
                        return voucher.getExpirationDate().isAfter(LocalDateTime.now());
                    }
                    return false;
                })
                .toList();
        if(availableVouchers.isEmpty()){
            return Collections.emptyList();
        }
        return availableVouchers;
    }


    public void applyVoucher(int userId, String code) {
        Voucher voucher = voucherRepository.findByCode(code).orElse(null);
        if (voucher != null) {
            if (voucher.getExpirationDate().isAfter(LocalDateTime.now())) {
                UserVoucher userVoucher = userVoucherService.getUserVoucherByUserIdAndVoucherId(userId, voucher.getId());
                if(userVoucher == null){
                    User user = userService.getUserById(userId);
                    Cart cart = cartService.getCartByUsername(user.getUsername());
                    List<CartItem> cartItems = cart.getCartItems();
                    List<CartItem> validCartItems = cartItems.stream()
                            .filter(cartItem -> cartItem.getProductDetail().getProduct().getCategory().equals(voucher.getCategory()))
                            .toList();
                    if (voucher.getCategory() != null){
                        if (validCartItems.isEmpty()){
                            throw new VoucherException("Voucher này không áp dụng cho sản phẩm bạn chọn");
                        }
                        processVoucher(voucher, user, cart, validCartItems);
                    }else{
                        processVoucher(voucher, user, cart, cartItems);
                    }
                }else{
                    throw new VoucherException("Voucher này đã được sử dụng");
                }
            }else {
                throw new VoucherException("Voucher này đã hết hạn");
            }
        }else{
            throw new VoucherException("Voucher không tồn tại");
        }
    }

    private void processVoucher(Voucher voucher,  User user, Cart cart, List<CartItem> validCartItems) {
        UserVoucher userVoucher;
        double discount = voucher.getDiscount();
        validCartItems
                .forEach(cartItem ->{
                    cartItem.setPrice((int) (cartItem.getPrice() - (cartItem.getPrice() * discount/100)));
                    cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
                    cartItemService.saveCartItem(cartItem);
                });
        userVoucher = new UserVoucher();
        userVoucher.setVoucher(voucher);
        userVoucher.setUser(user);
        userVoucher.setUsed(true);
        userVoucherService.saveUserVoucher(userVoucher);
        cart.setVoucher(voucher);
        List<CartItem> allCartItems = cart.getCartItems();
        cart.setTotal(allCartItems.stream().mapToInt(CartItem::getTotal).sum());
        cartService.saveCart(cart);
    }

    private void processRemoveVoucher(UserVoucher userVoucher, Cart cart, List<CartItem> validCartItems) {
        validCartItems
                .forEach(cartItem -> {
                    cartItem.setPrice(cartItem.getProductDetail().getPrice());
                    cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
                    cartItemService.saveCartItem(cartItem);
                });
        cart.setVoucher(null);
        cart.setTotal(validCartItems.stream().mapToInt(CartItem::getTotal).sum());
        cartService.saveCart(cart);
        //delete user voucher
        userVoucherService.deleteUserVoucher(userVoucher.getId());
    }

}


