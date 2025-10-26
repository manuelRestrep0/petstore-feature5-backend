package com.petstore.backend.controller;

import com.petstore.backend.dto.PromotionDTO;
import com.petstore.backend.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PromotionControllerTest {
    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    private PromotionDTO promotionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        promotionDTO = new PromotionDTO();
        promotionDTO.setPromotionId(1);
        promotionDTO.setPromotionName("Promo Halloween");
        promotionDTO.setDescription("Descuento especial de Halloween");
        promotionDTO.setDiscountPercentage(BigDecimal.valueOf(0.2));
        promotionDTO.setStartDate(LocalDateTime.now().minusDays(1));
        promotionDTO.setEndDate(LocalDateTime.now().plusDays(5));
        promotionDTO.setStatus("ACTIVE");
    }

    @Test
    void getAllActivePromotions_ShouldReturnList_WhenServiceReturnsPromotions() {
        when(promotionService.getAllActivePromotions()).thenReturn(List.of(promotionDTO));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getAllActivePromotions();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Promo Halloween", response.getBody().get(0).getPromotionName());
        verify(promotionService, times(1)).getAllActivePromotions();
    }

    @Test
    void getAllActivePromotions_ShouldReturn500_WhenExceptionThrown() {
        when(promotionService.getAllActivePromotions()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getAllActivePromotions();

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getAllPromotions_ShouldReturnList_WhenServiceReturnsPromotions() {
        when(promotionService.getAllPromotions()).thenReturn(List.of(promotionDTO));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getAllPromotions();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(promotionService, times(1)).getAllPromotions();
    }

    @Test
    void getAllPromotions_ShouldReturn500_WhenExceptionThrown() {
        when(promotionService.getAllPromotions()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getAllPromotions();

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getPromotionsByCategory_ShouldReturnList_WhenServiceReturnsPromotions() {
        when(promotionService.getPromotionsByCategory(1)).thenReturn(List.of(promotionDTO));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getPromotionsByCategory(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(promotionService, times(1)).getPromotionsByCategory(1);
    }

    @Test
    void getPromotionsByCategory_ShouldReturn500_WhenExceptionThrown() {
        when(promotionService.getPromotionsByCategory(1)).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getPromotionsByCategory(1);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getValidPromotions_ShouldReturnList_WhenServiceReturnsPromotions() {
        when(promotionService.getValidPromotions()).thenReturn(List.of(promotionDTO));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getValidPromotions();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(promotionService, times(1)).getValidPromotions();
    }

    @Test
    void getValidPromotions_ShouldReturn500_WhenExceptionThrown() {
        when(promotionService.getValidPromotions()).thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<List<PromotionDTO>> response = promotionController.getValidPromotions();

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getStatus_ShouldReturnOkResponse_WithExpectedFields() {
        ResponseEntity<?> response = promotionController.getStatus();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof java.util.Map);

        var body = (java.util.Map<?, ?>) response.getBody();
        assertEquals("OK", body.get("status"));
        assertTrue(body.containsKey("message"));
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("endpoints"));
    }
}
