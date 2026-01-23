package com.autoadda.apis.quote.repository;

import com.autoadda.apis.quote.entity.BannerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRequestRepository extends JpaRepository<BannerRequest, String> {

    Optional<BannerRequest> findByRequestCode(String requestCode);

    @Query("SELECT r.requestCode FROM BannerRequest r WHERE r.requestCode LIKE :prefix% ORDER BY r.requestCode DESC")
    List<String> findLatestCodesByPrefix(@Param("prefix") String codePrefix);

    List<BannerRequest> findByVendorId(Integer vendorId);

    List<BannerRequest> findByStatus(String status);

    List<BannerRequest> findByVendorIdAndStatus(Integer vendorId, String status);

    List<BannerRequest> findByReviewedBy(Integer reviewedBy);

    @Query("SELECT r FROM BannerRequest r WHERE r.vendorId = :vendorId ORDER BY r.createdAt DESC")
    List<BannerRequest> findByVendorIdOrderByCreatedAtDesc(@Param("vendorId") Integer vendorId);

    @Query("SELECT r FROM BannerRequest r ORDER BY r.createdAt DESC")
    List<BannerRequest> findAllOrderByCreatedAtDesc();
}
