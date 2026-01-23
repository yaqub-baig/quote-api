package com.autoadda.apis.quote.controller;

import com.autoadda.apis.quote.exception.BannerRequestNotFoundException;
import com.autoadda.apis.quote.request.BannerRequestSubmission;
import com.autoadda.apis.quote.response.BannerAnalyticsResponse;
import com.autoadda.apis.quote.response.BannerPricingResponse;
import com.autoadda.apis.quote.response.BannerRequestResponse;
import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.service.BannerAnalyticsService;
import com.autoadda.apis.quote.service.BannerPricingService;
import com.autoadda.apis.quote.service.BannerRequestService;
import com.autoadda.apis.quote.service.BannerService;
import com.autoadda.apis.quote.util.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/vendor/banners")
@RequiredArgsConstructor
public class BannerVendorController {

    private final BannerService bannerService;
    private final BannerRequestService bannerRequestService;
    private final BannerPricingService bannerPricingService;
    private final BannerAnalyticsService bannerAnalyticsService;

    @PostMapping("/requests")
    public ResponseEntity<BannerRequestResponse> submitRequest(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @RequestBody BannerRequestSubmission submission) {
        AuthorizationUtil.requireVendor(userRole);
        BannerRequestResponse response = bannerRequestService.submitRequest(submission, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<BannerRequestResponse>> getMyRequests(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole) {
        AuthorizationUtil.requireVendor(userRole);
        List<BannerRequestResponse> requests = bannerRequestService.getRequestsByVendorId(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<BannerRequestResponse> getRequestById(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id) {
        AuthorizationUtil.requireVendor(userRole);
        BannerRequestResponse response = bannerRequestService.getRequestById(id)
                .orElseThrow(() -> new BannerRequestNotFoundException("Request not found: " + id));

        if (!response.getVendorId().equals(userId) && !"ADMIN".equals(userRole)) {
            throw new BannerRequestNotFoundException("Request not found: " + id);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/requests/{id}")
    public ResponseEntity<BannerRequestResponse> updateRequest(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestBody BannerRequestSubmission submission) {
        AuthorizationUtil.requireVendor(userRole);
        return bannerRequestService.updateRequest(id, submission, userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new BannerRequestNotFoundException("Request not found: " + id));
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> cancelRequest(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id) {
        AuthorizationUtil.requireVendor(userRole);
        if (!bannerRequestService.cancelRequest(id, userId)) {
            throw new BannerRequestNotFoundException("Request not found: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-banners")
    public ResponseEntity<List<BannerResponse>> getMyBanners(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole) {
        AuthorizationUtil.requireVendor(userRole);
        List<BannerResponse> banners = bannerService.getBannersByVendorId(userId);
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/my-banners/{id}/analytics")
    public ResponseEntity<List<BannerAnalyticsResponse>> getMyBannerAnalytics(
            @RequestHeader("X-Auto-Adda-Id") Integer userId,
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String id,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        AuthorizationUtil.requireVendor(userRole);

        BannerResponse banner = bannerService.getBannerById(id)
                .orElseThrow(() -> new BannerRequestNotFoundException("Banner not found: " + id));

        if (!banner.getVendorId().equals(userId) && !"ADMIN".equals(userRole)) {
            throw new BannerRequestNotFoundException("Banner not found: " + id);
        }

        List<BannerAnalyticsResponse> analytics;
        if (startDate != null && endDate != null) {
            analytics = bannerAnalyticsService.getAnalyticsByBannerIdAndDateRange(id, startDate, endDate);
        } else {
            analytics = bannerAnalyticsService.getAnalyticsByBannerId(id);
        }
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/pricing/estimate")
    public ResponseEntity<Map<String, Object>> getPricingEstimate(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam String pageLocation,
            @RequestParam String position,
            @RequestParam String pricingType,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        AuthorizationUtil.requireVendor(userRole);
        BigDecimal estimate = bannerPricingService.calculateEstimate(pageLocation, position, pricingType, startDate, endDate);
        return ResponseEntity.ok(Map.of(
                "pageLocation", pageLocation,
                "position", position,
                "pricingType", pricingType,
                "startDate", startDate,
                "endDate", endDate,
                "estimatedCost", estimate,
                "currency", "INR"
        ));
    }

    @GetMapping("/pricing")
    public ResponseEntity<List<BannerPricingResponse>> getActivePricing(
            @RequestHeader("X-User-Role") String userRole,
            @RequestParam(required = false) String pageLocation,
            @RequestParam(required = false) String position) {
        AuthorizationUtil.requireVendor(userRole);
        List<BannerPricingResponse> pricing;
        if (pageLocation != null && position != null) {
            pricing = bannerPricingService.getActivePricingForSlot(pageLocation, position);
        } else {
            pricing = bannerPricingService.getActiveTemplatePricing();
        }
        return ResponseEntity.ok(pricing);
    }
}
