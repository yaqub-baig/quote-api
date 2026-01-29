package com.autoadda.apis.quote.mapper;

import com.autoadda.apis.quote.entity.Banner;
import com.autoadda.apis.quote.repository.MediaFileRepository;
import com.autoadda.apis.quote.request.BannerCreateRequest;
import com.autoadda.apis.quote.request.BannerUpdateRequest;
import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.util.BannerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BannerMapper {

    private final MediaFileRepository mediaFileRepository;
    private final MediaFileMapper mediaFileMapper;

    public Banner toEntity(BannerCreateRequest request) {
        Banner banner = new Banner();
        banner.setId(UUID.randomUUID().toString());
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        if (request.getBannerImageId() != null) {
            mediaFileRepository.findById(request.getBannerImageId())
                    .ifPresent(banner::setBannerImage);
        }
        banner.setTargetUrl(request.getTargetUrl());
        banner.setPageLocation(request.getPageLocation());
        banner.setPosition(request.getPosition());
        banner.setBannerType(request.getBannerType());
        banner.setStatus(BannerStatus.DRAFT);
        banner.setPriority(request.getPriority() != null ? request.getPriority() : 100);
        banner.setVendorId(request.getVendorId());
        banner.setStartDate(request.getStartDate());
        banner.setEndDate(request.getEndDate());
        banner.setTotalImpressions(0L);
        banner.setTotalClicks(0L);
        banner.setDailyImpressionLimit(request.getDailyImpressionLimit());
        banner.setTotalImpressionLimit(request.getTotalImpressionLimit());
        banner.setIsForPhone(request.getIsForPhone());
        banner.setCreatedAt(LocalDateTime.now());
        return banner;
    }

    public BannerResponse toResponse(Banner banner) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getId());
        response.setBannerCode(banner.getBannerCode());
        response.setTitle(banner.getTitle());
        response.setDescription(banner.getDescription());
        response.setBannerImage(mediaFileMapper.toResponse(banner.getBannerImage()));
        response.setTargetUrl(banner.getTargetUrl());
        response.setPageLocation(banner.getPageLocation());
        response.setPosition(banner.getPosition());
        response.setBannerType(banner.getBannerType());
        response.setStatus(banner.getStatus());
        response.setPriority(banner.getPriority());
        response.setVendorId(banner.getVendorId());
        response.setStartDate(banner.getStartDate());
        response.setEndDate(banner.getEndDate());
        response.setTotalImpressions(banner.getTotalImpressions());
        response.setTotalClicks(banner.getTotalClicks());
        response.setDailyImpressionLimit(banner.getDailyImpressionLimit());
        response.setTotalImpressionLimit(banner.getTotalImpressionLimit());
        response.setCreatedAt(banner.getCreatedAt());
        response.setUpdatedAt(banner.getUpdatedAt());
        response.setCreatedBy(banner.getCreatedBy());
        response.setIsForPhone(banner.getIsForPhone());
        return response;
    }

    public void updateEntity(Banner existing, BannerUpdateRequest request) {
        Optional.ofNullable(request.getTitle()).ifPresent(existing::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(existing::setDescription);
        if (request.getBannerImageId() != null) {
            mediaFileRepository.findById(request.getBannerImageId())
                    .ifPresent(existing::setBannerImage);
        }
        Optional.ofNullable(request.getTargetUrl()).ifPresent(existing::setTargetUrl);
        Optional.ofNullable(request.getPageLocation()).ifPresent(existing::setPageLocation);
        Optional.ofNullable(request.getPosition()).ifPresent(existing::setPosition);
        Optional.ofNullable(request.getBannerType()).ifPresent(existing::setBannerType);
        Optional.ofNullable(request.getStatus()).ifPresent(existing::setStatus);
        Optional.ofNullable(request.getPriority()).ifPresent(existing::setPriority);
        Optional.ofNullable(request.getStartDate()).ifPresent(existing::setStartDate);
        Optional.ofNullable(request.getEndDate()).ifPresent(existing::setEndDate);
        Optional.ofNullable(request.getDailyImpressionLimit()).ifPresent(existing::setDailyImpressionLimit);
        Optional.ofNullable(request.getTotalImpressionLimit()).ifPresent(existing::setTotalImpressionLimit);
        Optional.ofNullable(request.getIsForPhone()).ifPresent(existing::setIsForPhone);
        existing.setUpdatedAt(LocalDateTime.now());
    }
}
