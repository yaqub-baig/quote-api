package com.autoadda.apis.quote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "banner_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerAnalytics {
    @Id
    private String id;

    @Column(name = "banner_id", nullable = false)
    private String bannerId;

    @Column(name = "analytics_date", nullable = false)
    private LocalDate analyticsDate;

    @Column(nullable = false)
    private Long impressions;

    @Column(nullable = false)
    private Long clicks;

    @Column(name = "unique_impressions")
    private Long uniqueImpressions;

    @Column(name = "unique_clicks")
    private Long uniqueClicks;

    @Column(precision = 10, scale = 4)
    private BigDecimal ctr;

    @Column(name = "cost_incurred", precision = 15, scale = 2)
    private BigDecimal costIncurred;
}
