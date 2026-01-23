package com.autoadda.apis.quote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "banner_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerSchedule {
    @Id
    private String id;

    @Column(name = "banner_id", nullable = false)
    private String bannerId;

    @Column(name = "schedule_code", unique = true, nullable = false)
    private String scheduleCode;

    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @Column(name = "daily_start_time")
    private LocalTime dailyStartTime;

    @Column(name = "daily_end_time")
    private LocalTime dailyEndTime;

    @Column(name = "days_of_week")
    private String daysOfWeek;

    @Column(nullable = false)
    private String timezone;

    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;
}
