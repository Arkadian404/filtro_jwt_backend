package com.ark.security.controller.recommender;

import com.ark.security.dto.ProductDto;
import com.ark.security.exception.SuccessMessage;
import com.ark.security.service.recommender.RecommenderService;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
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


//    @GetMapping("/recommend/{userId}")
//    public ResponseEntity<List<ProductDto>> recommendProductsForUser(@PathVariable int userId){
//        try{
//            List<ProductDto> list =  recommenderService.recommendProductsForUser(userId);
//            return ResponseEntity.ok(list);
//        } catch (TasteException e) {
//            throw new RuntimeException(e);
//        }
//    }


    @GetMapping("/recommend/{userId}")
    public ResponseEntity<List<ProductDto>> recommendProductsForUser(@PathVariable int userId){
        try{
            List<ProductDto> list =  recommenderService.recommendProductsForUser(userId);
            return ResponseEntity.ok(list);
        } catch (TasteException e) {
            throw new RuntimeException(e);
        }
    }

}
