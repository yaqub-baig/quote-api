package com.autoadda.apis.quote.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerCreateRequest {
    private String title;
    private String description;
    private Long bannerImageId;
    private String targetUrl;
    private String pageLocation;
    private String position;
    private String bannerType;
    private Integer priority;
    private Integer vendorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long dailyImpressionLimit;
    private Long totalImpressionLimit;
    private Boolean isForPhone;
}
