package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerRequestResponse {
    private String id;
    private String requestCode;
    private Integer vendorId;
    private String vendorName;
    private String vendorEmail;
    private String vendorPhone;
    private String title;
    private String description;
    private MediaFileResponse proposedImage;
    private String requestedPageLocation;
    private String requestedPosition;
    private LocalDateTime requestedStartDate;
    private LocalDateTime requestedEndDate;
    private String requestedPricingType;
    private BigDecimal proposedBudget;
    private String status;
    private String adminNotes;
    private String rejectionReason;
    private String approvedBannerId;
    private Integer reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isForPhone;
}
