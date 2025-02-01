package com.ark.security.service;

import com.ark.security.dto.request.VoucherRequest;
import com.ark.security.dto.response.VoucherResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.VoucherException;
import com.ark.security.mapper.VoucherMapper;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.UserVoucher;
import com.ark.security.models.Voucher;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartItemRepository;
import com.ark.security.repository.CartRepository;
import com.ark.security.repository.UserVoucherRepository;
import com.ark.security.repository.VoucherRepository;
import com.ark.security.repository.product.CategoryRepository;
import com.ark.security.repository.user.UserRepository;
import com.ark.security.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final CategoryRepository categoryRepository;

    public VoucherResponse getVoucherByVoucherId(int id){
        Voucher voucher = getVoucherById(id);
        return voucherMapper.toVoucherResponse(voucher);
    }

    public List<VoucherResponse> getAllVouchers(){
        return voucherRepository.findAll()
                .stream()
                .map(voucherMapper::toVoucherResponse)
                .toList();
    }



    public VoucherResponse create(VoucherRequest voucherRequest){
        Voucher voucher = voucherMapper.toVoucher(voucherRequest);
        voucher.setCode(RandomStringUtils.random(10, true, true));
        voucher.setCreatedAt(LocalDateTime.now());
        voucher.setCategory(categoryRepository.findById(voucherRequest.getCategoryId())
                .orElse(null));
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }


    public VoucherResponse update(int id, VoucherRequest voucherRequest){
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherMapper.updateVoucher(voucher, voucherRequest);
        voucher.setCategory(categoryRepository.findById(voucherRequest.getCategoryId())
                .orElse(null));
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }

    public void delete(int id){
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherRepository.deleteById(voucher.getId());
    }

    public boolean isVoucherExpired(int id){
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        return voucher.getExpirationDate().isBefore(LocalDateTime.now());
    }

    public List<VoucherResponse> availableVouchersByProductId(int productId){
        List<Voucher> vouchers = voucherRepository.findAll()
                .stream()
                .filter(
                        voucher -> {
                            if(voucher.getCategory() != null){
                                return voucher.getCategory()
                                        .getProductList()
                                        .stream()
                                        .anyMatch(product -> product.getId().equals(productId)
                                                && !isVoucherExpired(voucher.getId())
                                        );
                            }else {
                                return false;
                            }
                        }
                ).toList();
        if(vouchers.isEmpty()){
            return Collections.emptyList();
        }
        return vouchers.stream().map(voucherMapper::toVoucherResponse).toList();
    }

    public List<VoucherResponse> availableVouchersToAllProducts(){
        List<Voucher> vouchers = voucherRepository.findAll()
                .stream()
                .filter(
                        voucher -> {
                            if(voucher.getCategory() == null){
                                return !isVoucherExpired(voucher.getId());
                            }else {
                                return false;
                            }
                        }
                ).toList();
        if(vouchers.isEmpty()){
            return Collections.emptyList();
        }
        return vouchers.stream().map(voucherMapper::toVoucherResponse).toList();
    }

    public void applyVoucher(String voucherCode){
        Voucher voucher = getVoucherByCode(voucherCode);
        User user = getCurrentAuthenticatedUser();
        validateVoucher(voucher, user);

        Cart cart = getUserCart(user.getUsername());
        List<CartItem> validItems = getValidItems(cart, voucher);

        if (voucher.getCategory() != null && validItems.isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_VALID);
        }

        List<CartItem> itemsToApply = voucher.getCategory() == null ? cart.getCartItems() : validItems;
        applyVoucherToCart(voucher, cart, itemsToApply);
    }


    public void removeVoucher(int voucherId){
        Voucher voucher = getVoucherById(voucherId);
        User user = getCurrentAuthenticatedUser();
        UserVoucher userVoucher = getUserVoucher(user.getId(), voucherId);
        Cart cart = getUserCart(user.getUsername());
        List<CartItem> validItems = getValidItems(cart, voucher);
        List<CartItem> itemsToApply = voucher.getCategory() == null ? cart.getCartItems() : validItems;
        processRemoveVoucher(userVoucher, cart, itemsToApply);
    }

    private Voucher getVoucherById(int id){
        return voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
    }

    private Voucher getVoucherByCode(String voucherCode) {
        return voucherRepository.findByCode(voucherCode)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
    }

    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private UserVoucher getUserVoucher(int userId, int voucherId) {
        return userVoucherRepository.findByUserIdAndVoucherId(userId, voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND_FOR_USER));
    }

    private void validateVoucher(Voucher voucher, User user) {
        if (isVoucherExpired(voucher.getId())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        boolean isVoucherUsed = userVoucherRepository
                .findByUserIdAndVoucherId(user.getId(), voucher.getId())
                .isPresent();
        if (isVoucherUsed) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_USED);
        }
    }

    private Cart getUserCart(String username) {
        return cartRepository.findByUserUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }

    private List<CartItem> getValidItems(Cart cart, Voucher voucher) {
        return cart.getCartItems().stream()
                .filter(item -> voucher.getCategory() != null &&
                        item.getProductDetail().getProduct().getCategory().equals(voucher.getCategory()))
                .toList();
    }

    private void applyVoucherToCart(Voucher voucher, Cart cart, List<CartItem> itemsToApply) {
        double discount = voucher.getDiscount() / 100.0;

        itemsToApply.forEach(item -> {
            item.setPrice((int) (item.getPrice() * (1 - discount)));
            item.setTotal(item.getPrice() * item.getQuantity());
            cartItemRepository.save(item);
        });

        cart.setTotal(cart.getCartItems().stream().mapToInt(CartItem::getTotal).sum());
        cart.setVoucher(voucher);
        cartRepository.save(cart);

        saveUserVoucher(voucher, cart.getUser());
    }
    private void processRemoveVoucher(UserVoucher userVoucher, Cart cart, List<CartItem> itemsToProcess) {
        itemsToProcess.forEach(cartItem -> {
            cartItem.setPrice(cartItem.getProductDetail().getPrice());
            cartItem.setTotal(cartItem.getPrice() * cartItem.getQuantity());
            cartItemRepository.save(cartItem);
        });

        cart.setVoucher(null);
        cart.setTotal(cart.getCartItems().stream().mapToInt(CartItem::getTotal).sum());
        cartRepository.save(cart);

        userVoucherRepository.delete(userVoucher);
    }


    private void saveUserVoucher(Voucher voucher, User user) {
        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setVoucher(voucher);
        userVoucher.setUser(user);
        userVoucher.setUsed(true);
        userVoucherRepository.save(userVoucher);
    }

}


