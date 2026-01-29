package com.autoadda.apis.quote.controller;

import com.autoadda.apis.quote.exception.BannerNotFoundException;
import com.autoadda.apis.quote.exception.ResourceNotFoundException;
import com.autoadda.apis.quote.request.BannerCreateRequest;
import com.autoadda.apis.quote.request.BannerPricingRequest;
import com.autoadda.apis.quote.request.BannerScheduleRequest;
import com.autoadda.apis.quote.request.BannerUpdateRequest;
import com.autoadda.apis.quote.response.*;
import com.autoadda.apis.quote.service.*;
import com.autoadda.apis.quote.util.AuthorizationUtil;
import com.autoadda.apis.quote.util.BannerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class BannerAdminController {

    public static final String BANNER_NOT_FOUND = "Banner not found: ";
    private final BannerService bannerService;
    private final BannerRequestService bannerRequestService;
    private final BannerScheduleService bannerScheduleService;
    private final BannerPricingService bannerPricingService;
    private final BannerAnalyticsService bannerAnalyticsService;

    @GetMapping
    public ResponseEntity<List<BannerResponse>> getAllBanners(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) BannerStatus status,
            @RequestParam(required = false) String pageLocation) {
        AuthorizationUtil.requireAdmin(userRole);

        List<BannerResponse> banners;
        if (status != null) {
            banners = bannerService.getBannersByStatus(status);
        } else if (pageLocation != null) {
            banners = bannerService.getBannersByPageLocation(pageLocation);
        } else {
            banners = bannerService.getAllBanners();
        }
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BannerResponse> getBannerById(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id) {
        AuthorizationUtil.requireAdmin(userRole);
        return bannerService.getBannerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BannerNotFoundException(BANNER_NOT_FOUND + id));
    }

    @PostMapping
    public ResponseEntity<BannerResponse> createBanner(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @RequestBody BannerCreateRequest request) {
        AuthorizationUtil.requireAdmin(userRole);
        BannerResponse banner = bannerService.createBanner(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(banner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BannerResponse> updateBanner(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestBody BannerUpdateRequest request) {
        AuthorizationUtil.requireAdmin(userRole);
        return bannerService.updateBanner(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BannerNotFoundException(BANNER_NOT_FOUND + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id) {
        AuthorizationUtil.requireAdmin(userRole);
        if (!bannerService.deleteBanner(id)) {
            throw new BannerNotFoundException(BANNER_NOT_FOUND + id);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BannerResponse> updateBannerStatus(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestParam BannerStatus status) {

        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }
        return bannerService.updateBannerStatus(id, status)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BannerNotFoundException(BANNER_NOT_FOUND + id));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<BannerRequestResponse>> getAllRequests(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) String status) {
        AuthorizationUtil.requireAdmin(userRole);
        List<BannerRequestResponse> requests;
        if (status != null) {
            requests = bannerRequestService.getRequestsByStatus(status);
        } else {
            requests = bannerRequestService.getAllRequests();
        }
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/{id}/approve")
    public ResponseEntity<BannerResponse> approveRequest(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestBody(required = false) Map<String, String> body) {
        AuthorizationUtil.requireAdmin(userRole);
        String adminNotes = body != null ? body.get("adminNotes") : null;
        BannerResponse banner = bannerRequestService.approveRequest(id, userId, adminNotes);
        return ResponseEntity.ok(banner);
    }

    @PostMapping("/requests/{id}/reject")
    public ResponseEntity<BannerRequestResponse> rejectRequest(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        AuthorizationUtil.requireAdmin(userRole);
        String rejectionReason = body.get("rejectionReason");
        BannerRequestResponse response = bannerRequestService.rejectRequest(id, userId, rejectionReason);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<BannerScheduleResponse>> getAllSchedules(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) String bannerId,
            @RequestParam(required = false) String status) {
        AuthorizationUtil.requireAdmin(userRole);
        List<BannerScheduleResponse> schedules;
        if (bannerId != null) {
            schedules = bannerScheduleService.getSchedulesByBannerId(bannerId);
        } else if (status != null) {
            schedules = bannerScheduleService.getSchedulesByStatus(status);
        } else {
            schedules = bannerScheduleService.getAllSchedules();
        }
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/schedules")
    public ResponseEntity<BannerScheduleResponse> createSchedule(
            @RequestHeader("X-User-Role") String userRole,
            @RequestBody BannerScheduleRequest request) {
        AuthorizationUtil.requireAdmin(userRole);
        BannerScheduleResponse schedule = bannerScheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<BannerScheduleResponse> updateSchedule(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestBody BannerScheduleRequest request) {
        AuthorizationUtil.requireAdmin(userRole);
        return bannerScheduleService.updateSchedule(id, request)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found: " + id));
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id) {
        AuthorizationUtil.requireAdmin(userRole);
        if (!bannerScheduleService.deleteSchedule(id)) {
            throw new ResourceNotFoundException("Schedule not found: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pricing")
    public ResponseEntity<List<BannerPricingResponse>> getAllPricing(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) String pageLocation) {
        AuthorizationUtil.requireAdmin(userRole);
        List<BannerPricingResponse> pricing;
        if (pageLocation != null) {
            pricing = bannerPricingService.getPricingByPageLocation(pageLocation);
        } else {
            pricing = bannerPricingService.getAllPricing();
        }
        return ResponseEntity.ok(pricing);
    }

    @PostMapping("/pricing")
    public ResponseEntity<BannerPricingResponse> createPricing(
            @RequestHeader("X-User-Role") String userRole,
            @RequestBody BannerPricingRequest request) {
        AuthorizationUtil.requireAdmin(userRole);
        BannerPricingResponse pricing = bannerPricingService.createPricing(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pricing);
    }

    @PutMapping("/pricing/{id}")
    public ResponseEntity<BannerPricingResponse> updatePricing(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestBody BannerPricingRequest request) {
        AuthorizationUtil.requireAdmin(userRole);
        return bannerPricingService.updatePricing(id, request)  
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing not found: " + id));
    }

    @GetMapping("/analytics")
    public ResponseEntity<List<BannerAnalyticsResponse>> getAnalytics(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam String bannerId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        AuthorizationUtil.requireAdmin(userRole);
        List<BannerAnalyticsResponse> analytics;
        if (startDate != null && endDate != null) {
            analytics = bannerAnalyticsService.getAnalyticsByBannerIdAndDateRange(bannerId, startDate, endDate);
        } else {
            analytics = bannerAnalyticsService.getAnalyticsByBannerId(bannerId);
        }
        return ResponseEntity.ok(analytics);
    }
}
