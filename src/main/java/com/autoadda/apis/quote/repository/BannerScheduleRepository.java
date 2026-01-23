package com.autoadda.apis.quote.repository;

import com.autoadda.apis.quote.entity.BannerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BannerScheduleRepository extends JpaRepository<BannerSchedule, String> {

    Optional<BannerSchedule> findByScheduleCode(String scheduleCode);

    @Query("SELECT s.scheduleCode FROM BannerSchedule s WHERE s.scheduleCode LIKE :prefix% ORDER BY s.scheduleCode DESC")
    List<String> findLatestCodesByPrefix(@Param("prefix") String codePrefix);

    List<BannerSchedule> findByBannerId(String bannerId);

    List<BannerSchedule> findByStatus(String status);

    List<BannerSchedule> findByBannerIdAndStatus(String bannerId, String status);

    @Query("SELECT s FROM BannerSchedule s WHERE s.status = 'SCHEDULED' AND s.startDate <= :now")
    List<BannerSchedule> findSchedulesToActivate(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM BannerSchedule s WHERE s.status = 'ACTIVE' AND s.endDate < :now")
    List<BannerSchedule> findSchedulesToComplete(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM BannerSchedule s WHERE s.bannerId = :bannerId AND s.status IN ('SCHEDULED', 'ACTIVE') AND ((s.startDate <= :endDate AND s.endDate >= :startDate))")
    List<BannerSchedule> findOverlappingSchedules(@Param("bannerId") String bannerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
