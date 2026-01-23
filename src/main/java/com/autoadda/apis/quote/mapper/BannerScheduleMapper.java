package com.autoadda.apis.quote.mapper;

import com.autoadda.apis.quote.entity.BannerSchedule;
import com.autoadda.apis.quote.request.BannerScheduleRequest;
import com.autoadda.apis.quote.response.BannerScheduleResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BannerScheduleMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BannerSchedule toEntity(BannerScheduleRequest request) {
        BannerSchedule schedule = new BannerSchedule();
        schedule.setId(UUID.randomUUID().toString());
        schedule.setBannerId(request.getBannerId());
        schedule.setStartDate(request.getStartDate());
        schedule.setEndDate(request.getEndDate());
        schedule.setDailyStartTime(request.getDailyStartTime());
        schedule.setDailyEndTime(request.getDailyEndTime());
        schedule.setDaysOfWeek(convertDaysToJson(request.getDaysOfWeek()));
        schedule.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Kolkata");
        schedule.setIsRecurring(request.getIsRecurring() != null ? request.getIsRecurring() : false);
        schedule.setStatus("SCHEDULED");
        schedule.setCreatedAt(LocalDateTime.now());
        return schedule;
    }

    public BannerScheduleResponse toResponse(BannerSchedule schedule) {
        BannerScheduleResponse response = new BannerScheduleResponse();
        response.setId(schedule.getId());
        response.setBannerId(schedule.getBannerId());
        response.setScheduleCode(schedule.getScheduleCode());
        response.setStartDate(schedule.getStartDate());
        response.setEndDate(schedule.getEndDate());
        response.setDailyStartTime(schedule.getDailyStartTime());
        response.setDailyEndTime(schedule.getDailyEndTime());
        response.setDaysOfWeek(convertJsonToDays(schedule.getDaysOfWeek()));
        response.setTimezone(schedule.getTimezone());
        response.setIsRecurring(schedule.getIsRecurring());
        response.setStatus(schedule.getStatus());
        response.setCreatedAt(schedule.getCreatedAt());
        response.setUpdatedAt(schedule.getUpdatedAt());
        return response;
    }

    public void updateEntity(BannerSchedule existing, BannerScheduleRequest request) {
        Optional.ofNullable(request.getStartDate()).ifPresent(existing::setStartDate);
        Optional.ofNullable(request.getEndDate()).ifPresent(existing::setEndDate);
        Optional.ofNullable(request.getDailyStartTime()).ifPresent(existing::setDailyStartTime);
        Optional.ofNullable(request.getDailyEndTime()).ifPresent(existing::setDailyEndTime);
        Optional.ofNullable(request.getDaysOfWeek()).ifPresent(days -> existing.setDaysOfWeek(convertDaysToJson(days)));
        Optional.ofNullable(request.getTimezone()).ifPresent(existing::setTimezone);
        Optional.ofNullable(request.getIsRecurring()).ifPresent(existing::setIsRecurring);
        existing.setUpdatedAt(LocalDateTime.now());
    }

    private String convertDaysToJson(List<String> days) {
        if (days == null || days.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(days);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private List<String> convertJsonToDays(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}
