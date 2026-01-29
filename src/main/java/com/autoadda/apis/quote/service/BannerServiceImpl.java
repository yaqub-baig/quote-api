package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.entity.Banner;
import com.autoadda.apis.quote.exception.BannerNotFoundException;
import com.autoadda.apis.quote.mapper.BannerMapper;
import com.autoadda.apis.quote.repository.BannerRepository;
import com.autoadda.apis.quote.request.BannerCreateRequest;
import com.autoadda.apis.quote.request.BannerUpdateRequest;
import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.util.BannerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    private final BannerAnalyticsService bannerAnalyticsService;

    @Override
    public List<BannerResponse> getAllBanners() {
        return bannerRepository.findAll()
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerResponse> getBannersByStatus(BannerStatus status) {
        return bannerRepository.findByStatus(status)
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerResponse> getBannersByPageLocation(String pageLocation) {
        return bannerRepository.findByPageLocation(pageLocation)
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerResponse> getBannersByVendorId(Integer vendorId) {
        return bannerRepository.findByVendorId(vendorId)
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<BannerResponse> getBannerById(String id) {
        return bannerRepository.findById(id)
                .map(bannerMapper::toResponse);
    }

    @Override
    @Transactional
    public BannerResponse createBanner(BannerCreateRequest request, Integer createdBy) {
        Banner banner = bannerMapper.toEntity(request);
        banner.setBannerCode(generateBannerCode());
        //have to change when we work on advancement on banner
        banner.setStatus(BannerStatus.ACTIVE);
        banner.setCreatedBy(createdBy);
        Banner saved = bannerRepository.save(banner);
        log.info("Created banner with code: {}", saved.getBannerCode());
        return bannerMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public Optional<BannerResponse> updateBanner(String id, BannerUpdateRequest request) {
        return bannerRepository.findById(id)
                .map(existing -> {
                    bannerMapper.updateEntity(existing, request);
                    Banner saved = bannerRepository.save(existing);
                    log.info("Updated banner: {}", id);
                    return bannerMapper.toResponse(saved);
                });
    }

    @Override
    @Transactional
    public boolean deleteBanner(String id) {
        if (!bannerRepository.existsById(id)) {
            return false;
        }
        bannerRepository.findById(id).ifPresent(banner -> {
            banner.setStatus(BannerStatus.ARCHIVED);
            banner.setUpdatedAt(LocalDateTime.now());
            bannerRepository.save(banner);
            log.info("Soft deleted (archived) banner: {}", id);
        });
        return true;
    }

    @Override
    @Transactional
    public Optional<BannerResponse> updateBannerStatus(String id, BannerStatus status) {
        return bannerRepository.findById(id)
                .map(banner -> {
                    banner.setStatus(status);
                    banner.setUpdatedAt(LocalDateTime.now());
                    Banner saved = bannerRepository.save(banner);
                    log.info("Updated banner {} status to: {}", id, status);
                    return bannerMapper.toResponse(saved);
                });
    }

    @Override
    public List<BannerResponse> getActiveBannersForDisplay() {
        return bannerRepository.findActiveBannersForDisplay(BannerStatus.ACTIVE, LocalDateTime.now())
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerResponse> getActiveBannersForPage(String pageLocation) {
        return bannerRepository.findActiveBannersForPage(BannerStatus.ACTIVE, pageLocation, LocalDateTime.now())
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerResponse> getActiveBannersForSlot(String pageLocation, String position) {
        return bannerRepository.findActiveBannersForSlot(BannerStatus.ACTIVE, pageLocation, position, LocalDateTime.now())
                .stream()
                .map(bannerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void trackImpression(String bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new BannerNotFoundException("Banner not found: " + bannerId));

        if (banner.getTotalImpressionLimit() != null &&
            banner.getTotalImpressions() != null &&
            banner.getTotalImpressions() >= banner.getTotalImpressionLimit()) {
            log.warn("Banner {} has reached impression limit", bannerId);
            return;
        }

        banner.setTotalImpressions(banner.getTotalImpressions() != null ? banner.getTotalImpressions() + 1 : 1);
        banner.setUpdatedAt(LocalDateTime.now());
        bannerRepository.save(banner);

        bannerAnalyticsService.recordImpression(bannerId);
    }

    @Override
    @Transactional
    public void trackClick(String bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new BannerNotFoundException("Banner not found: " + bannerId));

        banner.setTotalClicks(banner.getTotalClicks() != null ? banner.getTotalClicks() + 1 : 1);
        banner.setUpdatedAt(LocalDateTime.now());
        bannerRepository.save(banner);

        bannerAnalyticsService.recordClick(bannerId);
    }

    private String generateBannerCode() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyMM"));
        String codePrefix = "BN-" + yearMonth + "-";

        List<String> latestCodes = bannerRepository.findLatestCodesByPrefix(codePrefix);

        int nextSequence = 1;
        if (!latestCodes.isEmpty()) {
            String latestCode = latestCodes.get(0);
            try {
                String sequencePart = latestCode.substring(codePrefix.length());
                nextSequence = Integer.parseInt(sequencePart) + 1;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                log.warn("Failed to parse sequence number, starting from 1", e);
            }
        }

        return String.format("%s%06d", codePrefix, nextSequence);
    }
}
