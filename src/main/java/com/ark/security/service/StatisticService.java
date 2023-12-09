package com.ark.security.service;

import com.ark.security.models.product.Review;
import com.ark.security.models.statistic.ReviewRating;
import com.ark.security.repository.StatisticRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService implements StatisticRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReviewRating> getReviewRating(Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReviewRating>  cq = cb.createQuery(ReviewRating.class);
        Root<Review> root = cq.from(Review.class);
        cq.select(cb.construct(ReviewRating.class,
                root.get("rating").alias("rating"),cb.count(root.<String>get("user").get("id")).alias("count")))
                .where(cb.and(cb.equal(root.get("product").get("id"), id), cb.isNull(root.get("parentId"))))
                .groupBy(root.get("rating"));
        TypedQuery<ReviewRating> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
