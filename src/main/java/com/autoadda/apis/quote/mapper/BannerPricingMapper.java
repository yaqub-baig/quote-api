package com.autoadda.apis.quote.mapper;

import com.autoadda.apis.quote.entity.BannerPricing;
import com.autoadda.apis.quote.request.BannerPricingRequest;
import com.autoadda.apis.quote.response.BannerPricingResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class BannerPricingMapper {

    public BannerPricing toEntity(BannerPricingRequest request) {
        BannerPricing pricing = new BannerPricing();
        pricing.setId(UUID.randomUUID().toString());
        pricing.setPageLocation(request.getPageLocation());
        pricing.setPosition(request.getPosition());
        pricing.setPricingType(request.getPricingType());
        pricing.setBasePrice(request.getBasePrice());
        pricing.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        pricing.setMinimumSpend(request.getMinimumSpend());
        pricing.setDiscountPercentage(request.getDiscountPercentage());
        pricing.setEffectiveFrom(request.getEffectiveFrom() != null ? request.getEffectiveFrom() : LocalDateTime.now());
        pricing.setEffectiveUntil(request.getEffectiveUntil());
        pricing.setIsTemplate(request.getIsTemplate() != null ? request.getIsTemplate() : false);
        pricing.setPeakHourMultiplier(request.getPeakHourMultiplier());
        pricing.setWeekendMultiplier(request.getWeekendMultiplier());
        pricing.setCreatedAt(LocalDateTime.now());
        return pricing;
    }

    public BannerPricingResponse toResponse(BannerPricing pricing) {
        BannerPricingResponse response = new BannerPricingResponse();
        response.setId(pricing.getId());
        response.setPricingCode(pricing.getPricingCode());
        response.setPageLocation(pricing.getPageLocation());
        response.setPosition(pricing.getPosition());
        response.setPricingType(pricing.getPricingType());
        response.setBasePrice(pricing.getBasePrice());
        response.setCurrency(pricing.getCurrency());
        response.setMinimumSpend(pricing.getMinimumSpend());
        response.setDiscountPercentage(pricing.getDiscountPercentage());
        response.setEffectiveFrom(pricing.getEffectiveFrom());
        response.setEffectiveUntil(pricing.getEffectiveUntil());
        response.setIsTemplate(pricing.getIsTemplate());
        response.setPeakHourMultiplier(pricing.getPeakHourMultiplier());
        response.setWeekendMultiplier(pricing.getWeekendMultiplier());
        response.setCreatedAt(pricing.getCreatedAt());
        response.setUpdatedAt(pricing.getUpdatedAt());
        return response;
    }

    public void updateEntity(BannerPricing existing, BannerPricingRequest request) {
        Optional.ofNullable(request.getPageLocation()).ifPresent(existing::setPageLocation);
        Optional.ofNullable(request.getPosition()).ifPresent(existing::setPosition);
        Optional.ofNullable(request.getPricingType()).ifPresent(existing::setPricingType);
        Optional.ofNullable(request.getBasePrice()).ifPresent(existing::setBasePrice);
        Optional.ofNullable(request.getCurrency()).ifPresent(existing::setCurrency);
        Optional.ofNullable(request.getMinimumSpend()).ifPresent(existing::setMinimumSpend);
        Optional.ofNullable(request.getDiscountPercentage()).ifPresent(existing::setDiscountPercentage);
        Optional.ofNullable(request.getEffectiveFrom()).ifPresent(existing::setEffectiveFrom);
        Optional.ofNullable(request.getEffectiveUntil()).ifPresent(existing::setEffectiveUntil);
        Optional.ofNullable(request.getIsTemplate()).ifPresent(existing::setIsTemplate);
        Optional.ofNullable(request.getPeakHourMultiplier()).ifPresent(existing::setPeakHourMultiplier);
        Optional.ofNullable(request.getWeekendMultiplier()).ifPresent(existing::setWeekendMultiplier);
        existing.setUpdatedAt(LocalDateTime.now());
    }
}
