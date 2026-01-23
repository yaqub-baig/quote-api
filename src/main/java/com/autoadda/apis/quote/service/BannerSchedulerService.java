package com.autoadda.apis.quote.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerSchedulerService {

    private final BannerScheduleService bannerScheduleService;

    @Scheduled(fixedRate = 300000)
    public void processScheduledBanners() {
        log.debug("Running banner schedule processor...");
        try {
            bannerScheduleService.processScheduledBanners();
            log.debug("Banner schedule processor completed successfully");
        } catch (Exception e) {
            log.error("Error processing scheduled banners", e);
        }
    }
}
