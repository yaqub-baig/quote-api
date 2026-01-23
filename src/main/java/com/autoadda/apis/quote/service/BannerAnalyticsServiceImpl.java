package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.entity.BannerAnalytics;
import com.autoadda.apis.quote.repository.BannerAnalyticsRepository;
import com.autoadda.apis.quote.response.BannerAnalyticsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerAnalyticsServiceImpl implements BannerAnalyticsService {

    private final BannerAnalyticsRepository analyticsRepository;

    @Override
    public List<BannerAnalyticsResponse> getAnalyticsByBannerId(String bannerId) {
        return analyticsRepository.findByBannerId(bannerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<BannerAnalyticsResponse> getAnalyticsByBannerIdAndDateRange(String bannerId, LocalDate startDate, LocalDate endDate) {
        return analyticsRepository.findByBannerIdAndDateRange(bannerId, startDate, endDate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<BannerAnalyticsResponse> getAnalyticsByBannerIdAndDate(String bannerId, LocalDate date) {
        return analyticsRepository.findByBannerIdAndAnalyticsDate(bannerId, date)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public void recordImpression(String bannerId) {
        BannerAnalytics analytics = getOrCreateAnalytics(bannerId, LocalDate.now());
        analytics.setImpressions(analytics.getImpressions() + 1);
        analytics.setUniqueImpressions(analytics.getUniqueImpressions() != null ? analytics.getUniqueImpressions() + 1 : 1L);
        updateCtr(analytics);
        analyticsRepository.save(analytics);
        log.debug("Recorded impression for banner: {}", bannerId);
    }

    @Override
    @Transactional
    public void recordClick(String bannerId) {
        BannerAnalytics analytics = getOrCreateAnalytics(bannerId, LocalDate.now());
        analytics.setClicks(analytics.getClicks() + 1);
        analytics.setUniqueClicks(analytics.getUniqueClicks() != null ? analytics.getUniqueClicks() + 1 : 1L);
        updateCtr(analytics);
        analyticsRepository.save(analytics);
        log.debug("Recorded click for banner: {}", bannerId);
    }

    @Override
    @Transactional
    public BannerAnalyticsResponse getOrCreateTodayAnalytics(String bannerId) {
        BannerAnalytics analytics = getOrCreateAnalytics(bannerId, LocalDate.now());
        return toResponse(analytics);
    }

    private BannerAnalytics getOrCreateAnalytics(String bannerId, LocalDate date) {
        return analyticsRepository.findByBannerIdAndAnalyticsDate(bannerId, date)
                .orElseGet(() -> {
                    BannerAnalytics newAnalytics = new BannerAnalytics();
                    newAnalytics.setId(UUID.randomUUID().toString());
                    newAnalytics.setBannerId(bannerId);
                    newAnalytics.setAnalyticsDate(date);
                    newAnalytics.setImpressions(0L);
                    newAnalytics.setClicks(0L);
                    newAnalytics.setUniqueImpressions(0L);
                    newAnalytics.setUniqueClicks(0L);
                    newAnalytics.setCtr(BigDecimal.ZERO);
                    newAnalytics.setCostIncurred(BigDecimal.ZERO);
                    return analyticsRepository.save(newAnalytics);
                });
    }

    private void updateCtr(BannerAnalytics analytics) {
        if (analytics.getImpressions() != null && analytics.getImpressions() > 0 && analytics.getClicks() != null) {
            BigDecimal clicks = BigDecimal.valueOf(analytics.getClicks());
            BigDecimal impressions = BigDecimal.valueOf(analytics.getImpressions());
            analytics.setCtr(clicks.divide(impressions, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        }
    }

    private BannerAnalyticsResponse toResponse(BannerAnalytics analytics) {
        BannerAnalyticsResponse response = new BannerAnalyticsResponse();
        response.setId(analytics.getId());
        response.setBannerId(analytics.getBannerId());
        response.setAnalyticsDate(analytics.getAnalyticsDate());
        response.setImpressions(analytics.getImpressions());
        response.setClicks(analytics.getClicks());
        response.setUniqueImpressions(analytics.getUniqueImpressions());
        response.setUniqueClicks(analytics.getUniqueClicks());
        response.setCtr(analytics.getCtr());
        response.setCostIncurred(analytics.getCostIncurred());
        return response;
    }
}
