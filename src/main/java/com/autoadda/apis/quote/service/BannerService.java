package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.request.BannerCreateRequest;
import com.autoadda.apis.quote.request.BannerUpdateRequest;
import com.autoadda.apis.quote.response.BannerResponse;
import com.autoadda.apis.quote.util.BannerStatus;

import java.util.List;
import java.util.Optional;

public interface BannerService {

    List<BannerResponse> getAllBanners();

    List<BannerResponse> getBannersByStatus(BannerStatus status);

    List<BannerResponse> getBannersByPageLocation(String pageLocation);

    List<BannerResponse> getBannersByVendorId(Integer vendorId);

    Optional<BannerResponse> getBannerById(String id);

    BannerResponse createBanner(BannerCreateRequest request, Integer createdBy);

    Optional<BannerResponse> updateBanner(String id, BannerUpdateRequest request);

    boolean deleteBanner(String id);

    Optional<BannerResponse> updateBannerStatus(String id, BannerStatus status);

    List<BannerResponse> getActiveBannersForDisplay();

    List<BannerResponse> getActiveBannersForPage(String pageLocation, Boolean isForMobile);

    List<BannerResponse> getActiveBannersForSlot(String pageLocation, String position);

    void trackImpression(String bannerId);

    void trackClick(String bannerId);
}
