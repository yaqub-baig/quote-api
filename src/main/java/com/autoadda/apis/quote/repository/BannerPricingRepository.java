package com.autoadda.apis.quote.repository;

import com.autoadda.apis.quote.entity.BannerPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BannerPricingRepository extends JpaRepository<BannerPricing, String> {

    Optional<BannerPricing> findByPricingCode(String pricingCode);

    @Query("SELECT p.pricingCode FROM BannerPricing p WHERE p.pricingCode LIKE :prefix% ORDER BY p.pricingCode DESC")
    List<String> findLatestCodesByPrefix(@Param("prefix") String codePrefix);

    List<BannerPricing> findByPageLocation(String pageLocation);

    List<BannerPricing> findByPageLocationAndPosition(String pageLocation, String position);

    List<BannerPricing> findByPricingType(String pricingType);

    List<BannerPricing> findByIsTemplate(Boolean isTemplate);

    @Query("SELECT p FROM BannerPricing p WHERE p.pageLocation = :pageLocation AND p.position = :position AND p.effectiveFrom <= :now AND (p.effectiveUntil IS NULL OR p.effectiveUntil >= :now)")
    List<BannerPricing> findActivePricingForSlot(@Param("pageLocation") String pageLocation, @Param("position") String position, @Param("now") LocalDateTime now);

    @Query("SELECT p FROM BannerPricing p WHERE p.pageLocation = :pageLocation AND p.position = :position AND p.pricingType = :pricingType AND p.effectiveFrom <= :now AND (p.effectiveUntil IS NULL OR p.effectiveUntil >= :now)")
    Optional<BannerPricing> findActivePricingForSlotAndType(@Param("pageLocation") String pageLocation, @Param("position") String position, @Param("pricingType") String pricingType, @Param("now") LocalDateTime now);

    @Query("SELECT p FROM BannerPricing p WHERE p.isTemplate = true AND p.effectiveFrom <= :now AND (p.effectiveUntil IS NULL OR p.effectiveUntil >= :now)")
    List<BannerPricing> findActiveTemplatePricing(@Param("now") LocalDateTime now);
}
