package com.autoadda.apis.quote.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequestSubmission {
    private String vendorName;
    private String vendorEmail;
    private String vendorPhone;
    private String title;
    private String description;
    private Long proposedImageId;
    private String requestedPageLocation;
    private String requestedPosition;
    private LocalDateTime requestedStartDate;
    private LocalDateTime requestedEndDate;
    private String requestedPricingType;
    private BigDecimal proposedBudget;
    private Boolean isForPhone;
}
