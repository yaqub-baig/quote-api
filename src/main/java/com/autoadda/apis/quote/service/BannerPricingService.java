package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.request.BannerPricingRequest;
import com.autoadda.apis.quote.response.BannerPricingResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BannerPricingService {

    List<BannerPricingResponse> getAllPricing();

    List<BannerPricingResponse> getPricingByPageLocation(String pageLocation);

    List<BannerPricingResponse> getActivePricingForSlot(String pageLocation, String position);

    List<BannerPricingResponse> getActiveTemplatePricing();

    Optional<BannerPricingResponse> getPricingById(String id);

    BannerPricingResponse createPricing(BannerPricingRequest request);

    Optional<BannerPricingResponse> updatePricing(String id, BannerPricingRequest request);

    boolean deletePricing(String id);

    BigDecimal calculateEstimate(String pageLocation, String position, String pricingType,
                                  LocalDateTime startDate, LocalDateTime endDate);
}
