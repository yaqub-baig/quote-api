package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.response.BannerAnalyticsResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BannerAnalyticsService {

    List<BannerAnalyticsResponse> getAnalyticsByBannerId(String bannerId);

    List<BannerAnalyticsResponse> getAnalyticsByBannerIdAndDateRange(String bannerId, LocalDate startDate, LocalDate endDate);

    Optional<BannerAnalyticsResponse> getAnalyticsByBannerIdAndDate(String bannerId, LocalDate date);

    void recordImpression(String bannerId);

    void recordClick(String bannerId);

    BannerAnalyticsResponse getOrCreateTodayAnalytics(String bannerId);
}
