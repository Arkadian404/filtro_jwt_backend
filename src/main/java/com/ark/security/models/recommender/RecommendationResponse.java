package com.ark.security.models.recommender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {
    private List<Integer> recommendations;
    private String detail;
}
