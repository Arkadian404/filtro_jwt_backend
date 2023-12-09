package com.ark.security.models.statistic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRating {
    private Integer rating;
    private Long count;
}
