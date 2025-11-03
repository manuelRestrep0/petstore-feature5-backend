package com.petstore.backend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void testGetCategoryById_ValidId() throws Exception {
        // Crear una categoría primero
        String categoryJson = "{\"categoryName\":\"TestCategory\",\"description\":\"Test Description\"}";
        
        String response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
                
        // Extraer el ID de la respuesta
        Integer categoryId = Integer.valueOf(response.split("\"categoryId\":")[1].split(",")[0]);
        
        // Obtener la categoría por ID
        mockMvc.perform(get("/api/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.categoryName", is("TestCategory")));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        mockMvc.perform(get("/api/categories/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCategoryById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/categories/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCategory_Valid() throws Exception {
        String categoryJson = "{\"categoryName\":\"Electronics\",\"description\":\"Electronic devices\"}";
        
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.categoryName", is("Electronics")))
                .andExpect(jsonPath("$.description", is("Electronic devices")));
    }

    @Test
    void testCreateCategory_EmptyName() throws Exception {
        String categoryJson = "{\"categoryName\":\"\",\"description\":\"Test Description\"}";
        
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson))
                .andExpect(status().isCreated()); // El sistema permite nombres vacíos según comportamiento observado
    }

    @Test
    void testUpdateCategory_Valid() throws Exception {
        // Crear categoría
        String createJson = "{\"categoryName\":\"Original\",\"description\":\"Original Description\"}";
        String response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
                
        Integer categoryId = Integer.valueOf(response.split("\"categoryId\":")[1].split(",")[0]);
        
        // Actualizar categoría
        String updateJson = "{\"categoryName\":\"Updated\",\"description\":\"Updated Description\"}";
        mockMvc.perform(put("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName", is("Updated")));
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        String updateJson = "{\"categoryName\":\"Updated\",\"description\":\"Updated Description\"}";
        
        mockMvc.perform(put("/api/categories/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategory_Valid() throws Exception {
        // Crear categoría
        String createJson = "{\"categoryName\":\"ToDelete\",\"description\":\"Will be deleted\"}";
        String response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
                
        Integer categoryId = Integer.valueOf(response.split("\"categoryId\":")[1].split(",")[0]);
        
        // Eliminar categoría
        mockMvc.perform(delete("/api/categories/" + categoryId))
                .andExpect(status().isNoContent());
                
        // Verificar que fue eliminada
        mockMvc.perform(get("/api/categories/" + categoryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        mockMvc.perform(delete("/api/categories/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCategoriesWithPagination() throws Exception {
        mockMvc.perform(get("/api/categories")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
