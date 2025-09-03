package com.autoadda.apis.quote.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.autoadda.apis.quote.entity.Quotation;
import com.autoadda.apis.quote.repository.QuotationRepository;
import com.autoadda.apis.quote.request.QuotationRequest;
import com.autoadda.apis.quote.response.QuotationResponse;
import com.autoadda.apis.quote.mapper.QuotationMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        quotation.setGarageId(1);

        // Setup a sample request
        quotationRequest = new QuotationRequest();
        quotationRequest.setGarageIds(Arrays.asList(1, 2, 3));
        // Set other request properties as needed

        // Setup a sample response
        quotationResponse = new QuotationResponse();
        quotationResponse.setId("Q123");
        quotationResponse.setQuoteNumber("RQ-2312-000001");
        quotationResponse.setGarageId(1);
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
    void createQuotation_shouldGenerateQuoteNumberAndSaveForEachGarage() {
        // Arrange
        List<Integer> garageIds = Arrays.asList(1, 2, 3);
        quotationRequest.setGarageIds(garageIds);

        Quotation quotationForGarage1 = new Quotation();
        quotationForGarage1.setGarageId(1);
        quotationForGarage1.setQuoteNumber("RQ-2312-000002");

        Quotation quotationForGarage2 = new Quotation();
        quotationForGarage2.setGarageId(2);
        quotationForGarage2.setQuoteNumber("RQ-2312-000003");

        Quotation quotationForGarage3 = new Quotation();
        quotationForGarage3.setGarageId(3);
        quotationForGarage3.setQuoteNumber("RQ-2312-000004");

        // Mock mapper to return new Quotation instances for each call
        when(quotationMapper.toEntity(quotationRequest))
                .thenReturn(new Quotation(), new Quotation(), new Quotation());

        // Mock repository to return different quotations for each save
        when(quotationRepository.save(any(Quotation.class)))
                .thenReturn(quotationForGarage1, quotationForGarage2, quotationForGarage3);

        // Mock the sequence generation
        when(quotationRepository.findLatestCodesByPrefix(anyString()))
                .thenReturn(List.of("RQ-2312-000001"));

        QuotationResponse response1 = new QuotationResponse();
        response1.setGarageId(1);
        QuotationResponse response2 = new QuotationResponse();
        response2.setGarageId(2);
        QuotationResponse response3 = new QuotationResponse();
        response3.setGarageId(3);

        when(quotationMapper.toResponse(any(Quotation.class)))
                .thenReturn(response1, response2, response3);

        // Act
        List<QuotationResponse> result = quotationService.createQuotation(quotationRequest);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getGarageId());
        assertEquals(2, result.get(1).getGarageId());
        assertEquals(3, result.get(2).getGarageId());

        verify(quotationMapper, times(3)).toEntity(quotationRequest);
        verify(quotationRepository, times(3)).save(any(Quotation.class));
        verify(quotationMapper, times(3)).toResponse(any(Quotation.class));
        verify(quotationRepository, times(3)).findLatestCodesByPrefix(anyString());
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
    void getQuotationByGarageId_shouldReturnQuotations() {
        // Arrange
        Quotation quotation1 = new Quotation();
        quotation1.setGarageId(1);
        Quotation quotation2 = new Quotation();
        quotation2.setGarageId(1);

        when(quotationRepository.findByGarageId(1)).thenReturn(List.of(quotation1, quotation2));
        when(quotationMapper.toResponse(quotation1)).thenReturn(new QuotationResponse());
        when(quotationMapper.toResponse(quotation2)).thenReturn(new QuotationResponse());

        // Act
        List<QuotationResponse> result = quotationService.getQuotationByGarageId(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(quotationRepository, times(1)).findByGarageId(1);
        verify(quotationMapper, times(2)).toResponse(any(Quotation.class));
    }

    @Test
    void generateQuotationCode_firstInMonth_shouldStartFrom000001() {
        // Arrange
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of());
        when(quotationMapper.toEntity(any())).thenReturn(new Quotation());
        when(quotationRepository.save(any())).thenReturn(quotation);
        when(quotationMapper.toResponse(any())).thenReturn(quotationResponse);

        // Act
        List<QuotationResponse> result = quotationService.createQuotation(quotationRequest);

        // Assert
        assertNotNull(result);
        verify(quotationRepository, times(3)).findLatestCodesByPrefix(anyString());
    }

    @Test
    void generateQuotationCode_nextInSequence_shouldIncrement() {
        // Arrange
        when(quotationMapper.toEntity(any())).thenReturn(new Quotation(), new Quotation(), new Quotation());
        when(quotationRepository.save(any())).thenReturn(quotation, quotation, quotation);
        when(quotationMapper.toResponse(any())).thenReturn(quotationResponse, quotationResponse, quotationResponse);
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of("RQ-2312-000001"));

        // Act
        List<QuotationResponse> result = quotationService.createQuotation(quotationRequest);

        // Assert
        assertNotNull(result);
        verify(quotationRepository, times(3)).findLatestCodesByPrefix(anyString());
    }

    @Test
    void generateQuotationCode_invalidLatestCode_shouldStartFrom1() {
        // Arrange
        when(quotationMapper.toEntity(any())).thenReturn(new Quotation(), new Quotation(), new Quotation());
        when(quotationRepository.save(any())).thenReturn(quotation, quotation, quotation);
        when(quotationMapper.toResponse(any())).thenReturn(quotationResponse, quotationResponse, quotationResponse);
        when(quotationRepository.findLatestCodesByPrefix(anyString())).thenReturn(List.of("RQ-2312-INVALID"));

        // Act
        List<QuotationResponse> result = quotationService.createQuotation(quotationRequest);

        // Assert
        assertNotNull(result);
        verify(quotationRepository, times(3)).findLatestCodesByPrefix(anyString());
    }

    @Test
    void createQuotation_emptyGarageIds_shouldReturnEmptyList() {
        // Arrange
        quotationRequest.setGarageIds(List.of());

        // Act
        List<QuotationResponse> result = quotationService.createQuotation(quotationRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(quotationMapper, never()).toEntity(any());
        verify(quotationRepository, never()).save(any());
        verify(quotationMapper, never()).toResponse(any());
    }
}