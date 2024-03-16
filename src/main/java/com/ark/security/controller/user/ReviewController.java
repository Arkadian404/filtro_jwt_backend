package com.ark.security.controller.user;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.Review;
import com.ark.security.service.StatisticService;
import com.ark.security.service.product.ReviewService;
import com.ark.security.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/user/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final StatisticService statisticService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllReview(){
        return ResponseEntity.ok(reviewService.getAllReview());
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable int id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/parent/{id}")
    public ResponseEntity<?> getReviewByParentId(@PathVariable int id){
        return ResponseEntity.ok(reviewService.getReviewByParentId(id));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getReviewByProductId(@PathVariable int id){
        return ResponseEntity.ok(reviewService.getReviewByProductId(id));
    }

    @GetMapping("/get/countReviewProduct/{id}")
    public ResponseEntity<?> getCountReviewProductId(@PathVariable int id){
        return ResponseEntity.ok(reviewService.countReviewByProductId(id));
    }

    @GetMapping("/check/user/{userId}/review/{productId}")
    public ResponseEntity<?> isUserReviewed(@PathVariable int userId, @PathVariable int productId){
        return ResponseEntity.ok(reviewService.existsByUserId(userId, productId));
    }

    @GetMapping("/get/productReviewRating/{id}")
    public ResponseEntity<?> getReviewRating(@PathVariable Integer id){
        return ResponseEntity.ok(statisticService.getReviewRating(id));
    }

    @GetMapping("/check/user/{id}/{productId}")
    public ResponseEntity<?> checkUserHasBoughtProduct(@PathVariable int id, @PathVariable int productId){
        return ResponseEntity.ok(userService.hasUserBoughtProduct(id, productId));
    }

    @PreAuthorize("hasAnyRole('USER', 'EMPLOYEE', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@Valid @RequestBody Review review){
        reviewService.create(review);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Đánh giá thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PreAuthorize("hasAnyRole('USER', 'EMPLOYEE', 'ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable int id, @Valid @RequestBody Review review){
        reviewService.update(id, review);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

    @PreAuthorize("hasAnyRole('USER', 'EMPLOYEE', 'ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable int id){
        reviewService.delete(id);
        var success = SuccessMessage.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Xóa thành công")
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(success);
    }

}
