package com.ark.security.controller.recommender;

import com.ark.security.models.recommender.RecommendationResponse;
import com.ark.security.service.recommender.RecommenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/recommender")
@RequiredArgsConstructor
public class RecommenderController {
    private final RecommenderService recommenderService;


    @GetMapping("/fastapi/{userId}")
    public ResponseEntity<RecommendationResponse> getFastAPI(@PathVariable int userId){
        return ResponseEntity.ok(recommenderService.fastApi(userId));
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> check(@PathVariable int userId){
        return ResponseEntity.ok(recommenderService.check(userId));
    }


}
