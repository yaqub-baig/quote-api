package com.autoadda.apis.quote.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerPricingRequest {
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
}
