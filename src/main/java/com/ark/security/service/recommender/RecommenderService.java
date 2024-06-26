package com.ark.security.service.recommender;

import com.ark.security.dto.ProductDto;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.recommender.RecommendationResponse;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.ark.security.models.product.Product;
import com.ark.security.models.recommender.TastePreferences;
import com.ark.security.repository.OrderRepository;
import com.ark.security.repository.recommender.TastePreferencesRepository;
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
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommenderService {
    private final TastePreferencesRepository tastePreferencesRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(RecommenderService.class);
    private final int NEIGHBORHOOD_NUM = 5;
    @Value("${spring.fast-api.url}")
    private String URL;


    public void migrateOrderDataToTastePreferences(){
        List<Object[]> orderMigrationData = orderRepository.findOrderMigrationData();
        for(Object[] object : orderMigrationData){
            Long userId =  Long.valueOf(object[0].toString());
            Long itemId = Long.valueOf(object[1].toString());
            Double preference = Double.valueOf(object[2].toString());

            TastePreferences tastePreferences = new TastePreferences();
            tastePreferences.setUserId(userId);
            tastePreferences.setItemId(itemId);
            tastePreferences.setPreference(preference);
            tastePreferencesRepository.save(tastePreferences);
        }
    }



    boolean checkIfUserExistsInTastePreferences(Long userId){
        return tastePreferencesRepository.existsByUserId(userId);
    }

//    public List<ProductDto> recommendProductsForUser(int userId) throws TasteException {
//        List<ProductDto> productDtos = new ArrayList<>();
//        migrateOrderDataToTastePreferences();
//        if (checkIfUserExistsInTastePreferences((long) userId)) {
//            try {
//                Recommender recommender = getRecommender();
//                List<RecommendedItem> recommendations = recommender.recommend(userId, 5);
//                System.out.println("Recommneds for user " + userId + " are: ");
//                System.out.println("**********************************************");
//                System.out.println("ItemID\t estimated preference");
//                for (RecommendedItem ri : recommendations) {
//                    int itemId = (int) ri.getItemID();
//                    Product product = productService.getProductById(itemId);
//                    float estimatePref = recommender.estimatePreference(userId, itemId);
//                    System.out.println(itemId + " " + product.getName() + "\t" + estimatePref);
//                    ProductDto productDto = product.convertToDto();
//                    productDtos.add(productDto);
//                }
////                    System.out.println("**********************************************");
////                    long[] userIds = recommender.mostSimilarUserIDs(userId, 5);
////                    System.out.println("Most similar users to user " + userId + " are: ");
////                    for (long uid : userIds){
////                        int id = (int) uid;
////                        User user = userService.getUserById(id);
////                        System.out.println(user.getUsername());
////                    }
//            } catch (TasteException e) {
//                logger.error(String.valueOf(e));
//            }
//        }
//        return productDtos;
//
//    }

//    private Recommender getRecommender() throws TasteException {
//        DataModel dataModel = new ReloadFromJDBCDataModel(new MySQLJDBCDataModel(getDataSource()));
//        UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModel, Weighting.WEIGHTED);
//        UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, dataModel);
//        UserBasedRecommender userBasedRecommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
//        return new CachingRecommender(userBasedRecommender);
//    }


//    public void recommendProductsForUser(int userId) throws TasteException {
//        try{
//            DataModel dataModel = new ReloadFromJDBCDataModel(new MySQLJDBCDataModel(getDataSource()));
//            RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
//            RecommenderBuilder recommenderBuilder = this;
//            double score = evaluator.evaluate(recommenderBuilder, null, dataModel, 0.9, 1.0);
//            System.out.println("Score: " + score);
////            RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
////            RecommenderBuilder recommenderBuilder = this;
////            IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dataModel, null, 10, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
////            System.out.println("Precision: " + stats.getPrecision());
////            System.out.println("Recall: " + stats.getRecall());
//        }catch (TasteException e){
//            logger.error(String.valueOf(e));
//        }
//
//    }


    private MysqlDataSource getDataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setDatabaseName("filtro_jwt");
        return dataSource;
    }


    public RecommendationResponse fastApi(int userId){
        System.out.println(URL+"/recommendations/"+userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<RecommendationResponse> response = restTemplate.exchange(URL+"/recommendations/"+userId, HttpMethod.GET, entity, RecommendationResponse.class);
        ResponseEntity<RecommendationResponse> response = restTemplate.getForEntity(URL+"/recommendations/"+userId, RecommendationResponse.class);
        return response.getBody();
    }

    public List<ProductDto> recommend(int userId){
        if(!userService.checkUserExistById(userId)){
            throw new NotFoundException("Không tìm thấy user với ID: " + userId);
        }
        if(userService.hasUserEverBoughtAProduct(userId)){
            RecommendationResponse response = fastApi(userId);
            List<ProductDto> productDtos = new ArrayList<>();
            for(int id : response.getRecommendations()){
                Product product = productService.getProductById(id);
                ProductDto productDto = product.convertToDto();
                productDtos.add(productDto);
            }
            return productDtos;
        }
        return List.of();
    }

    public boolean check(int userId){
        return userService.hasUserEverBoughtAProduct(userId);
    }
}

