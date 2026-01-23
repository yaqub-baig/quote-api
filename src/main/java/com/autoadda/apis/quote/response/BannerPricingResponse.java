package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerPricingResponse {
    private String id;
    private String pricingCode;
    private String pageLocation;
    private String position;
    private String pricingType;
    private BigDecimal basePrice;
    private String currency;
    private BigDecimal minimumSpend;
    private BigDecimal discountPercentage;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveUntil;
    private Boolean isTemplate;
    private BigDecimal peakHourMultiplier;
    private BigDecimal weekendMultiplier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
