//package com.ark.security.service.wishlist;
//
//import com.ark.security.models.Wishlist;
//import com.ark.security.models.user.User;
//import com.ark.security.repository.WishlistRepository;
//
//import com.ark.security.service.WishlistService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ContextConfiguration(classes = {WishlistService.class})
//@ExtendWith(SpringExtension.class)
//class WishlistServiceTest {
//    @MockBean
//    private WishlistRepository wishlistRepository;
//
//    @Autowired
//    private WishlistService wishlistService;
//
//    /**
//     * Method under test: {@link WishlistService#getByUsername(String)}
//     */
//    @Test
//    void testGetByUsername() {
//        Wishlist wishlist = new Wishlist();
//        when(wishlistRepository.findByUserUsername(Mockito.<String>any())).thenReturn(Optional.of(wishlist));
//        assertSame(wishlist, wishlistService.getByUsername("janedoe"));
//        verify(wishlistRepository).findByUserUsername(Mockito.<String>any());
//    }
//
//    /**
//     * Method under test: {@link WishlistService#createWishlist(User)}
//     */
//    @Test
//    void testCreateWishlist() {
//        when(wishlistRepository.save(Mockito.<Wishlist>any())).thenReturn(new Wishlist());
//        User user = new User();
//        Wishlist actualCreateWishlistResult = wishlistService.createWishlist(user);
//        assertSame(user, actualCreateWishlistResult.getUser());
//        assertTrue(actualCreateWishlistResult.getStatus());
//        verify(wishlistRepository).save(Mockito.<Wishlist>any());
//    }
//}
//
