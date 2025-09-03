package com.autoadda.apis.quote.mapper;

import com.autoadda.apis.quote.config.AuthClient;
import com.autoadda.apis.quote.config.VehicleClient;
import com.autoadda.apis.quote.entity.Quotation;
import com.autoadda.apis.quote.request.QuotationRequest;
import com.autoadda.apis.quote.response.QuotationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuotationMapper {

    private final AuthClient authClient;
    private final VehicleClient vehicleClient;

    public Quotation toEntity(QuotationRequest quotation) {
        Quotation entity = new Quotation ( );
        entity.setId ( UUID.randomUUID ( ).toString ( ) );
        entity.setCustomerNotes ( quotation.getCustomerNotes ( ) );
        entity.setVehicleId ( quotation.getVehicleId ( ) );
        entity.setStatus ( "PENDING" );
        entity.setIssuedDate ( LocalDateTime.now ( ) );
        entity.setServiceId ( quotation.getServiceId ( ) );
        entity.setCustomerId ( quotation.getCustomerId ( ) );
        return entity;
    }

    public QuotationResponse toResponse(Quotation save) {
        QuotationResponse response = new QuotationResponse ( );
        response.setId ( save.getId ( ) );
        Optional.ofNullable ( save.getQuoteNumber ( ) ).ifPresent ( response::setQuoteNumber );
        Optional.ofNullable ( save.getEmail ( ) ).ifPresent ( response::setEmail );
        Optional.ofNullable ( save.getExpiryDate ( ) ).ifPresent ( response::setExpiryDate );
        Optional.ofNullable ( save.getCustomerNotes ( ) ).ifPresent ( response::setCustomerNotes );
        Optional.ofNullable ( save.getVehicleId ( ) ).ifPresent ( vehicleId -> response.setVehicle ( vehicleClient.getVehicleDetailsById ( vehicleId ) ) );
        Optional.ofNullable ( save.getGarageId ( ) ).ifPresent ( response::setGarageId );
        Optional.ofNullable ( save.getFullName ( ) ).ifPresent ( response::setFullName );
        Optional.ofNullable ( save.getPhoneNumber ( ) ).ifPresent ( response::setPhoneNumber );
        Optional.ofNullable ( save.getTechnicianNotes ( ) ).ifPresent ( response::setTechnicianNotes );
        Optional.ofNullable ( save.getStatus ( ) ).ifPresent ( response::setStatus );
        Optional.ofNullable ( save.getTotalAmount ( ) ).ifPresent ( response::setTotalAmount );
        Optional.ofNullable ( save.getIssuedDate ( ) ).ifPresent ( response::setIssuedDate );
        Optional.ofNullable ( save.getEmployeeId ( ) ).ifPresent ( response::setEmployeeId );
        Optional.ofNullable ( save.getServiceId ( ) ).ifPresent ( response::setServiceId );
        Optional.ofNullable ( save.getCustomerId ( ) ).ifPresent ( customerId -> response.setCustomer ( authClient.getUser ( customerId ) ) );
        Optional.ofNullable ( save.getOfferId ( ) ).ifPresent ( response::setOfferId );
        return response;
    }

    public void updateEntity(Quotation existing, QuotationRequest updated) {
        Optional.ofNullable ( updated.getCustomerNotes ( ) ).ifPresent ( existing::setCustomerNotes );
        Optional.ofNullable ( updated.getVehicleId ( ) ).ifPresent ( existing::setVehicleId );
        Optional.ofNullable ( updated.getServiceId ( ) ).ifPresent ( existing::setServiceId );
        Optional.ofNullable ( updated.getCustomerId ( ) ).ifPresent ( existing::setCustomerId );

    }
}