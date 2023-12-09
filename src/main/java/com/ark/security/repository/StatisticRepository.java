package com.ark.security.repository;

import com.ark.security.models.statistic.ReviewRating;

import java.util.List;

public interface StatisticRepository {
    List<ReviewRating> getReviewRating(Integer id);
}
