package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerAnalyticsResponse {
    private String id;
    private String bannerId;
    private LocalDate analyticsDate;
    private Long impressions;
    private Long clicks;
    private Long uniqueImpressions;
    private Long uniqueClicks;
    private BigDecimal ctr;
    private BigDecimal costIncurred;
}
