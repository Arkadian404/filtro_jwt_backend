package com.ark.security.controller.recommender;

import com.ark.security.dto.ProductDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.models.recommender.RecommendationResponse;
import com.ark.security.service.recommender.RecommenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/recommender")
@RequiredArgsConstructor
public class RecommenderController {
    private final RecommenderService recommenderService;


    @GetMapping("/migrate")
    public ResponseEntity<SuccessMessage> migrateOrderDataToTastePreferences(){
        recommenderService.migrateOrderDataToTastePreferences();
        return ResponseEntity.ok(SuccessMessage.builder().message("Migration completed").build());
    }

    @GetMapping("/fastapi/{userId}")
    public ResponseEntity<RecommendationResponse> getFastAPI(@PathVariable int userId){
        return ResponseEntity.ok(recommenderService.fastApi(userId));
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<List<ProductDto>> recommendProductsForUser(@PathVariable int userId){
        return ResponseEntity.ok(recommenderService.recommend(userId));
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> check(@PathVariable int userId){
        return ResponseEntity.ok(recommenderService.check(userId));
    }


//    @GetMapping("/recommend/{userId}")
//    public ResponseEntity<List<ProductDto>> recommendProductsForUser(@PathVariable int userId){
//        try{
//            List<ProductDto> list =  recommenderService.recommendProductsForUser(userId);
//            return ResponseEntity.ok(list);
//        } catch (TasteException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    @GetMapping("/recommend/{userId}")
//    public ResponseEntity<List<ProductDto>> recommendProductsForUser(@PathVariable int userId){
//        try{
//            List<ProductDto> list =  recommenderService.recommendProductsForUser(userId);
//            return ResponseEntity.ok(list);
//        } catch (TasteException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
