package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.entity.Banner;
import com.autoadda.apis.quote.entity.BannerSchedule;
import com.autoadda.apis.quote.exception.BannerNotFoundException;
import com.autoadda.apis.quote.exception.BannerScheduleConflictException;
import com.autoadda.apis.quote.mapper.BannerScheduleMapper;
import com.autoadda.apis.quote.repository.BannerRepository;
import com.autoadda.apis.quote.repository.BannerScheduleRepository;
import com.autoadda.apis.quote.request.BannerScheduleRequest;
import com.autoadda.apis.quote.response.BannerScheduleResponse;
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
public class BannerScheduleServiceImpl implements BannerScheduleService {

    private final BannerScheduleRepository scheduleRepository;
    private final BannerRepository bannerRepository;
    private final BannerScheduleMapper scheduleMapper;

    @Override
    public List<BannerScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerScheduleResponse> getSchedulesByBannerId(String bannerId) {
        return scheduleRepository.findByBannerId(bannerId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    public List<BannerScheduleResponse> getSchedulesByStatus(String status) {
        return scheduleRepository.findByStatus(status)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }

    @Override
    public Optional<BannerScheduleResponse> getScheduleById(String id) {
        return scheduleRepository.findById(id)
                .map(scheduleMapper::toResponse);
    }

    @Override
    @Transactional
    public BannerScheduleResponse createSchedule(BannerScheduleRequest request) {
        if (!bannerRepository.existsById(request.getBannerId())) {
            throw new BannerNotFoundException("Banner not found: " + request.getBannerId());
        }

        List<BannerSchedule> overlapping = scheduleRepository.findOverlappingSchedules(
                request.getBannerId(), request.getStartDate(), request.getEndDate());

        if (!overlapping.isEmpty()) {
            throw new BannerScheduleConflictException("Schedule overlaps with existing schedule");
        }

        BannerSchedule schedule = scheduleMapper.toEntity(request);
        schedule.setScheduleCode(generateScheduleCode());
        BannerSchedule saved = scheduleRepository.save(schedule);
        log.info("Created schedule {} for banner {}", saved.getScheduleCode(), request.getBannerId());
        return scheduleMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public Optional<BannerScheduleResponse> updateSchedule(String id, BannerScheduleRequest request) {
        return scheduleRepository.findById(id)
                .map(existing -> {
                    if (request.getStartDate() != null && request.getEndDate() != null) {
                        List<BannerSchedule> overlapping = scheduleRepository.findOverlappingSchedules(
                                existing.getBannerId(), request.getStartDate(), request.getEndDate());
                        overlapping.removeIf(s -> s.getId().equals(id));
                        if (!overlapping.isEmpty()) {
                            throw new BannerScheduleConflictException("Schedule overlaps with existing schedule");
                        }
                    }
                    scheduleMapper.updateEntity(existing, request);
                    BannerSchedule saved = scheduleRepository.save(existing);
                    log.info("Updated schedule: {}", id);
                    return scheduleMapper.toResponse(saved);
                });
    }

    @Override
    @Transactional
    public boolean deleteSchedule(String id) {
        if (!scheduleRepository.existsById(id)) {
            return false;
        }
        scheduleRepository.deleteById(id);
        log.info("Deleted schedule: {}", id);
        return true;
    }

    @Override
    @Transactional
    public void processScheduledBanners() {
        LocalDateTime now = LocalDateTime.now();

        List<BannerSchedule> toActivate = scheduleRepository.findSchedulesToActivate(now);
        for (BannerSchedule schedule : toActivate) {
            schedule.setStatus("ACTIVE");
            schedule.setUpdatedAt(now);
            scheduleRepository.save(schedule);

            bannerRepository.findById(schedule.getBannerId()).ifPresent(banner -> {
                if (BannerStatus.DRAFT.name().equals(banner.getStatus()) ||
                    BannerStatus.PAUSED.name().equals(banner.getStatus())) {
                    banner.setStatus(BannerStatus.ACTIVE);
                    banner.setUpdatedAt(now);
                    bannerRepository.save(banner);
                    log.info("Activated banner {} via schedule {}", banner.getBannerCode(), schedule.getScheduleCode());
                }
            });
        }

        List<BannerSchedule> toComplete = scheduleRepository.findSchedulesToComplete(now);
        for (BannerSchedule schedule : toComplete) {
            schedule.setStatus("COMPLETED");
            schedule.setUpdatedAt(now);
            scheduleRepository.save(schedule);

            bannerRepository.findById(schedule.getBannerId()).ifPresent(banner -> {
                List<BannerSchedule> activeSchedules = scheduleRepository.findByBannerIdAndStatus(banner.getId(), "ACTIVE");
                if (activeSchedules.isEmpty()) {
                    banner.setStatus(BannerStatus.EXPIRED);
                    banner.setUpdatedAt(now);
                    bannerRepository.save(banner);
                    log.info("Expired banner {} after schedule {} completed", banner.getBannerCode(), schedule.getScheduleCode());
                }
            });
        }

        List<Banner> expiredBanners = bannerRepository.findExpiredActiveBanners(now);
        for (Banner banner : expiredBanners) {
            banner.setStatus(BannerStatus.EXPIRED);
            banner.setUpdatedAt(now);
            bannerRepository.save(banner);
            log.info("Expired banner {} due to end date", banner.getBannerCode());
        }
    }

    private String generateScheduleCode() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyMM"));
        String codePrefix = "BS-" + yearMonth + "-";

        List<String> latestCodes = scheduleRepository.findLatestCodesByPrefix(codePrefix);

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
