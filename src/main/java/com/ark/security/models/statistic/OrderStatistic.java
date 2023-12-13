package com.ark.security.models.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatistic {
    private Integer day;
    private Integer month;
    private Integer year;
    private Long count;
}
