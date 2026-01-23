package com.autoadda.apis.quote.service;

import com.autoadda.apis.quote.request.BannerRequestSubmission;
import com.autoadda.apis.quote.response.BannerRequestResponse;
import com.autoadda.apis.quote.response.BannerResponse;

import java.util.List;
import java.util.Optional;

public interface BannerRequestService {

    List<BannerRequestResponse> getAllRequests();

    List<BannerRequestResponse> getRequestsByStatus(String status);

    List<BannerRequestResponse> getRequestsByVendorId(Integer vendorId);

    Optional<BannerRequestResponse> getRequestById(String id);

    BannerRequestResponse submitRequest(BannerRequestSubmission request, Integer vendorId);

    Optional<BannerRequestResponse> updateRequest(String id, BannerRequestSubmission request, Integer vendorId);

    boolean cancelRequest(String id, Integer vendorId);

    BannerResponse approveRequest(String id, Integer adminId, String adminNotes);

    BannerRequestResponse rejectRequest(String id, Integer adminId, String rejectionReason);
}
