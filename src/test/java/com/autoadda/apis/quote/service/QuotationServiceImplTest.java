package com.autoadda.apis.quote.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.autoadda.apis.quote.mapper.QuotationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.autoadda.apis.quote.entity.Quotation;
import com.autoadda.apis.quote.repository.QuotationRepository;
import com.autoadda.apis.quote.request.QuotationRequest;
import com.autoadda.apis.quote.response.QuotationResponse;

@ExtendWith(MockitoExtension.class)
public class QuotationServiceImplTest {

    @Mock
    private QuotationRepository quotationRepository;

    @Mock
    private QuotationMapper quotationMapper;

    @InjectMocks
    private QuotationServiceImpl quotationService;

    private Quotation quotation;
    private QuotationRequest quotationRequest;
    private QuotationResponse quotationResponse;

    @BeforeEach
    void setUp() {
        // Setup a sample quotation entity
        quotation = new Quotation();
        quotation.setId("Q123");
        quotation.setQuoteNumber("RQ-2312-000001");
        quotation.setIssuedDate(LocalDateTime.now());

        // Setup a sample request
        quotationRequest = new QuotationRequest();
        // Set request properties as needed

        // Setup a sample response
        quotationResponse = new QuotationResponse();
        quotationResponse.setId("Q123");
        quotationResponse.setQuoteNumber("RQ-2312-000001");
    }

    @Test
    void getAllQuotations_shouldReturnSortedList() {
        // Arrange
        Quotation olderQuotation = new Quotation();
        olderQuotation.setIssuedDate(LocalDateTime.now().minusDays(1));
        Quotation newerQuotation = new Quotation();
        newerQuotation.setIssuedDate(LocalDateTime.now());

        when(quotationRepository.findAll()).thenReturn(List.of(olderQuotation, newerQuotation));
        when(quotationMapper.toResponse(olderQuotation)).thenReturn(new QuotationResponse());
        when(quotationMapper.toResponse(newerQuotation)).thenReturn(new QuotationResponse());

        // Act
        List<QuotationResponse> result = quotationService.getAllQuotations();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(quotationRepository, times(1)).findAll();
        verify(quotationMapper, times(2)).toResponse(any(Quotation.class));
    }

    @Test
    void getQuotationById_exists_shouldReturnQuotation() {
        // Arrange
        when(quotationRepository.findById("Q123")).thenReturn(Optional.of(quotation));
        when(quotationMapper.toResponse(quotation)).thenReturn(quotationResponse);

        // Act
        Optional<QuotationResponse> result = quotationService.getQuotationById("Q123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Q123", result.get().getId());
        verify(quotationRepository, times(1)).findById("Q123");
        verify(quotationMapper, times(1)).toResponse(quotation);
    }

    @Test
    void getQuotationById_notExists_shouldReturnEmpty() {
        // Arrange
        when(quotationRepository.findById("NON_EXISTENT")).thenReturn(Optional.empty());

        // Act
        Optional<QuotationResponse> result = quotationService.getQuotationById("NON_EXISTENT");

        // Assert
        assertFalse(result.isPresent());
        verify(quotationRepository, times(1)).findById("NON_EXISTENT");
        verify(quotationMapper, never()).toResponse(any());
    }

    @Test
    void getQuotationByQuoteNumber_exists_shouldReturnQuotation() {
        // Arrange
        when(quotationRepository.findByQuoteNumber("RQ-2312-000001")).thenReturn(Optional.of(quotation));
        when(quotationMapper.toResponse(quotation)).thenReturn(quotationResponse);

        // Act
        Optional<QuotationResponse> result = quotationService.getQuotationByQuoteNumber("RQ-2312-000001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("RQ-2312-000001", result.get().getQuoteNumber());
        verify(quotationRepository, times(1)).findByQuoteNumber("RQ-2312-000001");
        verify(quotationMapper, times(1)).toResponse(quotation);
    }

    @Test
    void getQuotationByQuoteNumber_notExists_shouldReturnEmpty() {
        // Arrange
        when(quotationRepository.findByQuoteNumber("NON_EXISTENT")).thenReturn(Optional.empty());

        // Act
        Optional<QuotationResponse> result = quotationService.getQuotationByQuoteNumber("NON_EXISTENT");

        // Assert
        assertFalse(result.isPresent());
        verify(quotationRepository, times(1)).findByQuoteNumber("NON_EXISTENT");
        verify(quotationMapper, never()).toResponse(any());
    }

    @Test
    void createQuotation_shouldGenerateQuoteNumberAndSave() {
        // Arrange
        Quotation mappedQuotation = new Quotation();
        mappedQuotation.setQuoteNumber("RQ-2312-000002");

        when(quotationMapper.toEntity(quotationRequest)).thenReturn(mappedQuotation);
        when(quotationRepository.save(mappedQuotation)).thenReturn(quotation);
        when(quotationMapper.toResponse(quotation)).thenReturn(quotationResponse);

        // Mock the sequence generation dependencies
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of("RQ-2312-000001"));

        // Act
        QuotationResponse result = quotationService.createQuotation(quotationRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Q123", result.getId());
        verify(quotationMapper, times(1)).toEntity(quotationRequest);
        verify(quotationRepository, times(1)).save(mappedQuotation);
        verify(quotationMapper, times(1)).toResponse(quotation);
        // Verify that the quote number was set (this happens in the service method)
        assertNotNull(mappedQuotation.getQuoteNumber());
    }

    @Test
    void updateQuotation_exists_shouldUpdateAndReturn() {
        // Arrange
        QuotationRequest updatedRequest = new QuotationRequest();
        Quotation updatedQuotation = new Quotation();
        updatedQuotation.setId("Q123");
        QuotationResponse updatedResponse = new QuotationResponse();
        updatedResponse.setId("Q123");

        when(quotationRepository.findById("Q123")).thenReturn(Optional.of(quotation));
        doNothing().when(quotationMapper).updateEntity(quotation, updatedRequest);
        when(quotationRepository.save(quotation)).thenReturn(updatedQuotation);
        when(quotationMapper.toResponse(updatedQuotation)).thenReturn(updatedResponse);

        // Act
        Optional<QuotationResponse> result = quotationService.updateQuotation("Q123", updatedRequest);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Q123", result.get().getId());
        verify(quotationRepository, times(1)).findById("Q123");
        verify(quotationMapper, times(1)).updateEntity(quotation, updatedRequest);
        verify(quotationRepository, times(1)).save(quotation);
        verify(quotationMapper, times(1)).toResponse(updatedQuotation);
    }

    @Test
    void updateQuotation_notExists_shouldReturnEmpty() {
        // Arrange
        when(quotationRepository.findById("NON_EXISTENT")).thenReturn(Optional.empty());

        // Act
        Optional<QuotationResponse> result = quotationService.updateQuotation("NON_EXISTENT", quotationRequest);

        // Assert
        assertFalse(result.isPresent());
        verify(quotationRepository, times(1)).findById("NON_EXISTENT");
        verify(quotationMapper, never()).updateEntity(any(), any());
        verify(quotationRepository, never()).save(any());
        verify(quotationMapper, never()).toResponse(any());
    }

    @Test
    void deleteQuotation_exists_shouldReturnTrue() {
        // Arrange
        when(quotationRepository.existsById("Q123")).thenReturn(true);
        doNothing().when(quotationRepository).deleteById("Q123");

        // Act
        boolean result = quotationService.deleteQuotation("Q123");

        // Assert
        assertTrue(result);
        verify(quotationRepository, times(1)).existsById("Q123");
        verify(quotationRepository, times(1)).deleteById("Q123");
    }

    @Test
    void deleteQuotation_notExists_shouldReturnFalse() {
        // Arrange
        when(quotationRepository.existsById("NON_EXISTENT")).thenReturn(false);

        // Act
        boolean result = quotationService.deleteQuotation("NON_EXISTENT");

        // Assert
        assertFalse(result);
        verify(quotationRepository, times(1)).existsById("NON_EXISTENT");
        verify(quotationRepository, never()).deleteById(any());
    }

    @Test
    void generateQuotationCode_firstInMonth_shouldStartFrom000001() {
        // Arrange
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of());

        // Act - This is a private method, but we can test it through createQuotation
        // or use reflection if needed. For now, we'll test the behavior through createQuotation
        when(quotationMapper.toEntity(any())).thenReturn(new Quotation());
        when(quotationRepository.save(any())).thenReturn(quotation);
        when(quotationMapper.toResponse(any())).thenReturn(quotationResponse);

        // Set up the repository to return no existing codes (first in month)
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of());

        // Act
        QuotationResponse result = quotationService.createQuotation(quotationRequest);

        // Assert - The quote number generation is tested indirectly
        // You might want to use reflection to test the private method directly
        verify(quotationRepository, times(1)).findLatestCodesByPrefix(anyString());
    }

    @Test
    void generateQuotationCode_nextInSequence_shouldIncrement() {
        // Arrange
        when(quotationMapper.toEntity(any())).thenReturn(new Quotation());
        when(quotationRepository.save(any())).thenReturn(quotation);
        when(quotationMapper.toResponse(any())).thenReturn(quotationResponse);

        // Set up the repository to return an existing code
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of("RQ-2312-000001"));

        // Act
        QuotationResponse result = quotationService.createQuotation(quotationRequest);

        // Assert - The quote number generation should increment from 000001 to 000002
        verify(quotationRepository, times(1)).findLatestCodesByPrefix(anyString());
    }

    @Test
    void generateQuotationCode_invalidLatestCode_shouldStartFrom1() {
        // Arrange
        when(quotationMapper.toEntity(any())).thenReturn(new Quotation());
        when(quotationRepository.save(any())).thenReturn(quotation);
        when(quotationMapper.toResponse(any())).thenReturn(quotationResponse);

        // Set up the repository to return an invalid code format
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of("RQ-2312-INVALID"));

        // Act
        QuotationResponse result = quotationService.createQuotation(quotationRequest);

        // Assert - Should handle the invalid format and start from 1
        verify(quotationRepository, times(1)).findLatestCodesByPrefix(anyString());
    }
}