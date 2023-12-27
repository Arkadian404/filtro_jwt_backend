package com.ark.security.service.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ark.security.models.Cart;
import com.ark.security.models.user.User;
import com.ark.security.repository.CartRepository;

import java.util.Optional;

import com.ark.security.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CartService.class})
@ExtendWith(SpringExtension.class)
class CartServiceTest {
    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    /**
     * Method under test: {@link CartService#getCartByUsername(String)}
     */
    @Test
    void testGetCartByUsername() {
        Cart cart = new Cart();
        when(cartRepository.findByUserUsername(Mockito.<String>any())).thenReturn(Optional.of(cart));
        assertSame(cart, cartService.getCartByUsername("janedoe"));
        verify(cartRepository).findByUserUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CartService#checkActiveCart(int)}
     */
    @Test
    void testCheckActiveCart() {
        when(cartRepository.findByUserIdAndStatus(anyInt(), anyBoolean())).thenReturn(new Cart());
        assertTrue(cartService.checkActiveCart(1));
        verify(cartRepository).findByUserIdAndStatus(anyInt(), anyBoolean());
    }

    /**
     * Method under test: {@link CartService#checkActiveCart(int)}
     */
    @Test
    void testCheckActiveCart2() {
        when(cartRepository.findByUserIdAndStatus(anyInt(), anyBoolean())).thenReturn(null);
        assertFalse(cartService.checkActiveCart(1));
        verify(cartRepository).findByUserIdAndStatus(anyInt(), anyBoolean());
    }

    /**
     * Method under test: {@link CartService#saveCart(Cart)}
     */
    @Test
    void testSaveCart() {
        when(cartRepository.save(Mockito.<Cart>any())).thenReturn(new Cart());
        cartService.saveCart(new Cart());
        verify(cartRepository).save(Mockito.<Cart>any());
    }

    /**
     * Method under test: {@link CartService#createCart(User)}
     */
    @Test
    void testCreateCart() {
        when(cartRepository.save(Mockito.<Cart>any())).thenReturn(new Cart());
        User user = new User();
        Cart actualCreateCartResult = cartService.createCart(user);
        assertSame(user, actualCreateCartResult.getUser());
        assertTrue(actualCreateCartResult.getStatus());
        assertEquals(0, actualCreateCartResult.getTotal().intValue());
        verify(cartRepository).save(Mockito.<Cart>any());
    }
}

