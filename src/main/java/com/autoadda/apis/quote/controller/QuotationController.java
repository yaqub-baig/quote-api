package com.autoadda.apis.quote.controller;

import com.autoadda.apis.quote.exception.ResourceNotFoundException;
import com.autoadda.apis.quote.request.QuotationRequest;
import com.autoadda.apis.quote.response.QuotationResponse;
import com.autoadda.apis.quote.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @GetMapping
    public List<QuotationResponse> getAllQuotations() {
        return quotationService.getAllQuotations ( );
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationResponse> getQuotationById(@PathVariable String id) {
        return quotationService.getQuotationById ( id ).map ( ResponseEntity::ok ).orElseThrow ( () -> new ResourceNotFoundException ( "Quotation with id " + id + " not found" ) );
    }
    @GetMapping("/garage/{id}")
    public ResponseEntity<List<QuotationResponse>> getQuotationByGarageId(@PathVariable Integer id) {
        return ResponseEntity.ok ( quotationService.getQuotationByGarageId ( id  ));
    }

    @PostMapping
    public ResponseEntity<List<QuotationResponse>> createQuotation(@RequestBody QuotationRequest quotation) {
        return ResponseEntity.status ( HttpStatus.CREATED ).body ( quotationService.createQuotation ( quotation ) );
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuotationResponse> updateQuotation(@PathVariable String id, @RequestBody QuotationRequest updated) {
        return quotationService.updateQuotation ( id, updated ).map ( ResponseEntity::ok ).orElseThrow ( () -> new ResourceNotFoundException ( "Quotation with id " + id + " not found" ) );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuotation(@PathVariable String id) {
        if ( ! quotationService.deleteQuotation ( id ) ) {
            throw new ResourceNotFoundException ( "Quotation with id " + id + " not found" );
        }
        return ResponseEntity.noContent ( ).build ( );
    }
}