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
 * Integration tests for ProductController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetProductById_NonExistingId() throws Exception {
        // Con base de datos vacía, cualquier ID debería devolver 404
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductById_AnotherNonExistingId() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductById_InvalidId() throws Exception {
        // Spring devuelve 400 Bad Request para conversiones de parámetros inválidos
        mockMvc.perform(get("/api/products/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        // Test con categoría no existente
        mockMvc.perform(get("/api/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetProductsByCategory_InvalidId() throws Exception {
        // Test con ID de categoría inválido
        mockMvc.perform(get("/api/products/category/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearchProducts() throws Exception {
        // Test búsqueda con parámetro
        mockMvc.perform(get("/api/products/search").param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSearchProducts_EmptyName() throws Exception {
        // Test búsqueda con nombre vacío
        mockMvc.perform(get("/api/products/search").param("name", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSearchProducts_MissingParameter() throws Exception {
        // Test búsqueda sin parámetro de nombre (debería devolver error)
        mockMvc.perform(get("/api/products/search"))
                .andExpect(status().isBadRequest());
    }
}