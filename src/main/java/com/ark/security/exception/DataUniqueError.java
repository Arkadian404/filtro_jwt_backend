package com.ark.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataUniqueError {
    DUPLICATE_USERNAME("user.username_unique", "Username is already taken"),
    DUPLICATE_USER_EMAIL("user.email_unique", "Email is already taken"),
    DUPLICATE_BRAND_NAME("brand.name_unique", "Brand name is already taken"),
    DUPLICATE_CATEGORY_NAME("category.name_unique", "Category name is already taken"),
    DUPLICATE_PRODUCT_NAME("product.name_unique", "Product name is already taken"),
    DUPLICATE_FLAVOR_NAME("flavor.name_unique", "Flavor name is already taken"),
    DUPLICATE_ORIGIN_NAME("product_origin.name_unique", "Origin name is already taken"),
    ;

    private final String dbMessage;
    private final String userMessage;

    public static String getFriendlyMessage(String dbMessage){
        String lowerCaseDbMessage = dbMessage.toLowerCase();
        for(DataUniqueError error: values()){
            if(lowerCaseDbMessage.contains(error.dbMessage)){
                return error.userMessage;
            }
        }
        return "Data is already taken";
    }
}
