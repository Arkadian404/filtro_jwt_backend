package com.ark.security.models.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatistic {
    private Integer month;
    private Integer year;
    private Long count;
}
