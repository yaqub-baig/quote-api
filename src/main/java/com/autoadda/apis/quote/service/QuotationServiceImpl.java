package com.autoadda.apis.quote.service;


import com.autoadda.apis.quote.entity.Quotation;
import com.autoadda.apis.quote.mapper.QuotationMapper;
import com.autoadda.apis.quote.repository.QuotationRepository;
import com.autoadda.apis.quote.request.QuotationRequest;
import com.autoadda.apis.quote.response.QuotationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationMapper quotationMapper;

    @Override
    public List<QuotationResponse> getAllQuotations() {
        return quotationRepository.findAll ( )
                .stream ( )
                .sorted ( (q1, q2) -> q2.getIssuedDate ( ).compareTo ( q1.getIssuedDate ( ) ) )
                .map ( quotationMapper::toResponse )
                .toList ( );
    }

    @Override
    public Optional<QuotationResponse> getQuotationById(String id) {
        return quotationRepository.findById ( id )
                .map ( quotationMapper::toResponse );
    }

    @Override
    public Optional<QuotationResponse> getQuotationByQuoteNumber(String quoteNumber) {
        return quotationRepository.findByQuoteNumber ( quoteNumber )
                .map ( quotationMapper::toResponse );
    }

    @Override
    public List<QuotationResponse> createQuotation(QuotationRequest request) {
        List<Quotation> list = request.getGarageIds ( ).stream ( ).map ( garageId -> {
            Quotation quotation = quotationMapper.toEntity ( request );
            quotation.setGarageId ( garageId );
            quotation.setQuoteNumber ( generateQuotationCode ( ) );
            return quotationRepository.save ( quotation );
        } ).toList ( );
        return list.stream ( ).map ( quotationMapper::toResponse ).toList ( );
    }

    @Override
    public Optional<QuotationResponse> updateQuotation(String id, QuotationRequest updated) {
        return quotationRepository.findById ( id )
                .map ( existing -> {
                    quotationMapper.updateEntity ( existing, updated );
                    Quotation save = quotationRepository.save ( existing );
                    return quotationMapper.toResponse ( save );
                } );
    }

    @Override
    public boolean deleteQuotation(String id) {
        if ( ! quotationRepository.existsById ( id ) ) {
            return false;
        }
        quotationRepository.deleteById ( id );
        return true;
    }

    @Override
    public List<QuotationResponse> getQuotationByGarageId(Integer id) {
        return quotationRepository.findByGarageId ( id ).stream ( )
                .map ( quotationMapper::toResponse ).toList ( );
    }

    private String generateQuotationCode() {
        // Get current year and month
        LocalDateTime now = LocalDateTime.now ( );
        String yearMonth = now.format ( DateTimeFormatter.ofPattern ( "yyMM" ) );

        // Format: RQ-YYMM-XXXX
        String codePrefix = "RQ-" + yearMonth + "-";

        // Find the latest code for this prefix
        List<String> latestCodes = quotationRepository.findLatestCodesByPrefix (
                codePrefix );

        int nextSequence = 1;
        if ( ! latestCodes.isEmpty ( ) ) {
            String latestCode = latestCodes.get ( 0 );
            try {
                // Extract the sequence number from the latest code
                String sequencePart = latestCode.substring ( codePrefix.length ( ) );
                nextSequence = Integer.parseInt ( sequencePart ) + 1;
            }
            catch ( NumberFormatException | IndexOutOfBoundsException e ) {
                log.warn ( "Failed to parse sequence number, starting from 1", e );
            }
        }

        // Format the final code with the sequence padded to 4 digits
        return String.format ( "%s%06d", codePrefix, nextSequence );
    }
}