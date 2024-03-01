package com.ark.security.service.recommender;

import com.ark.security.dto.ProductDto;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.ark.security.models.product.Product;
import com.ark.security.models.recommender.TastePreferences;
import com.ark.security.models.user.User;
import com.ark.security.repository.OrderRepository;
import com.ark.security.repository.recommender.TastePreferencesRepository;
import com.ark.security.service.product.ProductService;
import com.ark.security.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommenderService {
    private final TastePreferencesRepository tastePreferencesRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RecommenderService.class);
    private final int NEIGHBORHOOD_NUM = 5;


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



    public List<ProductDto> recommendProductsForUser(int userId) throws TasteException {
        List<ProductDto> productDtos = new ArrayList<>();
        migrateOrderDataToTastePreferences();
        try{
            DataModel dataModel = new ReloadFromJDBCDataModel(new MySQLJDBCDataModel(getDataSource()));
            LogLikelihoodSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, dataModel);
            UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            List<RecommendedItem> recommendations = recommender.recommend(userId, 5);
            System.out.println("Recommneds for user " + userId + " are: ");
            System.out.println("**********************************************");
            System.out.println("ItemID\t estimated preference");
            for (RecommendedItem ri : recommendations){
                int itemId = (int) ri.getItemID();
                Product product = productService.getProductById(itemId);
                float estimatePref = ri.getValue();
                System.out.println(itemId + " " + product.getName() + "\t" + estimatePref);
                ProductDto productDto = product.convertToDto();
                productDtos.add(productDto);
            }
            System.out.println("**********************************************");
            long[] userIds = recommender.mostSimilarUserIDs(userId, 5);
            System.out.println("Most similar users to user " + userId + " are: ");
            for (long uid : userIds){
                int id = (int) uid;
                User user = userService.getUserById(id);
                System.out.println(user.getUsername());
            }
        } catch (TasteException e){
            logger.error(String.valueOf(e));
        }
        return productDtos;
    }


    private MysqlDataSource getDataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        dataSource.setDatabaseName("filtro_jwt");
        return dataSource;
    }


}
