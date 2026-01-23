package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.entity.BannerPricing;
import com.autoadda.apis.quote.mapper.BannerPricingMapper;
import com.autoadda.apis.quote.repository.BannerPricingRepository;
import com.autoadda.apis.quote.request.BannerPricingRequest;
import com.autoadda.apis.quote.response.BannerPricingResponse;
import com.autoadda.apis.quote.util.PricingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerPricingServiceImpl implements BannerPricingService {

    private final BannerPricingRepository pricingRepository;
    private final BannerPricingMapper pricingMapper;

    @Override
    public List<BannerPricingResponse> getAllPricing() {
        return pricingRepository.findAll()
                .stream()
                .map(pricingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerPricingResponse> getPricingByPageLocation(String pageLocation) {
        return pricingRepository.findByPageLocation(pageLocation)
                .stream()
                .map(pricingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerPricingResponse> getActivePricingForSlot(String pageLocation, String position) {
        return pricingRepository.findActivePricingForSlot(pageLocation, position, LocalDateTime.now())
                .stream()
                .map(pricingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerPricingResponse> getActiveTemplatePricing() {
        return pricingRepository.findActiveTemplatePricing(LocalDateTime.now())
                .stream()
                .map(pricingMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<BannerPricingResponse> getPricingById(String id) {
        return pricingRepository.findById(id)
                .map(pricingMapper::toResponse);
    }

    @Override
    @Transactional
    public BannerPricingResponse createPricing(BannerPricingRequest request) {
        BannerPricing pricing = pricingMapper.toEntity(request);
        pricing.setPricingCode(generatePricingCode());
        BannerPricing saved = pricingRepository.save(pricing);
        log.info("Created pricing template: {}", saved.getPricingCode());
        return pricingMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public Optional<BannerPricingResponse> updatePricing(String id, BannerPricingRequest request) {
        return pricingRepository.findById(id)
                .map(existing -> {
                    pricingMapper.updateEntity(existing, request);
                    BannerPricing saved = pricingRepository.save(existing);
                    log.info("Updated pricing: {}", id);
                    return pricingMapper.toResponse(saved);
                });
    }

    @Override
    @Transactional
    public boolean deletePricing(String id) {
        if (!pricingRepository.existsById(id)) {
            return false;
        }
        pricingRepository.deleteById(id);
        log.info("Deleted pricing: {}", id);
        return true;
    }

    @Override
    public BigDecimal calculateEstimate(String pageLocation, String position, String pricingType,
                                         LocalDateTime startDate, LocalDateTime endDate) {
        Optional<BannerPricing> pricingOpt = pricingRepository.findActivePricingForSlotAndType(
                pageLocation, position, pricingType, LocalDateTime.now());

        if (pricingOpt.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BannerPricing pricing = pricingOpt.get();
        BigDecimal basePrice = pricing.getBasePrice();

        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;

        BigDecimal estimate;

        if (PricingType.FLAT_RATE_DAILY.name().equals(pricingType)) {
            estimate = basePrice.multiply(BigDecimal.valueOf(days));
        } else if (PricingType.FLAT_RATE_WEEKLY.name().equals(pricingType)) {
            long weeks = (days + 6) / 7;
            estimate = basePrice.multiply(BigDecimal.valueOf(weeks));
        } else if (PricingType.FLAT_RATE_MONTHLY.name().equals(pricingType)) {
            long months = ChronoUnit.MONTHS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
            estimate = basePrice.multiply(BigDecimal.valueOf(months));
        } else if (PricingType.CPM.name().equals(pricingType)) {
            estimate = basePrice.multiply(BigDecimal.valueOf(days)).multiply(BigDecimal.valueOf(10));
        } else if (PricingType.CPC.name().equals(pricingType)) {
            estimate = basePrice.multiply(BigDecimal.valueOf(days)).multiply(BigDecimal.valueOf(100));
        } else {
            estimate = basePrice.multiply(BigDecimal.valueOf(days));
        }

        if (pricing.getDiscountPercentage() != null && pricing.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = estimate.multiply(pricing.getDiscountPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            estimate = estimate.subtract(discount);
        }

        if (pricing.getMinimumSpend() != null && estimate.compareTo(pricing.getMinimumSpend()) < 0) {
            estimate = pricing.getMinimumSpend();
        }

        return estimate.setScale(2, RoundingMode.HALF_UP);
    }

    private String generatePricingCode() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyMM"));
        String codePrefix = "BP-" + yearMonth + "-";

        List<String> latestCodes = pricingRepository.findLatestCodesByPrefix(codePrefix);

        int nextSequence = 1;
        if (!latestCodes.isEmpty()) {
            String latestCode = latestCodes.get(0);
            try {
                String sequencePart = latestCode.substring(codePrefix.length());
                nextSequence = Integer.parseInt(sequencePart) + 1;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                log.warn("Failed to parse sequence number, starting from 1", e);
            }
        }

        return String.format("%s%06d", codePrefix, nextSequence);
    }
}
