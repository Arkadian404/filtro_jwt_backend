package com.ark.security.service.product;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.Review;
import com.ark.security.models.user.User;
import com.ark.security.repository.product.ReviewRepository;
import com.ark.security.repository.user.UserRepository;
import com.ark.security.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReview() {
        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(new Review()));

        assertNotNull(reviewService.getAllReview());

        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetAllReviewEmpty() {
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> reviewService.getAllReview());
    }

    @Test
    void testGetReviewById() {
        Review review = new Review();
        review.setId(1);
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));

        Review result = reviewService.getReviewById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetReviewByIdNotFound() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> reviewService.getReviewById(1));
    }

    @Test
    void testCreate() {
        User user =  new User();
        user.setId(1);
        Product product = new Product();
        product.setId(1);


        Review review = new Review();
        review.setId(1);
        review.setProduct(product);
        review.setUser(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewRepository.avgRatingByProductId(anyInt())).thenReturn(5.0);
        reviewService.create(review);

        verify(reviewRepository, times(1)).save(review);
        verify(productService, times(1)).updateProductRating(anyInt(), anyDouble());
    }

    @Test
    void testUpdate() {
        Review review = new Review();
        review.setId(1);
        Product product = new Product();
        product.setId(1);
        review.setProduct(product);
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        reviewService.update(1, review);

        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testDelete() {
        Product product = new Product();
        product.setId(1);

        Review review = new Review();
        review.setId(1);
        review.setProduct(product);
        when(reviewRepository.existsById(anyInt())).thenReturn(true);
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(reviewRepository.avgRatingByProductId(anyInt())).thenReturn(5.0);

        reviewService.delete(1);

        verify(reviewRepository, times(1)).deleteById(anyInt());
        verify(productService, times(1)).updateProductRating(anyInt(), anyDouble());
    }

    @Test
    void testDeleteNotFound() {
        when(reviewRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> reviewService.delete(1));
    }


}