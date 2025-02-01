package com.ark.security.service.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ark.security.repository.CartItemRepository;
import com.ark.security.service.CartItemService;
import com.ark.security.service.CartService;
import com.ark.security.service.product.ProductDetailService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CartItemService.class})
@ExtendWith(SpringExtension.class)
class CartItemServiceTest {
    @MockBean
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemService cartItemService;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductDetailService productDetailService;


}

