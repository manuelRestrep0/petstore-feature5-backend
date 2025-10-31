package com.petstore.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for PromotionController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllActivePromotions() throws Exception {
        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllPromotions() throws Exception {
        mockMvc.perform(get("/api/promotions/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetPromotionsByCategory() throws Exception {
        // Test con categoría no existente
        mockMvc.perform(get("/api/promotions/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetPromotionsByCategory_InvalidId() throws Exception {
        // Test con ID de categoría inválido
        mockMvc.perform(get("/api/promotions/category/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetValidPromotions() throws Exception {
        mockMvc.perform(get("/api/promotions/valid"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetStatus() throws Exception {
        mockMvc.perform(get("/api/promotions/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetTrash() throws Exception {
        // Test endpoint de papelera
        mockMvc.perform(get("/api/promotions/trash"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeletePromotion_WithoutUserId() throws Exception {
        // Test eliminación sin userId (devuelve 404 si no encuentra la promoción o no puede eliminar)
        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePromotion_WithUserId() throws Exception {
        // Test eliminación con userId
        mockMvc.perform(delete("/api/promotions/1").param("userId", "1"))
                .andExpect(status().isNotFound()); // 404 porque la promoción no existe
    }

    @Test
    void testDeletePromotion_InvalidId() throws Exception {
        // Test eliminación con ID inválido
        mockMvc.perform(delete("/api/promotions/invalid").param("userId", "1"))
                .andExpect(status().isBadRequest());
    }
}