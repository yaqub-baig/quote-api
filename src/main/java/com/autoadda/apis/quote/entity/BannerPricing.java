package com.autoadda.apis.quote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "banner_pricing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerPricing {
    @Id
    private String id;

    @Column(name = "pricing_code", unique = true, nullable = false)
    private String pricingCode;

    @Column(name = "page_location", nullable = false)
    private String pageLocation;

    @Column(nullable = false)
    private String position;

    @Column(name = "pricing_type", nullable = false)
    private String pricingType;

    @Column(name = "base_price", precision = 15, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "minimum_spend", precision = 15, scale = 2)
    private BigDecimal minimumSpend;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "effective_from", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_until")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime effectiveUntil;

    @Column(name = "is_template")
    private Boolean isTemplate;

    @Column(name = "peak_hour_multiplier", precision = 5, scale = 2)
    private BigDecimal peakHourMultiplier;

    @Column(name = "weekend_multiplier", precision = 5, scale = 2)
    private BigDecimal weekendMultiplier;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;
}
