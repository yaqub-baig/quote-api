package com.autoadda.apis.quote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "quotation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {
    @Id
    private String id;

    @Column(name = "quote_number", unique = true, nullable = false)
    private String quoteNumber;

    @Column(nullable = false)
    private String status;

    @Column(name = "issued_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime issuedDate;

    @Column(name = "expiry_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiryDate;

    @Column(name = "total_amount")
    private Float totalAmount;

    @Column(name = "customer_notes")
    private String customerNotes;

    @Column(name = "technician_notes")
    private String technicianNotes;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "garage_id")
    private Integer garageId;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "offer_id")
    private String offerId;
}