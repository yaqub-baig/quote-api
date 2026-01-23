package com.autoadda.apis.quote.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "banner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
    @Id
    private String id;

    @Column(name = "banner_code", unique = true, nullable = false)
    private String bannerCode;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_image_id")
    private MediaFile bannerImage;

    @Column(name = "target_url")
    private String targetUrl;

    @Column(name = "page_location", nullable = false)
    private String pageLocation;

    @Column(nullable = false)
    private String position;

    @Column(name = "banner_type", nullable = false)
    private String bannerType;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer priority;

    @Column(name = "vendor_id")
    private Integer vendorId;

    @Column(name = "start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @Column(name = "total_impressions")
    private Long totalImpressions;

    @Column(name = "total_clicks")
    private Long totalClicks;

    @Column(name = "daily_impression_limit")
    private Long dailyImpressionLimit;

    @Column(name = "total_impression_limit")
    private Long totalImpressionLimit;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "is_for_phone")
    private Boolean isForPhone;
}
