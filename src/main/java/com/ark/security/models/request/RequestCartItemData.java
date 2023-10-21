package com.ark.security.models.request;

import com.ark.security.models.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCartItemData {
    private List<CartItem> cartItems;
    private List<Integer> productDetailIds;
}
