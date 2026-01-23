package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.request.BannerCreateRequest;
import com.autoadda.apis.quote.request.BannerUpdateRequest;
import com.autoadda.apis.quote.response.BannerResponse;

import java.util.List;
import java.util.Optional;

public interface BannerService {

    List<BannerResponse> getAllBanners();

    List<BannerResponse> getBannersByStatus(String status);

    List<BannerResponse> getBannersByPageLocation(String pageLocation);

    List<BannerResponse> getBannersByVendorId(Integer vendorId);

    Optional<BannerResponse> getBannerById(String id);

    BannerResponse createBanner(BannerCreateRequest request, Integer createdBy);

    Optional<BannerResponse> updateBanner(String id, BannerUpdateRequest request);

    boolean deleteBanner(String id);

    Optional<BannerResponse> updateBannerStatus(String id, String status);

    List<BannerResponse> getActiveBannersForDisplay();

    List<BannerResponse> getActiveBannersForPage(String pageLocation);

    List<BannerResponse> getActiveBannersForSlot(String pageLocation, String position);

    void trackImpression(String bannerId);

    void trackClick(String bannerId);
}
