package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationResponse {
    private String id;
    private String quoteNumber;
    private String status;
    private LocalDateTime issuedDate;
    private LocalDateTime expiryDate;
    private Float totalAmount;
    private String customerNotes;
    private String technicianNotes;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String vehicleId;
    private String employeeId;
    private String serviceId;
    private String garageId;
    private String customerId;
    private String offerId;
}