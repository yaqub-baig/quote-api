package com.autoadda.apis.quote.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerScheduleRequest {
    private String bannerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalTime dailyStartTime;
    private LocalTime dailyEndTime;
    private List<String> daysOfWeek;
    private String timezone;
    private Boolean isRecurring;
}
