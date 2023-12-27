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

    /**
     * Method under test: {@link CartItemService#getCartItemsByCartId(int)}
     */
    @Test
    void testGetCartItemsByCartId() {
        when(cartItemRepository.findAllByCartId(anyInt())).thenReturn(new ArrayList<>());
        assertTrue(cartItemService.getCartItemsByCartId(1).isEmpty());
        verify(cartItemRepository).findAllByCartId(anyInt());
    }

    /**
     * Method under test: {@link CartItemService#getCartItemsByCartId(int)}
     */
    @Test
    void testGetCartItemsByCartId3() {
        when(cartItemRepository.findAllByCartId(anyInt())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> cartItemService.getCartItemsByCartId(1));
        verify(cartItemRepository).findAllByCartId(anyInt());
    }



    /**
     * Method under test: {@link CartItemService#getCartItemsByCartId(int)}
     */
    @Test
    void testGetCartItemsByCartId6() {
        ProductDetail productDetail = new ProductDetail();
        Brand brand = new Brand();
        ArrayList<ProductDetail> productDetails = new ArrayList<>();
        ArrayList<ProductImage> images = new ArrayList<>();
        LocalDateTime createdAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        LocalDateTime updatedAt = LocalDate.of(1970, 1, 1).atStartOfDay();
        Flavor flavor = new Flavor();
        Category category = new Category();
        Sale sale = new Sale();
        ProductOrigin origin = new ProductOrigin();
        Vendor vendor = new Vendor();
        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
        productDetail.setProduct(new Product(1, "Name", "Slug", brand, 1, 10.0d, productDetails,
                "The characteristics of someone or something", images, createdAt, updatedAt, true, true, true, flavor,
                category, sale, origin, vendor, wishlistItems, new ArrayList<>()));
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.convertToDto()).thenReturn(new CartItemDto());
        doNothing().when(cartItem).setProductDetail(Mockito.<ProductDetail>any());
        cartItem.setProductDetail(productDetail);

        ArrayList<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        when(cartItemRepository.findAllByCartId(anyInt())).thenReturn(cartItemList);
        assertEquals(1, cartItemService.getCartItemsByCartId(1).size());
        verify(cartItemRepository).findAllByCartId(anyInt());
        verify(cartItem).convertToDto();
        verify(cartItem).setProductDetail(Mockito.<ProductDetail>any());
    }

    /**
     * Method under test: {@link CartItemService#addCartItemToCart(CartItemDto, User)}
     */
    @Test
    void testAddCartItemToCart12() {
        when(cartItemRepository.save(Mockito.<CartItem>any())).thenReturn(new CartItem());
        Product product = new Product();
        ArrayList<CartItem> cartItem = new ArrayList<>();
        when(productDetailService.getProductDetailById(anyInt()))
                .thenReturn(new ProductDetail(1, 1, 1, 3, true, product, cartItem, new ArrayList<>()));

        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem());
        Cart cart = mock(Cart.class);
        when(cart.getTotal()).thenReturn(1);
        doNothing().when(cart).setTotal(Mockito.<Integer>any());
        doNothing().when(cart).setUpdatedAt(Mockito.<LocalDateTime>any());
        when(cart.getCartItems()).thenReturn(new ArrayList<>());
        doNothing().when(cart).setCartItems(Mockito.<List<CartItem>>any());
        cart.setCartItems(cartItems);
        doNothing().when(cartService).saveCart(Mockito.<Cart>any());
        when(cartService.checkActiveCart(anyInt())).thenReturn(true);
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);

        ProductDetailDto productDetail = new ProductDetailDto();
        productDetail.setId(1);

        UserDto user = new UserDto();
        user.setId(1);

        CartDto cart2 = new CartDto();
        cart2.setUser(user);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(1);
        cartItemDto.setCart(cart2);
        cartItemDto.setProductDetail(productDetail);

        User user2 = new User();
        user2.setId(1);
        cartItemService.addCartItemToCart(cartItemDto, user2);
        verify(cartItemRepository).save(Mockito.<CartItem>any());
        verify(productDetailService).getProductDetailById(anyInt());
        verify(cartService).checkActiveCart(anyInt());
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cartService).saveCart(Mockito.<Cart>any());
        verify(cart).getTotal();
        verify(cart, atLeast(1)).getCartItems();
        verify(cart).setCartItems(Mockito.<List<CartItem>>any());
        verify(cart).setTotal(Mockito.<Integer>any());
        verify(cart).setUpdatedAt(Mockito.<LocalDateTime>any());
    }

    /**
     * Method under test: {@link CartItemService#addCartItemToCart(CartItemDto, User)}
     */
    @Test
    void testAddCartItemToCart13() {
        when(cartItemRepository.save(Mockito.<CartItem>any())).thenReturn(new CartItem());
        Product product = new Product();
        ArrayList<CartItem> cartItem = new ArrayList<>();
        when(productDetailService.getProductDetailById(anyInt()))
                .thenReturn(new ProductDetail(1, 1, 1, 3, true, product, cartItem, new ArrayList<>()));

        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem());
        Cart cart = mock(Cart.class);
        doThrow(new NotFoundException("An error occurred")).when(cart).setUpdatedAt(Mockito.<LocalDateTime>any());
        when(cart.getCartItems()).thenReturn(new ArrayList<>());
        doNothing().when(cart).setCartItems(Mockito.<List<CartItem>>any());
        cart.setCartItems(cartItems);
        when(cartService.checkActiveCart(anyInt())).thenReturn(true);
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);

        ProductDetailDto productDetail = new ProductDetailDto();
        productDetail.setId(1);

        UserDto user = new UserDto();
        user.setId(1);

        CartDto cart2 = new CartDto();
        cart2.setUser(user);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(1);
        cartItemDto.setCart(cart2);
        cartItemDto.setProductDetail(productDetail);

        User user2 = new User();
        user2.setId(1);
        assertThrows(NotFoundException.class, () -> cartItemService.addCartItemToCart(cartItemDto, user2));
        verify(cartItemRepository).save(Mockito.<CartItem>any());
        verify(productDetailService).getProductDetailById(anyInt());
        verify(cartService).checkActiveCart(anyInt());
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cart, atLeast(1)).getCartItems();
        verify(cart).setCartItems(Mockito.<List<CartItem>>any());
        verify(cart).setUpdatedAt(Mockito.<LocalDateTime>any());
    }


    /**
     * Method under test: {@link CartItemService#addCartItemToCart(CartItemDto, User)}
     */
    @Test
    void testAddCartItemToCart15() {
        when(cartItemRepository.save(Mockito.<CartItem>any())).thenReturn(new CartItem());
        ProductDetail productDetail = mock(ProductDetail.class);
        when(productDetail.getPrice()).thenReturn(1);
        when(productDetailService.getProductDetailById(anyInt())).thenReturn(productDetail);

        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem());
        Cart cart = mock(Cart.class);
        when(cart.getTotal()).thenReturn(1);
        doNothing().when(cart).setTotal(Mockito.<Integer>any());
        doNothing().when(cart).setUpdatedAt(Mockito.<LocalDateTime>any());
        when(cart.getCartItems()).thenReturn(new ArrayList<>());
        doNothing().when(cart).setCartItems(Mockito.<List<CartItem>>any());
        cart.setCartItems(cartItems);
        doNothing().when(cartService).saveCart(Mockito.<Cart>any());
        when(cartService.checkActiveCart(anyInt())).thenReturn(true);
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);

        ProductDetailDto productDetail2 = new ProductDetailDto();
        productDetail2.setId(1);

        UserDto user = new UserDto();
        user.setId(1);

        CartDto cart2 = new CartDto();
        cart2.setUser(user);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(1);
        cartItemDto.setCart(cart2);
        cartItemDto.setProductDetail(productDetail2);

        User user2 = new User();
        user2.setId(1);
        cartItemService.addCartItemToCart(cartItemDto, user2);
        verify(cartItemRepository).save(Mockito.<CartItem>any());
        verify(productDetailService).getProductDetailById(anyInt());
        verify(productDetail).getPrice();
        verify(cartService).checkActiveCart(anyInt());
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cartService).saveCart(Mockito.<Cart>any());
        verify(cart).getTotal();
        verify(cart, atLeast(1)).getCartItems();
        verify(cart).setCartItems(Mockito.<List<CartItem>>any());
        verify(cart).setTotal(Mockito.<Integer>any());
        verify(cart).setUpdatedAt(Mockito.<LocalDateTime>any());
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity2() {
        when(cartService.getCartByUsername(Mockito.<String>any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartService).getCartByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity3() {
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.empty());
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(new Cart());
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartService).getCartByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity4() {
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(new CartItem()));

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartService).getCartByUsername(Mockito.<String>any());
    }


    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity6() {
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(new CartItem()));
        Cart cart = mock(Cart.class);
        when(cart.getCartItems()).thenReturn(new ArrayList<>());
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cart).getCartItems();
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity7() {
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getId()).thenReturn(1);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Cart cart = mock(Cart.class);
        when(cart.getCartItems()).thenReturn(new ArrayList<>());
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItem).getId();
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cart).getCartItems();
    }


    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity10() {
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getId()).thenThrow(new NotFoundException("An error occurred"));
        Optional<CartItem> ofResult = Optional.of(cartItem);
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Cart cart = mock(Cart.class);
        when(cart.getCartItems()).thenReturn(new ArrayList<>());
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart);
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItem).getId();
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cart).getCartItems();
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity11() {
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getId()).thenReturn(1);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        when(cartItemRepository.save(Mockito.<CartItem>any())).thenReturn(new CartItem());
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        ArrayList<CartItem> cartItemList = new ArrayList<>();
        Cart cart = new Cart();
        ProductDetail productDetail = new ProductDetail();
        cartItemList.add(new CartItem(1, cart, productDetail, 1, 1, 1, LocalDate.of(1970, 1, 1).atStartOfDay()));
        Cart cart2 = mock(Cart.class);
        when(cart2.getTotal()).thenReturn(1);
        doNothing().when(cart2).setTotal(Mockito.<Integer>any());
        when(cart2.getCartItems()).thenReturn(cartItemList);
        doNothing().when(cartService).saveCart(Mockito.<Cart>any());
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart2);
        cartItemService.updateCartItemQuantity(1, new User(), 10);
        verify(cartItemRepository).save(Mockito.<CartItem>any());
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItem).getId();
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cartService).saveCart(Mockito.<Cart>any());
        verify(cart2).getTotal();
        verify(cart2).getCartItems();
        verify(cart2).setTotal(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity12() {
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getId()).thenReturn(1);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        when(cartItemRepository.save(Mockito.<CartItem>any())).thenReturn(new CartItem());
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        ArrayList<CartItem> cartItemList = new ArrayList<>();
        Cart cart = new Cart();
        ProductDetail productDetail = new ProductDetail();
        cartItemList.add(new CartItem(1, cart, productDetail, 1, 1, 1, LocalDate.of(1970, 1, 1).atStartOfDay()));
        Cart cart2 = mock(Cart.class);
        when(cart2.getTotal()).thenThrow(new NotFoundException("An error occurred"));
        when(cart2.getCartItems()).thenReturn(cartItemList);
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart2);
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).save(Mockito.<CartItem>any());
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItem).getId();
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cart2).getTotal();
        verify(cart2).getCartItems();
    }

    /**
     * Method under test: {@link CartItemService#updateCartItemQuantity(int, User, int)}
     */
    @Test
    void testUpdateCartItemQuantity13() {
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getId()).thenReturn(6);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        ArrayList<CartItem> cartItemList = new ArrayList<>();
        Cart cart = new Cart();
        ProductDetail productDetail = new ProductDetail();
        cartItemList.add(new CartItem(1, cart, productDetail, 1, 1, 1, LocalDate.of(1970, 1, 1).atStartOfDay()));
        Cart cart2 = mock(Cart.class);
        when(cart2.getCartItems()).thenReturn(cartItemList);
        when(cartService.getCartByUsername(Mockito.<String>any())).thenReturn(cart2);
        assertThrows(NotFoundException.class, () -> cartItemService.updateCartItemQuantity(1, new User(), 10));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItem, atLeast(1)).getId();
        verify(cartService).getCartByUsername(Mockito.<String>any());
        verify(cart2).getCartItems();
    }


    /**
     * Method under test: {@link CartItemService#getCartItemById(int)}
     */
    @Test
    void testGetCartItemById() {
        CartItem cartItem = new CartItem();
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(cartItem));
        assertSame(cartItem, cartItemService.getCartItemById(1));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link CartItemService#getCartItemById(int)}
     */
    @Test
    void testGetCartItemById2() {
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartItemService.getCartItemById(1));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link CartItemService#getCartItemById(int)}
     */
    @Test
    void testGetCartItemById3() {
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenThrow(new NotFoundException("An error occurred"));
        assertThrows(NotFoundException.class, () -> cartItemService.getCartItemById(1));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
    }


    /**
     * Method under test: {@link CartItemService#deleteCartItem(int)}
     */
    @Test
    void testDeleteCartItem4() {
        Cart cart = new Cart();
        cart.setTotal(0);
        Cart cart2 = new Cart();
        ProductDetail productDetail = new ProductDetail();

        CartItem cartItem = new CartItem(1, cart2, productDetail, 1, 1, 1, LocalDate.of(1970, 1, 1).atStartOfDay());
        cartItem.setCart(cart);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        doNothing().when(cartItemRepository).deleteById(Mockito.<Integer>any());
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        cartItemService.deleteCartItem(1);
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItemRepository).deleteById(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link CartItemService#deleteCartItem(int)}
     */
    @Test
    void testDeleteCartItem6() {
        Cart cart = new Cart();
        cart.setTotal(0);

        Cart cart2 = new Cart();
        cart2.setTotal(1);
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getTotal()).thenReturn(1);
        when(cartItem.getCart()).thenReturn(cart2);
        doNothing().when(cartItem).setCart(Mockito.<Cart>any());
        cartItem.setCart(cart);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        doNothing().when(cartItemRepository).deleteById(Mockito.<Integer>any());
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        cartItemService.deleteCartItem(1);
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItemRepository).deleteById(Mockito.<Integer>any());
        verify(cartItem, atLeast(1)).getCart();
        verify(cartItem).getTotal();
        verify(cartItem).setCart(Mockito.<Cart>any());
    }

    /**
     * Method under test: {@link CartItemService#deleteCartItem(int)}
     */
    @Test
    void testDeleteCartItem7() {
        Cart cart = new Cart();
        cart.setTotal(0);
        Cart cart2 = mock(Cart.class);
        when(cart2.getTotal()).thenReturn(1);
        doNothing().when(cart2).setTotal(Mockito.<Integer>any());
        cart2.setTotal(1);
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getTotal()).thenReturn(1);
        when(cartItem.getCart()).thenReturn(cart2);
        doNothing().when(cartItem).setCart(Mockito.<Cart>any());
        cartItem.setCart(cart);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        doNothing().when(cartItemRepository).deleteById(Mockito.<Integer>any());
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        cartItemService.deleteCartItem(1);
        verify(cartItemRepository).findById(Mockito.<Integer>any());
        verify(cartItemRepository).deleteById(Mockito.<Integer>any());
        verify(cartItem, atLeast(1)).getCart();
        verify(cartItem).getTotal();
        verify(cartItem).setCart(Mockito.<Cart>any());
        verify(cart2).getTotal();
        verify(cart2, atLeast(1)).setTotal(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link CartItemService#deleteCartItem(int)}
     */
    @Test
    void testDeleteCartItem9() {
        when(cartItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartItemService.deleteCartItem(1));
        verify(cartItemRepository).findById(Mockito.<Integer>any());
    }
}

