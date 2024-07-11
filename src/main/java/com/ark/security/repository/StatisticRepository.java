package com.ark.security.repository;
import com.ark.security.models.statistic.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository {
    List<ReviewRating> getReviewRating(Integer id);
    Revenue getRevenue(Integer month, Integer year);
    List<Revenue> getRevenueByDate(LocalDate startDate, LocalDate endDate);
    List<Revenue> getRevenueByCurrentMonth();
    List<Revenue> getRevenueByChosenMonth(int month);
    List<Revenue> getRevenueByLastMonth();
    OrderStatistic getOrderStatistic(Integer month, Integer year);
    List<OrderStatistic> getOrderStatisticByDate(LocalDate startDate, LocalDate endDate);
    OrderStatistic getOrderStatisticByCurrentMonth();
    List<OrderStatistic> getOrderStatisticByChosenMonth(int month);
    OrderStatistic getOrderStatisticByLastMonth();
    List<OrderStatistic> getFailedOrderStatisticByDate(LocalDate startDate, LocalDate endDate);
    OrderStatistic getFailedOrderStatisticByCurrentMonth();
    List<OrderStatistic> getFailedOrderStatisticByChosenMonth(int month);
    OrderStatistic getFailedOrderStatisticByLastMonth();
    List<OrderLocationStatistic> getOrderLocationStatisticByCurrentMonth();
    List<OrderLocationStatistic> getOrderLocationStatisticByLastMonth();
    List<OrderLocationStatistic> getOrderLocationStatisticByChosenMonth(int month);
    UserStatistic getUserStatistic(Integer month, Integer year);
    ProductStatistic getProductStatistic(Integer month, Integer year);

    List<CategoryStatistic> getCategoryStatisticByCurrentMonth();
    List<CategoryStatistic> getCategoryStatisticByLastMonth();
    List<CategoryStatistic> getCategoryStatisticByChosenMonth(int month);

    List<FlavorStatistic> getFlavorStatisticByCurrentMonth();
    List<FlavorStatistic> getFlavorStatisticByLastMonth();
    List<FlavorStatistic> getFlavorStatisticByChosenMonth(int month);

    List<BrandStatistic> getBrandStatisticByCurrentMonth();
    List<BrandStatistic> getBrandStatisticByLastMonth();
    List<BrandStatistic> getBrandStatisticByChosenMonth(int month);

    List<OriginStatistic> getOriginStatisticByCurrentMonth();
    List<OriginStatistic> getOriginStatisticByLastMonth();
    List<OriginStatistic> getOriginStatisticByChosenMonth(int month);

}
