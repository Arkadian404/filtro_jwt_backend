package com.ark.security.service.recommender;

import com.ark.security.dto.response.ProductResponse;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.recommender.RecommendationResponse;
import com.ark.security.service.product.ProductService;
import com.ark.security.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommenderService {
    private final ProductService productService;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(RecommenderService.class);
    @Value("${spring.fast-api.url}")
    private String URL;




    public RecommendationResponse fastApi(int userId){
        System.out.println(URL+"/recommendations/"+userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<RecommendationResponse> response = restTemplate.exchange(URL+"/recommendations/"+userId, HttpMethod.GET, entity, RecommendationResponse.class);
        ResponseEntity<RecommendationResponse> response = restTemplate.getForEntity(URL+"/recommendations/"+userId, RecommendationResponse.class);
        return response.getBody();
    }

    public List<ProductResponse> recommend(int userId){
        if(!userService.checkUserExistById(userId)){
            throw new NotFoundException("Không tìm thấy user với ID: " + userId);
        }
        if(userService.hasUserEverBoughtAProduct(userId)){
            RecommendationResponse response = fastApi(userId);
            List<ProductResponse> products= new ArrayList<>();
            for(int id : response.getRecommendations()){
                ProductResponse product = productService.getProductById(id);
                products.add(product);
            }
            return products;
        }
        return List.of();
    }

    public boolean check(int userId){
        return userService.hasUserEverBoughtAProduct(userId);
    }
}

