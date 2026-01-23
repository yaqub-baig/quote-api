package com.autoadda.apis.quote.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "banner_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequest {
    @Id
    private String id;

    @Column(name = "request_code", unique = true, nullable = false)
    private String requestCode;

    @Column(name = "vendor_id", nullable = false)
    private Integer vendorId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "vendor_email")
    private String vendorEmail;

    @Column(name = "vendor_phone")
    private String vendorPhone;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposed_image_id")
    private MediaFile proposedImage;

    @Column(name = "requested_page_location", nullable = false)
    private String requestedPageLocation;

    @Column(name = "requested_position", nullable = false)
    private String requestedPosition;

    @Column(name = "requested_start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime requestedStartDate;

    @Column(name = "requested_end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime requestedEndDate;

    @Column(name = "requested_pricing_type")
    private String requestedPricingType;

    @Column(name = "proposed_budget", precision = 15, scale = 2)
    private BigDecimal proposedBudget;

    @Column(nullable = false)
    private String status;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "approved_banner_id")
    private String approvedBannerId;

    @Column(name = "reviewed_by")
    private Integer reviewedBy;

    @Column(name = "reviewed_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime reviewedAt;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    @Column(name = "is_for_phone")
    private Boolean isForPhone;
}
