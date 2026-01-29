package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.entity.Banner;
import com.autoadda.apis.quote.entity.BannerRequest;
import com.autoadda.apis.quote.exception.BannerRequestNotFoundException;
import com.autoadda.apis.quote.exception.UnauthorizedAccessException;
import com.autoadda.apis.quote.mapper.BannerMapper;
import com.autoadda.apis.quote.mapper.BannerRequestMapper;
import com.autoadda.apis.quote.repository.BannerRepository;
import com.autoadda.apis.quote.repository.BannerRequestRepository;
import com.autoadda.apis.quote.request.BannerRequestSubmission;
import com.autoadda.apis.quote.response.BannerRequestResponse;
import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.util.BannerRequestStatus;
import com.autoadda.apis.quote.util.BannerStatus;
import com.autoadda.apis.quote.util.BannerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerRequestServiceImpl implements BannerRequestService {

    private final BannerRequestRepository requestRepository;
    private final BannerRepository bannerRepository;
    private final BannerRequestMapper requestMapper;
    private final BannerMapper bannerMapper;

    @Override
    public List<BannerRequestResponse> getAllRequests() {
        return requestRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(requestMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerRequestResponse> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status)
                .stream()
                .map(requestMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerRequestResponse> getRequestsByVendorId(Integer vendorId) {
        return requestRepository.findByVendorIdOrderByCreatedAtDesc(vendorId)
                .stream()
                .map(requestMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<BannerRequestResponse> getRequestById(String id) {
        return requestRepository.findById(id)
                .map(requestMapper::toResponse);
    }

    @Override
    @Transactional
    public BannerRequestResponse submitRequest(BannerRequestSubmission submission, Integer vendorId) {
        BannerRequest request = requestMapper.toEntity(submission, vendorId);
        request.setRequestCode(generateRequestCode());
        BannerRequest saved = requestRepository.save(request);
        log.info("Vendor {} submitted banner request with code: {}", vendorId, saved.getRequestCode());
        return requestMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public Optional<BannerRequestResponse> updateRequest(String id, BannerRequestSubmission submission, Integer vendorId) {
        return requestRepository.findById(id)
                .map(existing -> {
                    if (!existing.getVendorId().equals(vendorId)) {
                        throw new UnauthorizedAccessException("Not authorized to update this request");
                    }
                    if (!BannerRequestStatus.SUBMITTED.name().equals(existing.getStatus()) &&
                        !BannerRequestStatus.NEGOTIATING.name().equals(existing.getStatus())) {
                        throw new IllegalArgumentException("Cannot update request in status: " + existing.getStatus());
                    }
                    requestMapper.updateEntity(existing, submission);
                    BannerRequest saved = requestRepository.save(existing);
                    log.info("Updated banner request: {}", id);
                    return requestMapper.toResponse(saved);
                });
    }

    @Override
    @Transactional
    public boolean cancelRequest(String id, Integer vendorId) {
        return requestRepository.findById(id)
                .map(existing -> {
                    if (!existing.getVendorId().equals(vendorId)) {
                        throw new UnauthorizedAccessException("Not authorized to cancel this request");
                    }
                    if (BannerRequestStatus.APPROVED.name().equals(existing.getStatus()) ||
                        BannerRequestStatus.COMPLETED.name().equals(existing.getStatus())) {
                        throw new IllegalArgumentException("Cannot cancel request in status: " + existing.getStatus());
                    }
                    existing.setStatus(BannerRequestStatus.CANCELLED.name());
                    existing.setUpdatedAt(LocalDateTime.now());
                    requestRepository.save(existing);
                    log.info("Cancelled banner request: {}", id);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public BannerResponse approveRequest(String id, Integer adminId, String adminNotes) {
        BannerRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new BannerRequestNotFoundException("Banner request not found: " + id));

        if (!BannerRequestStatus.SUBMITTED.name().equals(request.getStatus()) &&
            !BannerRequestStatus.UNDER_REVIEW.name().equals(request.getStatus())) {
            throw new IllegalArgumentException("Cannot approve request in status: " + request.getStatus());
        }

        Banner banner = createBannerFromRequest(request, adminId);
        Banner savedBanner = bannerRepository.save(banner);

        request.setStatus(BannerRequestStatus.APPROVED.name());
        request.setApprovedBannerId(savedBanner.getId());
        request.setReviewedBy(adminId);
        request.setReviewedAt(LocalDateTime.now());
        request.setAdminNotes(adminNotes);
        request.setUpdatedAt(LocalDateTime.now());
        requestRepository.save(request);

        log.info("Approved banner request {} and created banner {}", id, savedBanner.getBannerCode());
        return bannerMapper.toResponse(savedBanner);
    }

    @Override
    @Transactional
    public BannerRequestResponse rejectRequest(String id, Integer adminId, String rejectionReason) {
        BannerRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new BannerRequestNotFoundException("Banner request not found: " + id));

        if (!BannerRequestStatus.SUBMITTED.name().equals(request.getStatus()) &&
            !BannerRequestStatus.UNDER_REVIEW.name().equals(request.getStatus())) {
            throw new IllegalArgumentException("Cannot reject request in status: " + request.getStatus());
        }

        request.setStatus(BannerRequestStatus.REJECTED.name());
        request.setRejectionReason(rejectionReason);
        request.setReviewedBy(adminId);
        request.setReviewedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        BannerRequest saved = requestRepository.save(request);

        log.info("Rejected banner request {} by admin {}", id, adminId);
        return requestMapper.toResponse(saved);
    }

    private Banner createBannerFromRequest(BannerRequest request, Integer adminId) {
        Banner banner = new Banner();
        banner.setId(UUID.randomUUID().toString());
        banner.setBannerCode(generateBannerCode());
        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setBannerImage(request.getProposedImage());
        banner.setPageLocation(request.getRequestedPageLocation());
        banner.setPosition(request.getRequestedPosition());
        banner.setBannerType(BannerType.IMAGE.name());
        banner.setStatus(BannerStatus.DRAFT);
        banner.setPriority(100);
        banner.setVendorId(request.getVendorId());
        banner.setStartDate(request.getRequestedStartDate());
        banner.setEndDate(request.getRequestedEndDate());
        banner.setTotalImpressions(0L);
        banner.setTotalClicks(0L);
        banner.setCreatedAt(LocalDateTime.now());
        banner.setCreatedBy(adminId);
        return banner;
    }

    private String generateRequestCode() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyMM"));
        String codePrefix = "BR-" + yearMonth + "-";

        List<String> latestCodes = requestRepository.findLatestCodesByPrefix(codePrefix);

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
