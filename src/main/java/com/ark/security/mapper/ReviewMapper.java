package com.ark.security.mapper;

import com.ark.security.dto.request.ReviewRequest;
import com.ark.security.dto.response.ReviewResponse;
import com.ark.security.models.product.Review;
import com.ark.security.service.DTFormatter;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "request.productId", ignore = true)
    @Mapping(target = "request.userId", ignore = true)
    Review toReview(ReviewRequest request);
    void updateReview(@MappingTarget Review review, ReviewRequest request);
    ReviewResponse toReviewResponse(Review review);


    @AfterMapping
    default void setCreated(Review review, @MappingTarget ReviewResponse response){
        DTFormatter formatter = new DTFormatter();
        response.setCreated(formatter.format(review.getCreatedAt()));
    }
}
