package com.autoadda.apis.quote.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationRequest {

    private String customerNotes;

    private String vehicleId;

    private String serviceId;

    private String customerId;

    private List<Integer> garageIds;

}