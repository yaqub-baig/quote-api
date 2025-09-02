package com.autoadda.apis.quote.service;


import com.autoadda.apis.quote.request.QuotationRequest;
import com.autoadda.apis.quote.response.QuotationResponse;

import java.util.List;
import java.util.Optional;

public interface QuotationService {
    List<QuotationResponse> getAllQuotations();

    Optional<QuotationResponse> getQuotationById(String id);

    Optional<QuotationResponse> getQuotationByQuoteNumber(String quoteNumber);

    List<QuotationResponse> createQuotation(QuotationRequest quotation);

    Optional<QuotationResponse> updateQuotation(String id, QuotationRequest updated);

    boolean deleteQuotation(String id);

    List<QuotationResponse> getQuotationByGarageId(Integer id);

}