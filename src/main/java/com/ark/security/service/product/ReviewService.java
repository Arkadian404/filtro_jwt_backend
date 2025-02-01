package com.ark.security.service.product;

import com.ark.security.dto.request.ReviewRequest;
import com.ark.security.dto.response.ReviewResponse;
import com.ark.security.exception.AppException;
import com.ark.security.exception.ErrorCode;
import com.ark.security.exception.NotFoundException;
import com.ark.security.mapper.ReviewMapper;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.Review;
import com.ark.security.repository.product.ProductRepository;
import com.ark.security.repository.product.ReviewRepository;
import com.ark.security.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;


    public List<ReviewResponse> getAllReview(){
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    public List<ReviewResponse> getReviewsByProductId(int productId){
        return reviewRepository.findByProductId(productId)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND))
                .stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    public ReviewResponse getReviewById(Integer id){
        return reviewMapper.toReviewResponse(reviewRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND)));
    }

    public List<ReviewResponse> getReviewByParentId(Integer id){
        return reviewRepository.findByParentId(id)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND))
                .stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    public List<ReviewResponse> getReviewByProductId(Integer id){
        return reviewRepository.findByProductId(id)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND))
                .stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }

    public Integer countReviewByProductId(Integer id){
        return reviewRepository.countReviewByProductId(id);
    }

    public ReviewResponse create(ReviewRequest request) {
        Integer userId = request.getUserId();
        Integer parentId = request.getParentId();
        Integer productId = request.getProductId();
        if (checkReviewExist(userId, productId, parentId) > 0) {
            throw new AppException(ErrorCode.REVIEW_EXISTED);
        }
        Review review = reviewMapper.toReview(request);
        review.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        review.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        review.setCreatedAt(Instant.now());
        ReviewResponse response = reviewMapper.toReviewResponse(reviewRepository.save(review));
        Double avgRating = getAvgRatingByProductId(productId);
        updateProductRating(productId, avgRating);
        return response;
    }

    public ReviewResponse update(int id, ReviewRequest request){
        Review oldReview = reviewRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        Integer productId = oldReview.getProduct().getId();
        reviewMapper.updateReview(oldReview, request);
        ReviewResponse response = reviewMapper.toReviewResponse(reviewRepository.save(oldReview));
        Double avgRating = getAvgRatingByProductId(productId);
        updateProductRating(productId, avgRating);
        return response;
    }

    public void delete(int id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        Integer productId = review.getProduct().getId();
        reviewRepository.deleteById(id);
        Double avgRating = getAvgRatingByProductId(productId);
        updateProductRating(productId, avgRating);
    }

    private Double getAvgRatingByProductId(Integer id){
        return reviewRepository.avgRatingByProductId(id);
    }

    private Long checkReviewExist(Integer userId, Integer productId, Integer parentId){
        return reviewRepository.isReviewExisting(userId, productId, parentId);
    }

    public boolean isReviewExistByUser(int userId, int productId){
        return reviewRepository.existsByUserIdAndProductIdAndParentIdIsNull(userId, productId);
    }

    private void updateProductRating(int productId, Double avgRating){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setRating(avgRating);
        productRepository.save(product);
    }




}
