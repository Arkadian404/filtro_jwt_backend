package com.ark.security.repository;

import com.ark.security.models.statistic.*;

import java.util.List;

public interface StatisticRepository {
    List<ReviewRating> getReviewRating(Integer id);
    Revenue getRevenue(Integer month, Integer year);
    OrderStatistic getOrderStatistic(Integer month, Integer year);
    UserStatistic getUserStatistic(Integer month, Integer year);
    ProductStatistic getProductStatistic(Integer month, Integer year);
}
