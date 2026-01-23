package com.autoadda.apis.quote.repository;

import com.autoadda.apis.quote.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {

    Optional<Banner> findByBannerCode(String bannerCode);

    @Query("SELECT b.bannerCode FROM Banner b WHERE b.bannerCode LIKE :prefix% ORDER BY b.bannerCode DESC")
    List<String> findLatestCodesByPrefix(@Param("prefix") String codePrefix);

    List<Banner> findByStatus(String status);

    List<Banner> findByVendorId(Integer vendorId);

    List<Banner> findByPageLocation(String pageLocation);

    List<Banner> findByPageLocationAndPosition(String pageLocation, String position);

    List<Banner> findByStatusAndPageLocation(String status, String pageLocation);

    List<Banner> findByStatusAndPageLocationAndPosition(String status, String pageLocation, String position);

    @Query("SELECT b FROM Banner b WHERE b.status = :status AND b.startDate <= :now AND (b.endDate IS NULL OR b.endDate >= :now)")
    List<Banner> findActiveBannersForDisplay(@Param("status") String status, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Banner b WHERE b.status = :status AND b.pageLocation = :pageLocation AND b.startDate <= :now AND (b.endDate IS NULL OR b.endDate >= :now) ORDER BY b.priority ASC")
    List<Banner> findActiveBannersForPage(@Param("status") String status, @Param("pageLocation") String pageLocation, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Banner b WHERE b.status = :status AND b.pageLocation = :pageLocation AND b.position = :position AND b.startDate <= :now AND (b.endDate IS NULL OR b.endDate >= :now) ORDER BY b.priority ASC")
    List<Banner> findActiveBannersForSlot(@Param("status") String status, @Param("pageLocation") String pageLocation, @Param("position") String position, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Banner b WHERE b.status = 'ACTIVE' AND b.endDate < :now")
    List<Banner> findExpiredActiveBanners(@Param("now") LocalDateTime now);

    @Query("SELECT b FROM Banner b WHERE b.status = 'DRAFT' AND b.startDate <= :now")
    List<Banner> findBannersToActivate(@Param("now") LocalDateTime now);
}
