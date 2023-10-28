package com.ark.security.service;

import com.ark.security.exception.NotFoundException;
import com.ark.security.models.Review;
import com.ark.security.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
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
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public void update(int id, Review review){
        Review oldReview = getReviewById(id);
        review.setCreatedAt(LocalDateTime.now());
        oldReview.setComment(review.getComment());
        reviewRepository.save(oldReview);
    }

    public void delete(int id){
        if(!reviewRepository.existsById(id)){
            throw new NotFoundException(REVIEW_NOT_FOUND);
        }
        reviewRepository.deleteById(id);
    }

}
