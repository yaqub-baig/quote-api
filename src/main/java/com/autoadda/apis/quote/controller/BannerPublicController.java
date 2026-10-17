package com.autoadda.apis.quote.controller;

import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.service.BannerService;
import com.autoadda.apis.quote.service.BannerTrackingThrottle;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerPublicController {

    private final BannerService bannerService;
    private final BannerTrackingThrottle trackingThrottle;

    @GetMapping
    public ResponseEntity<List<BannerResponse>> getActiveBanners() {
        List<BannerResponse> banners = bannerService.getActiveBannersForDisplay();
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/page/{pageLocation}")
    public ResponseEntity<List<BannerResponse>> getBannersForPage(@PathVariable String pageLocation,@RequestParam(required = false) Boolean isForMobile) {
        List<BannerResponse> banners = bannerService.getActiveBannersForPage(pageLocation,isForMobile);
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/page/{pageLocation}/position/{position}")
    public ResponseEntity<List<BannerResponse>> getBannersForSlot(
            @PathVariable String pageLocation,
            @PathVariable String position) {
        List<BannerResponse> banners = bannerService.getActiveBannersForSlot(pageLocation, position);
        return ResponseEntity.ok(banners);
    }

    @PostMapping("/{id}/impression")
    public ResponseEntity<Map<String, String>> trackImpression(@PathVariable String id, HttpServletRequest request) {
        if (!trackingThrottle.shouldCount("impression", id, clientIp(request))) {
            return ResponseEntity.ok(Map.of("status", "skipped"));
        }
        bannerService.trackImpression(id);
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }

    @PostMapping("/{id}/click")
    public ResponseEntity<Map<String, String>> trackClick(@PathVariable String id, HttpServletRequest request) {
        if (!trackingThrottle.shouldCount("click", id, clientIp(request))) {
            return ResponseEntity.ok(Map.of("status", "skipped"));
        }
        bannerService.trackClick(id);
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }

    /**
     * The client address as Apache saw it. Apache appends the real peer address to any
     * X-Forwarded-For the client sent (and the gateway passes it on unchanged), so the last
     * entry is the one a client cannot forge.
     */
    static String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            String[] parts = xff.split(",");
            String last = parts[parts.length - 1].trim();
            if (!last.isEmpty()) {
                return last;
            }
        }
        return request.getRemoteAddr();
    }
}
