package com.petstore.backend.controller;

import com.petstore.backend.dto.PromotionDTO;
import com.petstore.backend.dto.PromotionListResponse;
import com.petstore.backend.dto.PromotionResponseDTO;
import com.petstore.backend.entity.Promotion;
import com.petstore.backend.mapper.MapperFacade;
import com.petstore.backend.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "http://127.0.0.1:5500", "http://localhost:5500"})
public class PromotionController {

    @Autowired
    private PromotionService promotionService;
    
    @Autowired
    private MapperFacade mapperFacade;

    /**
     * Obtiene todas las promociones activas y vigentes
     * GET /api/promotions
     */
    @GetMapping
    public ResponseEntity<PromotionListResponse> getAllActivePromotions() {
        try {
            List<PromotionDTO> promotions = promotionService.getAllActivePromotions();
            
            PromotionListResponse response = PromotionListResponse.success(promotions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            PromotionListResponse errorResponse = PromotionListResponse.error(
                "Error al obtener las promociones: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Obtiene todas las promociones (para administración)
     * GET /api/promotions/all
     */
    @GetMapping("/all")
    public ResponseEntity<PromotionListResponse> getAllPromotions() {
        try {
            List<PromotionDTO> promotions = promotionService.getAllPromotions();
            
            PromotionListResponse response = PromotionListResponse.success(promotions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            PromotionListResponse errorResponse = PromotionListResponse.error(
                "Error al obtener todas las promociones: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Obtiene promociones por categoría
     * GET /api/promotions/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PromotionListResponse> getPromotionsByCategory(@PathVariable Integer categoryId) {
        try {
            List<PromotionDTO> promotions = promotionService.getPromotionsByCategory(categoryId);
            
            PromotionListResponse response = PromotionListResponse.success(promotions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            PromotionListResponse errorResponse = PromotionListResponse.error(
                "Error al obtener promociones por categoría: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Obtiene promociones vigentes para hoy
     * GET /api/promotions/valid
     */
    @GetMapping("/valid")
    public ResponseEntity<PromotionListResponse> getValidPromotions() {
        try {
            List<PromotionDTO> promotions = promotionService.getValidPromotions();
            
            PromotionListResponse response = PromotionListResponse.success(promotions);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            PromotionListResponse errorResponse = PromotionListResponse.error(
                "Error al obtener promociones vigentes: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Endpoint de estado para verificar que el servicio funciona
     * GET /api/promotions/status
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok().body(java.util.Map.of(
            "status", "OK",
            "message", "Promotion service is running",
            "timestamp", java.time.LocalDateTime.now(),
            "endpoints", java.util.List.of(
                "GET /api/promotions - Promociones activas y vigentes",
                "GET /api/promotions/all - Todas las promociones (admin)",
                "GET /api/promotions/category/{id} - Promociones por categoría",
                "GET /api/promotions/valid - Promociones vigentes hoy",
                "GET /api/promotions/status - Estado del servicio"
            )
        ));
    }
}
