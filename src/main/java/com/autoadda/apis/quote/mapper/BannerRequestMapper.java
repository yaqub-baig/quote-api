package com.autoadda.apis.quote.mapper;

import com.autoadda.apis.quote.entity.BannerRequest;
import com.autoadda.apis.quote.repository.MediaFileRepository;
import com.autoadda.apis.quote.request.BannerRequestSubmission;
import com.autoadda.apis.quote.response.BannerRequestResponse;
import com.autoadda.apis.quote.util.BannerRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BannerRequestMapper {

    private final MediaFileRepository mediaFileRepository;
    private final MediaFileMapper mediaFileMapper;

    public BannerRequest toEntity(BannerRequestSubmission request, Integer vendorId) {
        BannerRequest entity = new BannerRequest();
        entity.setId(UUID.randomUUID().toString());
        entity.setVendorId(vendorId);
        entity.setVendorName(request.getVendorName());
        entity.setVendorEmail(request.getVendorEmail());
        entity.setVendorPhone(request.getVendorPhone());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        if (request.getProposedImageId() != null) {
            mediaFileRepository.findById(request.getProposedImageId())
                    .ifPresent(entity::setProposedImage);
        }
        entity.setRequestedPageLocation(request.getRequestedPageLocation());
        entity.setRequestedPosition(request.getRequestedPosition());
        entity.setRequestedStartDate(request.getRequestedStartDate());
        entity.setRequestedEndDate(request.getRequestedEndDate());
        entity.setRequestedPricingType(request.getRequestedPricingType());
        entity.setProposedBudget(request.getProposedBudget());
        entity.setIsForPhone(request.getIsForPhone());
        entity.setStatus(BannerRequestStatus.SUBMITTED.name());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public BannerRequestResponse toResponse(BannerRequest entity) {
        BannerRequestResponse response = new BannerRequestResponse();
        response.setId(entity.getId());
        response.setRequestCode(entity.getRequestCode());
        response.setVendorId(entity.getVendorId());
        response.setVendorName(entity.getVendorName());
        response.setVendorEmail(entity.getVendorEmail());
        response.setVendorPhone(entity.getVendorPhone());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setProposedImage(mediaFileMapper.toResponse(entity.getProposedImage()));
        response.setRequestedPageLocation(entity.getRequestedPageLocation());
        response.setRequestedPosition(entity.getRequestedPosition());
        response.setRequestedStartDate(entity.getRequestedStartDate());
        response.setRequestedEndDate(entity.getRequestedEndDate());
        response.setRequestedPricingType(entity.getRequestedPricingType());
        response.setProposedBudget(entity.getProposedBudget());
        response.setStatus(entity.getStatus());
        response.setAdminNotes(entity.getAdminNotes());
        response.setRejectionReason(entity.getRejectionReason());
        response.setApprovedBannerId(entity.getApprovedBannerId());
        response.setReviewedBy(entity.getReviewedBy());
        response.setReviewedAt(entity.getReviewedAt());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setIsForPhone(entity.getIsForPhone());
        return response;
    }

    public void updateEntity(BannerRequest existing, BannerRequestSubmission request) {
        Optional.ofNullable(request.getVendorName()).ifPresent(existing::setVendorName);
        Optional.ofNullable(request.getVendorEmail()).ifPresent(existing::setVendorEmail);
        Optional.ofNullable(request.getVendorPhone()).ifPresent(existing::setVendorPhone);
        Optional.ofNullable(request.getTitle()).ifPresent(existing::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(existing::setDescription);
        if (request.getProposedImageId() != null) {
            mediaFileRepository.findById(request.getProposedImageId())
                    .ifPresent(existing::setProposedImage);
        }
        Optional.ofNullable(request.getRequestedPageLocation()).ifPresent(existing::setRequestedPageLocation);
        Optional.ofNullable(request.getRequestedPosition()).ifPresent(existing::setRequestedPosition);
        Optional.ofNullable(request.getRequestedStartDate()).ifPresent(existing::setRequestedStartDate);
        Optional.ofNullable(request.getRequestedEndDate()).ifPresent(existing::setRequestedEndDate);
        Optional.ofNullable(request.getRequestedPricingType()).ifPresent(existing::setRequestedPricingType);
        Optional.ofNullable(request.getProposedBudget()).ifPresent(existing::setProposedBudget);
        Optional.ofNullable(request.getIsForPhone()).ifPresent(existing::setIsForPhone);
        existing.setUpdatedAt(LocalDateTime.now());
    }
}
