package com.ark.security.repository;
import com.ark.security.models.statistic.*;

import java.util.List;

public interface StatisticRepository {
    List<ReviewRating> getReviewRating(Integer id);
    Revenue getRevenue(Integer month, Integer year);
    Revenue getRevenueByDate(Integer day, Integer month, Integer year);
    List<Revenue> getRevenueByCurrentMonth();
    List<Revenue> getRevenueByLastMonth();
    OrderStatistic getOrderStatistic(Integer month, Integer year);
    OrderStatistic getOrderStatisticByDate(Integer day, Integer month, Integer year);
    OrderStatistic getOrderStatisticByCurrentMonth();
    OrderStatistic getOrderStatisticByLastMonth();
    OrderStatistic getFailedOrderStatisticByCurrentMonth();
    OrderStatistic getFailedOrderStatisticByLastMonth();
    List<OrderLocationStatistic> getOrderLocationStatisticByCurrentMonth();
    List<OrderLocationStatistic> getOrderLocationStatisticByLastMonth();
    UserStatistic getUserStatistic(Integer month, Integer year);
    ProductStatistic getProductStatistic(Integer month, Integer year);

    List<CategoryStatistic> getCategoryStatisticByCurrentMonth();
    List<CategoryStatistic> getCategoryStatisticByLastMonth();

    List<FlavorStatistic> getFlavorStatisticByCurrentMonth();
    List<FlavorStatistic> getFlavorStatisticByLastMonth();

    List<BrandStatistic> getBrandStatisticByCurrentMonth();
    List<BrandStatistic> getBrandStatisticByLastMonth();

    List<OriginStatistic> getOriginStatisticByCurrentMonth();
    List<OriginStatistic> getOriginStatisticByLastMonth();

}
