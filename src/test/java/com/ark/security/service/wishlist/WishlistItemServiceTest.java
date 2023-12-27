//package com.ark.security.service.wishlist;
//
//
//import com.ark.security.dto.ProductDto;
//import com.ark.security.dto.UserDto;
//import com.ark.security.dto.WishlistDto;
//import com.ark.security.dto.WishlistItemDto;
//import com.ark.security.exception.NotFoundException;
//import com.ark.security.models.Wishlist;
//import com.ark.security.models.WishlistItem;
//import com.ark.security.models.product.Product;
//import com.ark.security.models.product.ProductDetail;
//import com.ark.security.models.user.User;
//import com.ark.security.repository.WishlistItemRepository;
//import com.ark.security.service.WishlistItemService;
//import com.ark.security.service.WishlistService;
//import com.ark.security.service.product.ProductService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//@ContextConfiguration(classes = {WishlistItemService.class})
//@ExtendWith(SpringExtension.class)
//class WishlistItemServiceTest {
//    @MockBean
//    private ProductService productService;
//
//    @MockBean
//    private WishlistItemRepository wishlistItemRepository;
//
//    @Autowired
//    private WishlistItemService wishlistItemService;
//
//    @MockBean
//    private WishlistService wishlistService;
//
//    /**
//     * Method under test: {@link WishlistItemService#getWishlistItemById(int)}
//     */
//    @Test
//    void testGetWishlistItemById() {
//        WishlistItem wishlistItem = new WishlistItem();
//        when(wishlistItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(wishlistItem));
//        assertSame(wishlistItem, wishlistItemService.getWishlistItemById(1));
//        verify(wishlistItemRepository).findById(Mockito.<Integer>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getWishlistItemById(int)}
//     */
//    @Test
//    void testGetWishlistItemById2() {
//        when(wishlistItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.empty());
//        assertThrows(NotFoundException.class, () -> wishlistItemService.getWishlistItemById(1));
//        verify(wishlistItemRepository).findById(Mockito.<Integer>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getWishlistItemById(int)}
//     */
//    @Test
//    void testGetWishlistItemById3() {
//        when(wishlistItemRepository.findById(Mockito.<Integer>any()))
//                .thenThrow(new NotFoundException("An error occurred"));
//        assertThrows(NotFoundException.class, () -> wishlistItemService.getWishlistItemById(1));
//        verify(wishlistItemRepository).findById(Mockito.<Integer>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getWishlistItemByProductId(int)}
//     */
//    @Test
//    void testGetWishlistItemByProductId() {
//        WishlistItem wishlistItem = new WishlistItem();
//        when(wishlistItemRepository.findByProductId(anyInt())).thenReturn(Optional.of(wishlistItem));
//        assertSame(wishlistItem, wishlistItemService.getWishlistItemByProductId(1));
//        verify(wishlistItemRepository).findByProductId(anyInt());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getWishlistItemByProductId(int)}
//     */
//    @Test
//    void testGetWishlistItemByProductId2() {
//        when(wishlistItemRepository.findByProductId(anyInt())).thenReturn(Optional.empty());
//        assertThrows(NotFoundException.class, () -> wishlistItemService.getWishlistItemByProductId(1));
//        verify(wishlistItemRepository).findByProductId(anyInt());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getWishlistItemByProductId(int)}
//     */
//    @Test
//    void testGetWishlistItemByProductId3() {
//        when(wishlistItemRepository.findByProductId(anyInt())).thenThrow(new NotFoundException("An error occurred"));
//        assertThrows(NotFoundException.class, () -> wishlistItemService.getWishlistItemByProductId(1));
//        verify(wishlistItemRepository).findByProductId(anyInt());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getItemsByWishlistId(int)}
//     */
//    @Test
//    void testGetItemsByWishlistId() {
//        when(wishlistItemRepository.findAllByWishlistId(anyInt())).thenReturn(Optional.of(new ArrayList<>()));
//        assertTrue(wishlistItemService.getItemsByWishlistId(1).isEmpty());
//        verify(wishlistItemRepository).findAllByWishlistId(anyInt());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getItemsByWishlistId(int)}
//     */
//    @Test
//    void testGetItemsByWishlistId4() {
//        when(wishlistItemRepository.findAllByWishlistId(anyInt())).thenReturn(Optional.empty());
//        assertTrue(wishlistItemService.getItemsByWishlistId(1).isEmpty());
//        verify(wishlistItemRepository).findAllByWishlistId(anyInt());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getItemsByWishlistId(int)}
//     */
//    @Test
//    void testGetItemsByWishlistId5() {
//        when(wishlistItemRepository.findAllByWishlistId(anyInt())).thenThrow(new NotFoundException("An error occurred"));
//        assertThrows(NotFoundException.class, () -> wishlistItemService.getItemsByWishlistId(1));
//        verify(wishlistItemRepository).findAllByWishlistId(anyInt());
//    }
//
//
//    /**
//     * Method under test: {@link WishlistItemService#getItemsByWishlistId(int)}
//     */
//    @Test
//    void testGetItemsByWishlistId11() {
//        User user = new User();
//        user.setId(1);
//
//        Wishlist wishlist = new Wishlist();
//        wishlist.setUser(user);
//
//        ArrayList<ProductDetail> productDetails = new ArrayList<>();
//        productDetails.add(new ProductDetail());
//        Product product = mock(Product.class);
//        ProductDto productDto = new ProductDto();
//        when(product.convertToDto()).thenReturn(productDto);
//        doNothing().when(product).setProductDetails(Mockito.<List<ProductDetail>>any());
//        product.setProductDetails(productDetails);
//        Wishlist wishlist2 = new Wishlist();
//
//        WishlistItem wishlistItem = new WishlistItem(1, wishlist2, product, LocalDate.of(1970, 1, 1).atStartOfDay());
//        wishlistItem.setWishlist(wishlist);
//
//        ArrayList<WishlistItem> wishlistItemList = new ArrayList<>();
//        wishlistItemList.add(wishlistItem);
//        Optional<List<WishlistItem>> ofResult = Optional.of(wishlistItemList);
//        when(wishlistItemRepository.findAllByWishlistId(anyInt())).thenReturn(ofResult);
//        List<WishlistItemDto> actualItemsByWishlistId = wishlistItemService.getItemsByWishlistId(1);
//        assertEquals(1, actualItemsByWishlistId.size());
//        WishlistItemDto getResult = actualItemsByWishlistId.get(0);
//        assertEquals("00:00", getResult.getAddDate().toLocalTime().toString());
//        assertEquals(1, getResult.getId().intValue());
//        assertSame(productDto, getResult.getProduct());
//        WishlistDto wishlist3 = getResult.getWishlist();
//        assertNull(wishlist3.getId());
//        assertNull(wishlist3.getCreatedAt());
//        assertNull(wishlist3.getStatus());
//        assertNull(wishlist3.getUpdatedAt());
//        UserDto user2 = wishlist3.getUser();
//        assertNull(user2.getUsername());
//        assertNull(user2.getProvince());
//        assertNull(user2.getPhone());
//        assertNull(user2.getLastname());
//        assertEquals(1, user2.getId().intValue());
//        assertNull(user2.getFirstname());
//        assertNull(user2.getEmail());
//        assertNull(user2.getDob());
//        assertNull(user2.getDistrict());
//        assertNull(user2.getAddress());
//        assertNull(user2.getWard());
//        verify(wishlistItemRepository).findAllByWishlistId(anyInt());
//        verify(product).convertToDto();
//        verify(product).setProductDetails(Mockito.<List<ProductDetail>>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#getItemsByWishlistId(int)}
//     */
//    @Test
//    void testGetItemsByWishlistId12() {
//        User user = new User();
//        user.setId(1);
//
//        Wishlist wishlist = new Wishlist();
//        wishlist.setUser(user);
//        WishlistItem wishlistItem = mock(WishlistItem.class);
//        when(wishlistItem.convertToDto()).thenReturn(new WishlistItemDto());
//        doNothing().when(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//        wishlistItem.setWishlist(wishlist);
//
//        ArrayList<WishlistItem> wishlistItemList = new ArrayList<>();
//        wishlistItemList.add(wishlistItem);
//        Optional<List<WishlistItem>> ofResult = Optional.of(wishlistItemList);
//        when(wishlistItemRepository.findAllByWishlistId(anyInt())).thenReturn(ofResult);
//        assertEquals(1, wishlistItemService.getItemsByWishlistId(1).size());
//        verify(wishlistItemRepository).findAllByWishlistId(anyInt());
//        verify(wishlistItem).convertToDto();
//        verify(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#add(WishlistItemDto, Wishlist)}
//     */
//    @Test
//    void testAdd7() {
//        when(wishlistItemRepository.save(Mockito.<WishlistItem>any())).thenReturn(new WishlistItem());
//        doNothing().when(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        when(productService.getProductById(anyInt())).thenReturn(new Product());
//
//        ProductDto product = new ProductDto();
//        product.setId(1);
//        ProductDto productDto = mock(ProductDto.class);
//        when(productDto.getId()).thenReturn(1);
//        WishlistItemDto wishlistItemDto = mock(WishlistItemDto.class);
//        when(wishlistItemDto.convertToEntity()).thenReturn(new WishlistItem());
//        when(wishlistItemDto.getProduct()).thenReturn(productDto);
//        doNothing().when(wishlistItemDto).setProduct(Mockito.<ProductDto>any());
//        wishlistItemDto.setProduct(product);
//
//        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
//        wishlistItems.add(new WishlistItem());
//        Wishlist wishlist = mock(Wishlist.class);
//        when(wishlist.getWishlistItems()).thenReturn(new ArrayList<>());
//        doNothing().when(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        doNothing().when(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        wishlist.setWishlistItems(wishlistItems);
//        wishlistItemService.add(wishlistItemDto, wishlist);
//        verify(wishlistItemRepository).save(Mockito.<WishlistItem>any());
//        verify(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        verify(productService).getProductById(anyInt());
//        verify(wishlistItemDto).getProduct();
//        verify(wishlistItemDto).convertToEntity();
//        verify(wishlistItemDto).setProduct(Mockito.<ProductDto>any());
//        verify(productDto).getId();
//        verify(wishlist, atLeast(1)).getWishlistItems();
//        verify(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        verify(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#add(WishlistItemDto, Wishlist)}
//     */
//    @Test
//    void testAdd9() {
//        when(wishlistItemRepository.save(Mockito.<WishlistItem>any())).thenReturn(new WishlistItem());
//        doNothing().when(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        when(productService.getProductById(anyInt())).thenReturn(new Product());
//
//        ProductDto product = new ProductDto();
//        product.setId(1);
//        ProductDto productDto = mock(ProductDto.class);
//        when(productDto.getId()).thenReturn(1);
//        WishlistItem wishlistItem = mock(WishlistItem.class);
//        doNothing().when(wishlistItem).setAddDate(Mockito.<LocalDateTime>any());
//        doNothing().when(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//        WishlistItemDto wishlistItemDto = mock(WishlistItemDto.class);
//        when(wishlistItemDto.convertToEntity()).thenReturn(wishlistItem);
//        when(wishlistItemDto.getProduct()).thenReturn(productDto);
//        doNothing().when(wishlistItemDto).setProduct(Mockito.<ProductDto>any());
//        wishlistItemDto.setProduct(product);
//
//        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
//        wishlistItems.add(new WishlistItem());
//        Wishlist wishlist = mock(Wishlist.class);
//        when(wishlist.getWishlistItems()).thenReturn(new ArrayList<>());
//        doNothing().when(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        doNothing().when(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        wishlist.setWishlistItems(wishlistItems);
//        wishlistItemService.add(wishlistItemDto, wishlist);
//        verify(wishlistItemRepository).save(Mockito.<WishlistItem>any());
//        verify(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        verify(productService).getProductById(anyInt());
//        verify(wishlistItemDto).getProduct();
//        verify(wishlistItemDto).convertToEntity();
//        verify(wishlistItemDto).setProduct(Mockito.<ProductDto>any());
//        verify(wishlistItem).setAddDate(Mockito.<LocalDateTime>any());
//        verify(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//        verify(productDto).getId();
//        verify(wishlist, atLeast(1)).getWishlistItems();
//        verify(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        verify(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#addItems(WishlistItemDto, User)}
//     */
//    @Test
//    void testAddItems11() {
//        when(wishlistItemRepository.save(Mockito.<WishlistItem>any())).thenReturn(new WishlistItem());
//
//        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
//        wishlistItems.add(new WishlistItem());
//        Wishlist wishlist = mock(Wishlist.class);
//        doNothing().when(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        when(wishlist.getWishlistItems()).thenReturn(new ArrayList<>());
//        doNothing().when(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        wishlist.setWishlistItems(wishlistItems);
//        doNothing().when(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        when(wishlistService.getByUsername(Mockito.<String>any())).thenReturn(wishlist);
//        when(productService.getProductById(anyInt())).thenReturn(new Product());
//
//        ProductDto product = new ProductDto();
//        product.setId(1);
//
//        UserDto user = new UserDto();
//        user.setId(1);
//
//        WishlistDto wishlist2 = new WishlistDto();
//        wishlist2.setUser(user);
//
//        ProductDto productDto = new ProductDto();
//        productDto.setId(1);
//        WishlistItemDto wishlistDto = mock(WishlistItemDto.class);
//        when(wishlistDto.convertToEntity()).thenReturn(new WishlistItem());
//        when(wishlistDto.getProduct()).thenReturn(productDto);
//        doNothing().when(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        doNothing().when(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//        wishlistDto.setWishlist(wishlist2);
//        wishlistDto.setProduct(product);
//        wishlistItemService.addItems(wishlistDto, new User());
//        verify(wishlistItemRepository).save(Mockito.<WishlistItem>any());
//        verify(wishlistService).getByUsername(Mockito.<String>any());
//        verify(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        verify(wishlist, atLeast(1)).getWishlistItems();
//        verify(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        verify(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        verify(productService).getProductById(anyInt());
//        verify(wishlistDto).getProduct();
//        verify(wishlistDto).convertToEntity();
//        verify(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        verify(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#addItems(WishlistItemDto, User)}
//     */
//    @Test
//    void testAddItems12() {
//        when(wishlistItemRepository.save(Mockito.<WishlistItem>any())).thenReturn(new WishlistItem());
//
//        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
//        wishlistItems.add(new WishlistItem());
//        Wishlist wishlist = mock(Wishlist.class);
//        doThrow(new NotFoundException("An error occurred")).when(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        when(wishlist.getWishlistItems()).thenReturn(new ArrayList<>());
//        doNothing().when(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        wishlist.setWishlistItems(wishlistItems);
//        when(wishlistService.getByUsername(Mockito.<String>any())).thenReturn(wishlist);
//        when(productService.getProductById(anyInt())).thenReturn(new Product());
//
//        ProductDto product = new ProductDto();
//        product.setId(1);
//
//        UserDto user = new UserDto();
//        user.setId(1);
//
//        WishlistDto wishlist2 = new WishlistDto();
//        wishlist2.setUser(user);
//
//        ProductDto productDto = new ProductDto();
//        productDto.setId(1);
//        WishlistItemDto wishlistDto = mock(WishlistItemDto.class);
//        when(wishlistDto.convertToEntity()).thenReturn(new WishlistItem());
//        when(wishlistDto.getProduct()).thenReturn(productDto);
//        doNothing().when(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        doNothing().when(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//        wishlistDto.setWishlist(wishlist2);
//        wishlistDto.setProduct(product);
//        assertThrows(NotFoundException.class, () -> wishlistItemService.addItems(wishlistDto, new User()));
//        verify(wishlistItemRepository).save(Mockito.<WishlistItem>any());
//        verify(wishlistService).getByUsername(Mockito.<String>any());
//        verify(wishlist, atLeast(1)).getWishlistItems();
//        verify(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        verify(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        verify(productService).getProductById(anyInt());
//        verify(wishlistDto).getProduct();
//        verify(wishlistDto).convertToEntity();
//        verify(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        verify(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#addItems(WishlistItemDto, User)}
//     */
//    @Test
//    void testAddItems14() {
//        when(wishlistItemRepository.save(Mockito.<WishlistItem>any())).thenReturn(new WishlistItem());
//
//        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
//        wishlistItems.add(new WishlistItem());
//        Wishlist wishlist = mock(Wishlist.class);
//        doNothing().when(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        when(wishlist.getWishlistItems()).thenReturn(new ArrayList<>());
//        doNothing().when(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        wishlist.setWishlistItems(wishlistItems);
//        doNothing().when(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        when(wishlistService.getByUsername(Mockito.<String>any())).thenReturn(wishlist);
//        when(productService.getProductById(anyInt())).thenReturn(new Product());
//
//        ProductDto product = new ProductDto();
//        product.setId(1);
//
//        UserDto user = new UserDto();
//        user.setId(1);
//
//        WishlistDto wishlist2 = new WishlistDto();
//        wishlist2.setUser(user);
//
//        ProductDto productDto = new ProductDto();
//        productDto.setId(1);
//        WishlistItem wishlistItem = mock(WishlistItem.class);
//        doNothing().when(wishlistItem).setAddDate(Mockito.<LocalDateTime>any());
//        doNothing().when(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//        WishlistItemDto wishlistDto = mock(WishlistItemDto.class);
//        when(wishlistDto.convertToEntity()).thenReturn(wishlistItem);
//        when(wishlistDto.getProduct()).thenReturn(productDto);
//        doNothing().when(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        doNothing().when(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//        wishlistDto.setWishlist(wishlist2);
//        wishlistDto.setProduct(product);
//        wishlistItemService.addItems(wishlistDto, new User());
//        verify(wishlistItemRepository).save(Mockito.<WishlistItem>any());
//        verify(wishlistService).getByUsername(Mockito.<String>any());
//        verify(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        verify(wishlist, atLeast(1)).getWishlistItems();
//        verify(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        verify(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        verify(productService).getProductById(anyInt());
//        verify(wishlistDto).getProduct();
//        verify(wishlistDto).convertToEntity();
//        verify(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        verify(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//        verify(wishlistItem).setAddDate(Mockito.<LocalDateTime>any());
//        verify(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#addItems(WishlistItemDto, User)}
//     */
//    @Test
//    void testAddItems15() {
//        when(wishlistItemRepository.save(Mockito.<WishlistItem>any())).thenReturn(new WishlistItem());
//
//        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
//        wishlistItems.add(new WishlistItem());
//        Wishlist wishlist = mock(Wishlist.class);
//        doNothing().when(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        when(wishlist.getWishlistItems()).thenReturn(new ArrayList<>());
//        doNothing().when(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        wishlist.setWishlistItems(wishlistItems);
//        doNothing().when(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        when(wishlistService.getByUsername(Mockito.<String>any())).thenReturn(wishlist);
//        when(productService.getProductById(anyInt())).thenReturn(new Product());
//
//        ProductDto product = new ProductDto();
//        product.setId(1);
//
//        UserDto user = new UserDto();
//        user.setId(1);
//
//        WishlistDto wishlist2 = new WishlistDto();
//        wishlist2.setUser(user);
//        ProductDto productDto = mock(ProductDto.class);
//        when(productDto.getId()).thenReturn(1);
//        doNothing().when(productDto).setId(Mockito.<Integer>any());
//        productDto.setId(1);
//        WishlistItem wishlistItem = mock(WishlistItem.class);
//        doNothing().when(wishlistItem).setAddDate(Mockito.<LocalDateTime>any());
//        doNothing().when(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//        WishlistItemDto wishlistDto = mock(WishlistItemDto.class);
//        when(wishlistDto.convertToEntity()).thenReturn(wishlistItem);
//        when(wishlistDto.getProduct()).thenReturn(productDto);
//        doNothing().when(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        doNothing().when(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//        wishlistDto.setWishlist(wishlist2);
//        wishlistDto.setProduct(product);
//        wishlistItemService.addItems(wishlistDto, new User());
//        verify(wishlistItemRepository).save(Mockito.<WishlistItem>any());
//        verify(wishlistService).getByUsername(Mockito.<String>any());
//        verify(wishlistService).saveWishlist(Mockito.<Wishlist>any());
//        verify(wishlist, atLeast(1)).getWishlistItems();
//        verify(wishlist).setUpdatedAt(Mockito.<LocalDateTime>any());
//        verify(wishlist).setWishlistItems(Mockito.<List<WishlistItem>>any());
//        verify(productService).getProductById(anyInt());
//        verify(wishlistDto).getProduct();
//        verify(wishlistDto).convertToEntity();
//        verify(wishlistDto).setProduct(Mockito.<ProductDto>any());
//        verify(wishlistDto).setWishlist(Mockito.<WishlistDto>any());
//        verify(wishlistItem).setAddDate(Mockito.<LocalDateTime>any());
//        verify(wishlistItem).setWishlist(Mockito.<Wishlist>any());
//        verify(productDto).getId();
//        verify(productDto).setId(Mockito.<Integer>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#deleteCartItem(int)}
//     */
//    @Test
//    void testDeleteCartItem() {
//        doNothing().when(wishlistItemRepository).deleteById(Mockito.<Integer>any());
//        when(wishlistItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(new WishlistItem()));
//        wishlistItemService.deleteCartItem(1);
//        verify(wishlistItemRepository).findById(Mockito.<Integer>any());
//        verify(wishlistItemRepository).deleteById(Mockito.<Integer>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#deleteCartItem(int)}
//     */
//    @Test
//    void testDeleteCartItem2() {
//        doThrow(new NotFoundException("An error occurred")).when(wishlistItemRepository)
//                .deleteById(Mockito.<Integer>any());
//        when(wishlistItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(new WishlistItem()));
//        assertThrows(NotFoundException.class, () -> wishlistItemService.deleteCartItem(1));
//        verify(wishlistItemRepository).findById(Mockito.<Integer>any());
//        verify(wishlistItemRepository).deleteById(Mockito.<Integer>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistItemService#deleteCartItem(int)}
//     */
//    @Test
//    void testDeleteCartItem4() {
//        when(wishlistItemRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.empty());
//        assertThrows(NotFoundException.class, () -> wishlistItemService.deleteCartItem(1));
//        verify(wishlistItemRepository).findById(Mockito.<Integer>any());
//    }
//}
//
//
