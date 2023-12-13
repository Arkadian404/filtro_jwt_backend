package com.ark.security.models.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Revenue {
    private Integer day;
    private Integer month;
    private Integer year;
    private Number revenue;
}
