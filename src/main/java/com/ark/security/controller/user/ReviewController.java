package com.ark.security.controller.user;

import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.product.Review;
import com.ark.security.service.product.ReviewService;
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

    @PreAuthorize("hasAnyRole('USER', 'EMPLOYEE', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody Review review){
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
    public ResponseEntity<?> updateReview(@PathVariable int id, @RequestBody Review review){
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
