package com.autoadda.apis.quote.response;

import com.autoadda.apis.quote.util.BannerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponse {
    private String id;
    private String bannerCode;
    private String title;
    private String description;
    private MediaFileResponse bannerImage;
    private String targetUrl;
    private String pageLocation;
    private String position;
    private String bannerType;
    private BannerStatus status;
    private Integer priority;
    private Integer vendorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long totalImpressions;
    private Long totalClicks;
    private Long dailyImpressionLimit;
    private Long totalImpressionLimit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer createdBy;
    private Boolean isForPhone;
}
