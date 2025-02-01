package com.ark.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    KEY_INVALID(0, "Key message invalid", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND(1, "Brand not found", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED(2, "Brand existed", HttpStatus.BAD_REQUEST),
    BRAND_NAME_NOT_EMPTY(3, "Brand name must not be empty", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(4, "Category not found", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(5, "Category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_NOT_EMPTY(6, "Category name must not be empty", HttpStatus.BAD_REQUEST),
    FLAVOR_NOT_FOUND(7, "Flavor not found", HttpStatus.BAD_REQUEST),
    FLAVOR_EXISTED(8, "Flavor existed", HttpStatus.BAD_REQUEST),
    FLAVOR_NAME_NOT_EMPTY(9, "Flavor name must not be empty", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(10, "Product not found", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(11, "Product existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_NOT_EMPTY(12, "Product name must not be empty", HttpStatus.BAD_REQUEST),
    PRODUCT_DETAIL_NOT_FOUND(13, "Product detail not found", HttpStatus.BAD_REQUEST),
    PRODUCT_DETAIL_EXISTED(14, "Product detail existed", HttpStatus.BAD_REQUEST),
    PRODUCT_DETAIL_NAME_NOT_EMPTY(15, "Product detail name must not be empty", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_NOT_FOUND(16, "Product image not found", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_EXISTED(17, "Product image existed", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_NAME_NOT_EMPTY(18, "Product image name must not be empty", HttpStatus.BAD_REQUEST),
    PRODUCT_ORIGIN_NOT_FOUND(19, "Product origin not found", HttpStatus.BAD_REQUEST),
    PRODUCT_ORIGIN_EXISTED(20, "Product origin existed", HttpStatus.BAD_REQUEST),
    PRODUCT_ORIGIN_NAME_NOT_EMPTY(21, "Product origin name must not be empty", HttpStatus.BAD_REQUEST),
    PRODUCT_ORIGIN_CONTINENT_NOT_EMPTY(22, "Product origin continent must not be empty", HttpStatus.BAD_REQUEST),
    VENDOR_NOT_FOUND(23, "Vendor not found", HttpStatus.BAD_REQUEST),
    VENDOR_EXISTED(24, "Vendor existed", HttpStatus.BAD_REQUEST),
    VENDOR_NAME_NOT_EMPTY(25, "Vendor name must not be empty", HttpStatus.BAD_REQUEST),
    VENDOR_ADDRESS_NOT_EMPTY(26, "Vendor address must not be empty", HttpStatus.BAD_REQUEST),
    VENDOR_PHONE_NOT_EMPTY(27, "Vendor phone must not be empty", HttpStatus.BAD_REQUEST),
    VENDOR_EMAIL_NOT_EMPTY(28, "Vendor email must not be empty", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(29, "Review not found", HttpStatus.BAD_REQUEST),
    RATING_NOT_VALID(30, "Rating must be in range of 1 and 5", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EMPTY(31, "Comment must not be empty", HttpStatus.BAD_REQUEST),
    REVIEW_EXISTED(32, "You already reviewed this product", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(33, "User not found", HttpStatus.BAD_REQUEST),
    USER_EXISTED(34, "User existed", HttpStatus.BAD_REQUEST),
    USER_USERNAME_NOT_EMPTY(35, "User name must not be empty", HttpStatus.BAD_REQUEST),
    USER_USERNAME_EXIST(36, "User name existed", HttpStatus.BAD_REQUEST),
    USER_EMAIL_NOT_EMPTY(37, "User email must not be empty", HttpStatus.BAD_REQUEST),
    USER_PHONE_NOT_EMPTY(38, "User phone must not be empty", HttpStatus.BAD_REQUEST),
    USER_FIRST_NAME_NOT_EMPTY(39, "User first name must not be empty", HttpStatus.BAD_REQUEST),
    USER_LAST_NAME_NOT_EMPTY(40, "User last name must not be empty", HttpStatus.BAD_REQUEST),
    USER_PASSWORD_NOT_EMPTY(41, "User password must not be empty", HttpStatus.BAD_REQUEST),
    WISHLIST_ITEM_NOT_FOUND(42, "Wishlist item not found", HttpStatus.BAD_REQUEST),
    WISHLIST_NOT_FOUND(43, "Wishlist not found", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND(44, "Voucher not found", HttpStatus.BAD_REQUEST),
    VOUCHER_EXISTED(45, "Voucher existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NAME_NOT_EMPTY(46, "Voucher name must not be empty", HttpStatus.BAD_REQUEST),
    VOUCHER_DISCOUNT_NOT_VALID(47, "Discount must be in range of 0 and 100", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED(48, "Voucher expired", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_VALID(49, "Voucher not valid", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_USED(50, "Voucher already used", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND_FOR_USER(51, "Voucher not found for user", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(51, "Cart not found", HttpStatus.BAD_REQUEST),
    CART_ITEM_MUST_BE_POSITIVE(52, "Cart item must be positive", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND(53, "Cart item not found", HttpStatus.BAD_REQUEST),
    CART_ITEM_QUANTITY_INVALID(54, "Cart item quantity invalid", HttpStatus.BAD_REQUEST),
    CAN_NOT_BE_NEGATIVE(300, "Can not be negative", HttpStatus.BAD_REQUEST),
    SHORTAGE_QUANTITY(301, "Shortage quantity", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(301, "Email invalid", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(302, "Phone invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(303, "Password not match", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(2000, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2001, "You have no permission", HttpStatus.FORBIDDEN),
    ;
    private final int code;
    private final String message;
    private final HttpStatus status;
}
