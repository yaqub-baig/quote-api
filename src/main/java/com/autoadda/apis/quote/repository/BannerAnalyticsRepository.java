package com.autoadda.apis.quote.repository;

import com.autoadda.apis.quote.entity.BannerAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BannerAnalyticsRepository extends JpaRepository<BannerAnalytics, String> {

    List<BannerAnalytics> findByBannerId(String bannerId);

    Optional<BannerAnalytics> findByBannerIdAndAnalyticsDate(String bannerId, LocalDate analyticsDate);

    @Query("SELECT a FROM BannerAnalytics a WHERE a.bannerId = :bannerId AND a.analyticsDate BETWEEN :startDate AND :endDate ORDER BY a.analyticsDate ASC")
    List<BannerAnalytics> findByBannerIdAndDateRange(@Param("bannerId") String bannerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM BannerAnalytics a WHERE a.analyticsDate = :date")
    List<BannerAnalytics> findByAnalyticsDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(a.impressions) FROM BannerAnalytics a WHERE a.bannerId = :bannerId")
    Long sumImpressionsByBannerId(@Param("bannerId") String bannerId);

    @Query("SELECT SUM(a.clicks) FROM BannerAnalytics a WHERE a.bannerId = :bannerId")
    Long sumClicksByBannerId(@Param("bannerId") String bannerId);

    @Query("SELECT SUM(a.impressions) FROM BannerAnalytics a WHERE a.bannerId = :bannerId AND a.analyticsDate = :date")
    Long sumImpressionsByBannerIdAndDate(@Param("bannerId") String bannerId, @Param("date") LocalDate date);

    @Query("SELECT SUM(a.costIncurred) FROM BannerAnalytics a WHERE a.bannerId = :bannerId AND a.analyticsDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal sumCostByBannerIdAndDateRange(@Param("bannerId") String bannerId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
