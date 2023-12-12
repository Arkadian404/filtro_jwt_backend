package com.ark.security.service;

import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.product.Product;
import com.ark.security.models.product.Review;
import com.ark.security.models.statistic.*;
import com.ark.security.models.user.User;
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

    @Override
    public Revenue getRevenue(Integer month, Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Revenue>  cq = cb.createQuery(Revenue.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                Revenue.class,
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.sum(root.get("total")).alias("revenue")

        ))
                .where(cb.and(cb.equal(cb.function("MONTH", Integer.class, root.get("orderDate")), month),
                        cb.equal(cb.function("YEAR", Integer.class, root.get("orderDate")), year),
                        cb.equal(root.get("status"), OrderStatus.CONFIRMED)))
                .groupBy(cb.function("MONTH", Integer.class, root.get("orderDate")),
                        cb.function("YEAR", Integer.class, root.get("orderDate")));
        TypedQuery<Revenue> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new Revenue(month, year, 0L));
    }

    @Override
    public OrderStatistic getOrderStatistic(Integer month, Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatistic>  cq = cb.createQuery(OrderStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderStatistic.class,
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.count(root.get("orderCode")).alias("count")

        ))
                .where(cb.and(cb.equal(cb.function("MONTH", Integer.class, root.get("orderDate")), month),
                        cb.equal(cb.function("YEAR", Integer.class, root.get("orderDate")), year)))
                .groupBy(cb.function("MONTH", Integer.class, root.get("orderDate")),
                        cb.function("YEAR", Integer.class, root.get("orderDate")));
        TypedQuery<OrderStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new OrderStatistic(month, year, 0L));
    }

    @Override
    public UserStatistic getUserStatistic(Integer month, Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserStatistic>  cq = cb.createQuery(UserStatistic.class);
        Root<User> root = cq.from(User.class);
        cq.select(cb.construct(
                UserStatistic.class,
                cb.function("MONTH", Integer.class, root.get("createdDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("createdDate")).alias("year"),
                cb.count(root.get("username")).alias("count")

        ))
                .where(cb.and(cb.equal(cb.function("MONTH", Integer.class, root.get("createdDate")), month),
                        cb.equal(cb.function("YEAR", Integer.class, root.get("createdDate")), year)))
                .groupBy(cb.function("MONTH", Integer.class, root.get("createdDate")),
                        cb.function("YEAR", Integer.class, root.get("createdDate")));
        TypedQuery<UserStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new UserStatistic(month, year, 0L));
    }

    @Override
    public ProductStatistic getProductStatistic(Integer month, Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductStatistic>  cq = cb.createQuery(ProductStatistic.class);
        Root<Product> root = cq.from(Product.class);
        cq.select(cb.construct(
                ProductStatistic.class,
                cb.function("MONTH", Integer.class, root.get("createdAt")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("createdAt")).alias("year"),
                cb.count(root.get("name")).alias("count")

        ))
                .where(cb.and(cb.equal(cb.function("MONTH", Integer.class, root.get("createdAt")), month),
                        cb.equal(cb.function("YEAR", Integer.class, root.get("createdAt")), year)))
                .groupBy(cb.function("MONTH", Integer.class, root.get("createdAt")),
                        cb.function("YEAR", Integer.class, root.get("createdAt")));
        TypedQuery<ProductStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new ProductStatistic(month, year, 0L));
    }

}
