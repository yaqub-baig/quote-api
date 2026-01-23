package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.request.BannerScheduleRequest;
import com.autoadda.apis.quote.response.BannerScheduleResponse;

import java.util.List;
import java.util.Optional;

public interface BannerScheduleService {

    List<BannerScheduleResponse> getAllSchedules();

    List<BannerScheduleResponse> getSchedulesByBannerId(String bannerId);

    List<BannerScheduleResponse> getSchedulesByStatus(String status);

    Optional<BannerScheduleResponse> getScheduleById(String id);

    BannerScheduleResponse createSchedule(BannerScheduleRequest request);

    Optional<BannerScheduleResponse> updateSchedule(String id, BannerScheduleRequest request);

    boolean deleteSchedule(String id);

    void processScheduledBanners();
}
