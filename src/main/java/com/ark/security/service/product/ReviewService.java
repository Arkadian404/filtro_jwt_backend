package com.ark.security.service.product;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.product.Review;
import com.ark.security.repository.product.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final String REVIEW_NOT_FOUND = "Review not found";
    private final String REVIEW_EMPTY = "Review is empty";

    public List<Review> getAllReview(){
        List<Review> reviews = reviewRepository.findAll();
        if(reviews.isEmpty()){
            throw new NotFoundException(REVIEW_EMPTY);
        }
        return reviews;
    }

    public Review getReviewById(Integer id){
        return reviewRepository.findById(id).orElseThrow(()-> new NotFoundException(REVIEW_NOT_FOUND));
    }

    public List<Review> getReviewByParentId(Integer id){
        return reviewRepository.findByParentId(id).orElseThrow(()-> new NotFoundException(REVIEW_NOT_FOUND));
    }

    public List<Review> getReviewByProductId(Integer id){
        return reviewRepository.findByProductId(id).orElseThrow(()-> new NotFoundException(REVIEW_NOT_FOUND));
    }

    public void create(Review review){
        Integer userId =  review.getUser().getId();
        Integer parentId = review.getParentId();
        Integer productId = review.getProduct().getId();
        if(existsByUserId(userId, productId) && parentId == null){
            throw new NotFoundException("Bạn đã đánh giá sản phẩm này rồi!");
        }else {
            review.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(review);
            Double avgRating = getAvgRatingByProductId(productId);
            productService.updateProductRating(productId, avgRating);
        }
    }

    public Double getAvgRatingByProductId(Integer id){
        return reviewRepository.avgRatingByProductId(id);
    }

    public Integer countReviewByProductId(Integer id){
        System.out.println(reviewRepository.countReviewByProductId(id));
        return reviewRepository.countReviewByProductId(id);
    }

    public void update(int id, Review review){
        Review oldReview = getReviewById(id);
        int productId = oldReview.getProduct().getId();
        review.setCreatedAt(LocalDateTime.now());
        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());
        reviewRepository.save(oldReview);

        Double avgRating = getAvgRatingByProductId(productId);
        productService.updateProductRating(productId, avgRating);
    }

    public void delete(int id){
        if(!reviewRepository.existsById(id)){
            throw new NotFoundException(REVIEW_NOT_FOUND);
        }

        Review review = getReviewById(id);
        int productId = review.getProduct().getId();
        reviewRepository.deleteById(id);
        Double avgRating = getAvgRatingByProductId(productId);
        productService.updateProductRating(productId, avgRating);
    }

    public boolean existsByUserId(int userId, int productId){
        return reviewRepository.existsByUserIdAndProductId(userId, productId);
    }


}
