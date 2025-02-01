package com.ark.security.controller.user;

import com.ark.security.dto.ApiResponse;
import com.ark.security.dto.request.ReviewRequest;
import com.ark.security.dto.response.ReviewResponse;
import com.ark.security.models.statistic.ReviewRating;
import com.ark.security.service.StatisticService;
import com.ark.security.service.product.ReviewService;
import com.ark.security.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewTestService;
    private final UserService userTestService;
    private final StatisticService statisticService;

    @GetMapping
    public ApiResponse<List<ReviewResponse>> getAllReview(){
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewTestService.getAllReview())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ReviewResponse> getReviewById(@PathVariable int id){
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewTestService.getReviewById(id))
                .build();
    }

    @GetMapping("/product/{id}")
    public ApiResponse<List<ReviewResponse>> getReviewByProductId(@PathVariable int id){
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewTestService.getReviewsByProductId(id))
                .build();
    }

    @GetMapping("/product/{id}/rating")
    public ApiResponse<List<ReviewRating>> getReviewRating(@PathVariable int id){
        return ApiResponse.<List<ReviewRating>>builder()
                .result(statisticService.getReviewRating(id))
                .build();
    }

    @GetMapping("/parent/{id}")
    public ApiResponse<List<ReviewResponse>> getReviewByParentId(@PathVariable int id){
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewTestService.getReviewByParentId(id))
                .build();
    }

    @GetMapping("/count/product/{id}")
    public ApiResponse<Integer> countReviewByProductId(@PathVariable int id){
        return ApiResponse.<Integer>builder()
                .result(reviewTestService.countReviewByProductId(id))
                .build();
    }

    @GetMapping("/check/user/{userId}/review/{productId}")
    public ApiResponse<Boolean> isUserReviewed(@PathVariable int userId, @PathVariable int productId){
        return ApiResponse.<Boolean>builder()
                .result(reviewTestService.isReviewExistByUser(userId, productId))
                .build();
    }

    @GetMapping("/check/user/{id}/bought/{productId}")
    public ApiResponse<Boolean> isUserBought(@PathVariable int id, @PathVariable int productId){
        return ApiResponse.<Boolean>builder()
                .result(userTestService.hasUserBoughtProduct(id, productId))
                .build();
    }

    @PostMapping
    public ApiResponse<ReviewResponse> createReview(@RequestBody @Valid ReviewRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .message("Create review successfully")
                .result(reviewTestService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ReviewResponse> updateReview(@PathVariable int id, @RequestBody @Valid ReviewRequest request){
        return ApiResponse.<ReviewResponse>builder()
                .message("Update review successfully")
                .result(reviewTestService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String > deleteReview(@PathVariable int id){
        reviewTestService.delete(id);
        return ApiResponse.<String>builder()
                .result("Delete review successfully")
                .build();
    }

}
