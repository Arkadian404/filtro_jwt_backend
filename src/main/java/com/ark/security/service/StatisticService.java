package com.ark.security.service;

import com.ark.security.models.order.Order;
import com.ark.security.models.order.OrderDetail;
import com.ark.security.models.order.OrderStatus;
import com.ark.security.models.product.*;
import com.ark.security.models.statistic.*;
import com.ark.security.models.user.User;
import com.ark.security.repository.StatisticRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        return query.getResultStream().findFirst().orElse(new Revenue(null,month, year, 0L));
    }

    @Override
    public Revenue getRevenueByDate(Integer day, Integer month, Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Revenue>  cq = cb.createQuery(Revenue.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                Revenue.class,
                cb.function("DAY", Integer.class, root.get("orderDate")).alias("day"),
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.sum(root.get("total")).alias("revenue")

        ))
                .where(cb.and(
                        cb.equal(cb.function("DAY", Integer.class, root.get("orderDate")), day),
                        cb.equal(cb.function("MONTH", Integer.class, root.get("orderDate")), month),
                        cb.equal(cb.function("YEAR", Integer.class, root.get("orderDate")), year),
                        cb.equal(root.get("status"), OrderStatus.CONFIRMED)))
                .groupBy(cb.function("DAY", Integer.class, root.get("orderDate")),
                        cb.function("MONTH", Integer.class, root.get("orderDate")),
                        cb.function("YEAR", Integer.class, root.get("orderDate")));
        TypedQuery<Revenue> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new Revenue(day,month, year, 0L));
    }


    private LocalDateTime firstDayOfMonth(){
        LocalDateTime now = LocalDateTime.now();
        return now.withDayOfMonth(1);
    }

    private LocalDateTime firstDayOfLastMonth(){
        LocalDateTime now = LocalDateTime.now();
        return now.minusMonths(1).withDayOfMonth(1);
    }

    private LocalDateTime lastDayOfPreviousMonth(){
        LocalDateTime now = LocalDateTime.now();
        return now.withDayOfMonth(1).minusDays(1);
    }

    @Override
    public List<Revenue> getRevenueByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Revenue> cq = cb.createQuery(Revenue.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                Revenue.class,
                cb.function("DAY", Integer.class, root.get("orderDate")).alias("day"),
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.sum(root.get("total")).alias("revenue")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfMonth()
                        ),
                        cb.equal(root.get("status"), OrderStatus.CONFIRMED)
                )

        ).groupBy(
                cb.function("DAY", Integer.class, root.get("orderDate")),
                cb.function("MONTH", Integer.class, root.get("orderDate")),
                cb.function("YEAR", Integer.class, root.get("orderDate")
        ));
        TypedQuery<Revenue> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<Revenue> getRevenueByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Revenue> cq = cb.createQuery(Revenue.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                Revenue.class,
                cb.function("DAY", Integer.class, root.get("orderDate")).alias("day"),
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.sum(root.get("total")).alias("revenue")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                            root.get("orderDate"),
                            firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                            root.get("orderDate"),
                            lastDayOfPreviousMonth()
                        ),
                        cb.equal(root.get("status"), OrderStatus.CONFIRMED)
                )

        ).groupBy(
                cb.function("DAY", Integer.class, root.get("orderDate")),
                cb.function("MONTH", Integer.class, root.get("orderDate")),
                cb.function("YEAR", Integer.class, root.get("orderDate")
                ));
        TypedQuery<Revenue> query = entityManager.createQuery(cq);
        return query.getResultList();
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
        return query.getResultStream().findFirst().orElse(new OrderStatistic(null,month, year, 0L));
    }

    @Override
    public OrderStatistic getOrderStatisticByDate(Integer day, Integer month, Integer year) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatistic>  cq = cb.createQuery(OrderStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderStatistic.class,
                cb.function("DAY", Integer.class, root.get("orderDate")).alias("day"),
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.count(root.get("orderCode")).alias("count")

        ))
                .where(cb.and(
                        cb.equal(cb.function("DAY", Integer.class, root.get("orderDate")), day),
                        cb.equal(cb.function("MONTH", Integer.class, root.get("orderDate")), month),
                        cb.equal(cb.function("YEAR", Integer.class, root.get("orderDate")), year)))
                .groupBy(cb.function("DAY", Integer.class, root.get("orderDate")),
                        cb.function("MONTH", Integer.class, root.get("orderDate")),
                        cb.function("YEAR", Integer.class, root.get("orderDate")));
        TypedQuery<OrderStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new OrderStatistic(day,month, year, 0L));
    }

    @Override
    public OrderStatistic getOrderStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatistic> cq = cb.createQuery(OrderStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderStatistic.class,
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.count(root.get("orderCode")).alias("count")
        )).where(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        firstDayOfMonth()
                )
        ).groupBy(
                cb.function("MONTH", Integer.class, root.get("orderDate")),
                cb.function("YEAR", Integer.class, root.get("orderDate")
                ));
        TypedQuery<OrderStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new OrderStatistic(null,null, null, 0L));
    }

    @Override
    public OrderStatistic getOrderStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatistic> cq = cb.createQuery(OrderStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderStatistic.class,
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.count(root.get("orderCode")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        )
                )

        ).groupBy(
                cb.function("MONTH", Integer.class, root.get("orderDate")),
                cb.function("YEAR", Integer.class, root.get("orderDate")
                ));
        TypedQuery<OrderStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new OrderStatistic(null,null, null, 0L));
    }

    @Override
    public OrderStatistic getFailedOrderStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatistic> cq = cb.createQuery(OrderStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderStatistic.class,
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.count(root.get("orderCode")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfMonth()
                        ),
                        cb.or(
                                cb.equal(root.get("status"), OrderStatus.CANCELED),
                                cb.equal(root.get("status"), OrderStatus.FAILED)
                        )
                )

        ).groupBy(
                cb.function("MONTH", Integer.class, root.get("orderDate")),
                cb.function("YEAR", Integer.class, root.get("orderDate")
                ));
        TypedQuery<OrderStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new OrderStatistic(null,null, null, 0L));
    }

    @Override
    public OrderStatistic getFailedOrderStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderStatistic> cq = cb.createQuery(OrderStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderStatistic.class,
                cb.function("MONTH", Integer.class, root.get("orderDate")).alias("month"),
                cb.function("YEAR", Integer.class, root.get("orderDate")).alias("year"),
                cb.count(root.get("orderCode")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        ),
                        cb.or(
                                cb.equal(root.get("status"), OrderStatus.CANCELED),
                                cb.equal(root.get("status"), OrderStatus.FAILED)
                        )
                )

        ).groupBy(
                cb.function("MONTH", Integer.class, root.get("orderDate")),
                cb.function("YEAR", Integer.class, root.get("orderDate")
                ));
        TypedQuery<OrderStatistic> query = entityManager.createQuery(cq);
        return query.getResultStream().findFirst().orElse(new OrderStatistic(null,null, null, 0L));
    }

    @Override
    public List<OrderLocationStatistic> getOrderLocationStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderLocationStatistic> cq = cb.createQuery(OrderLocationStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderLocationStatistic.class,
                root.get("province").alias("province"),
                cb.count(root.get("orderCode")).alias("count")
        )).where(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        firstDayOfMonth()
                )
        ).groupBy(
                root.get("province")
        );
        TypedQuery<OrderLocationStatistic> query = entityManager.createQuery(cq).setMaxResults(6);
        return query.getResultList();
    }

    @Override
    public List<OrderLocationStatistic> getOrderLocationStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderLocationStatistic> cq = cb.createQuery(OrderLocationStatistic.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(cb.construct(
                OrderLocationStatistic.class,
                root.get("province").alias("province"),
                cb.count(root.get("orderCode")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        )
                )

        ).groupBy(
                root.get("province")
        );
        TypedQuery<OrderLocationStatistic> query = entityManager.createQuery(cq).setMaxResults(6);
        return query.getResultList();
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
        return query.getResultStream().findFirst().orElse(new UserStatistic(null,month, year, 0L));
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
        return query.getResultStream().findFirst().orElse(new ProductStatistic(null,month, year, 0L));
    }

    @Override
    public List<CategoryStatistic> getCategoryStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryStatistic> cq = cb.createQuery(CategoryStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, Category> c = p.join("category");
        cq.select(cb.construct(
                CategoryStatistic.class,
                c.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        firstDayOfMonth()
                )
        ).groupBy(
                c.get("name")
        );
        TypedQuery<CategoryStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<CategoryStatistic> getCategoryStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategoryStatistic> cq = cb.createQuery(CategoryStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, Category> c = p.join("category");
        cq.select(cb.construct(
                CategoryStatistic.class,
                c.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        )
                )

        ).groupBy(
                c.get("name")
        );
        TypedQuery<CategoryStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<FlavorStatistic> getFlavorStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FlavorStatistic> cq = cb.createQuery(FlavorStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, Flavor> f = p.join("flavor");
        cq.select(cb.construct(
                FlavorStatistic.class,
                f.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        firstDayOfMonth()
                )
        ).groupBy(
                f.get("name")
        );
        TypedQuery<FlavorStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<FlavorStatistic> getFlavorStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FlavorStatistic> cq = cb.createQuery(FlavorStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, Flavor> f = p.join("flavor");
        cq.select(cb.construct(
                FlavorStatistic.class,
                f.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        )
                )

        ).groupBy(
                f.get("name")
        );
        TypedQuery<FlavorStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<BrandStatistic> getBrandStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BrandStatistic> cq = cb.createQuery(BrandStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, Brand> b = p.join("brand");
        cq.select(cb.construct(
                BrandStatistic.class,
                b.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        firstDayOfMonth()
                )
        ).groupBy(
                b.get("name")
        );
        TypedQuery<BrandStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<BrandStatistic> getBrandStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BrandStatistic> cq = cb.createQuery(BrandStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, Brand> b = p.join("brand");
        cq.select(cb.construct(
                BrandStatistic.class,
                b.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        )
                )

        ).groupBy(
                b.get("name")
        );
        TypedQuery<BrandStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<OriginStatistic> getOriginStatisticByCurrentMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OriginStatistic> cq = cb.createQuery(OriginStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, ProductOrigin> po = p.join("origin");
        cq.select(cb.construct(
                OriginStatistic.class,
                po.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.greaterThanOrEqualTo(
                        root.get("orderDate"),
                        firstDayOfMonth()
                )
        ).groupBy(
                po.get("name")
        ).orderBy(
                cb.desc(cb.count(root.get("id")))
        );
        TypedQuery<OriginStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<OriginStatistic> getOriginStatisticByLastMonth() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OriginStatistic> cq = cb.createQuery(OriginStatistic.class);
        Root<OrderDetail> root = cq.from(OrderDetail.class);
        Join<OrderDetail, ProductDetail> pd = root.join("productDetail");
        Join<ProductDetail, Product> p = pd.join("product");
        Join<Product, ProductOrigin> po = p.join("origin");
        cq.select(cb.construct(
                OriginStatistic.class,
                po.get("name").alias("name"),
                cb.count(root.get("id")).alias("count")
        )).where(
                cb.and(
                        cb.greaterThanOrEqualTo(
                                root.get("orderDate"),
                                firstDayOfLastMonth()
                        ),
                        cb.lessThanOrEqualTo(
                                root.get("orderDate"),
                                lastDayOfPreviousMonth()
                        )
                )

        ).groupBy(
                po.get("name")
        ).orderBy(
                cb.desc(cb.count(root.get("id")))
        );
        TypedQuery<OriginStatistic> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

}
