package com.ark.security.service.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ark.security.dto.CartDto;
import com.ark.security.dto.CartItemDto;
import com.ark.security.dto.ProductDetailDto;
import com.ark.security.dto.UserDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Cart;
import com.ark.security.models.CartItem;
import com.ark.security.models.WishlistItem;
import com.ark.security.models.product.Brand;
import com.ark.security.models.product.Category;
import com.ark.security.models.product.Flavor;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.ProductDetail;
import com.ark.security.models.product.ProductImage;
import com.ark.security.models.product.ProductOrigin;
import com.ark.security.models.product.Sale;
import com.ark.security.models.product.Vendor;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartItemRepository;
import com.ark.security.service.CartItemService;
import com.ark.security.service.CartService;
import com.ark.security.service.product.ProductDetailService;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

