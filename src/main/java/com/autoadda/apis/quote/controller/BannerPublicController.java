package com.autoadda.apis.quote.controller;

import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.service.BannerService;
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

    @GetMapping
    public ResponseEntity<List<BannerResponse>> getActiveBanners() {
        List<BannerResponse> banners = bannerService.getActiveBannersForDisplay();
        return ResponseEntity.ok(banners);
    }

    @GetMapping("/page/{pageLocation}")
    public ResponseEntity<List<BannerResponse>> getBannersForPage(@PathVariable String pageLocation) {
        List<BannerResponse> banners = bannerService.getActiveBannersForPage(pageLocation);
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
    public ResponseEntity<Map<String, String>> trackImpression(@PathVariable String id) {
        bannerService.trackImpression(id);
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }

    @PostMapping("/{id}/click")
    public ResponseEntity<Map<String, String>> trackClick(@PathVariable String id) {
        bannerService.trackClick(id);
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }
}
