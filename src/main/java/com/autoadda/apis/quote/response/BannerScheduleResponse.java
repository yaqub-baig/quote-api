package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerScheduleResponse {
    private String id;
    private String bannerId;
    private String scheduleCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime dailyStartTime;
    private LocalTime dailyEndTime;
    private List<String> daysOfWeek;
    private String timezone;
    private Boolean isRecurring;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
