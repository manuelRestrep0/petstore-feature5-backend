package com.petstore.backend.controller;

import com.petstore.backend.dto.CategoryDTO;
import com.petstore.backend.entity.Category;
import com.petstore.backend.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------
    // GET /api/categories - obtener todas las categorías
    // -------------------------------------------------------------
    @Test
    void getAllCategories_returnsListOfCategories() {
        // Arrange
        List<Category> mockCategories = Arrays.asList(
                new Category("Tecnología", "Productos tecnológicos"),
                new Category("Hogar", "Artículos del hogar")
        );
        when(categoryService.findAll()).thenReturn(mockCategories);

        // Act
        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Tecnología", response.getBody().get(0).getCategoryName());
        verify(categoryService, times(1)).findAll();
    }

    @Test
    void getAllCategories_whenExceptionThrown_returnsInternalServerError() {
        // Arrange
        when(categoryService.findAll()).thenThrow(new RuntimeException("DB error"));

        // Act
        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories();

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        verify(categoryService, times(1)).findAll();
    }

    // -------------------------------------------------------------
    // GET /api/categories/{id} - obtener categoría por id
    // -------------------------------------------------------------
    @Test
    void getCategoryById_existingId_returnsCategory() {
        // Arrange
        Category category = new Category("Ropa", "Moda y vestimenta");
        when(categoryService.findById(1)).thenReturn(Optional.of(category));

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ropa", response.getBody().getCategoryName());
        verify(categoryService, times(1)).findById(1);
    }

    @Test
    void getCategoryById_nonExistingId_returnsNotFound() {
        // Arrange
        when(categoryService.findById(99)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(99);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(categoryService, times(1)).findById(99);
    }

    @Test
    void getCategoryById_whenExceptionThrown_returnsServerError() {
        // Arrange
        when(categoryService.findById(1)).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }

    // -------------------------------------------------------------
    // POST /api/categories - crear categoría
    // -------------------------------------------------------------
    @Test
    void createCategory_validCategory_returnsCreatedCategory() {
        // Arrange
        CategoryDTO dto = new CategoryDTO(null, "Electrónica", "Dispositivos eléctricos");
        Category categoryToSave = new Category("Electrónica", "Dispositivos eléctricos");
        Category savedCategory = new Category("Electrónica", "Dispositivos eléctricos");

        when(categoryService.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.createCategory(dto);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Electrónica", response.getBody().getCategoryName());
        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_whenExceptionThrown_returnsBadRequest() {
        // Arrange
        CategoryDTO dto = new CategoryDTO(null, "Fail", "Error simulation");
        when(categoryService.save(any(Category.class))).thenThrow(new RuntimeException("DB error"));

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.createCategory(dto);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    // -------------------------------------------------------------
    // PUT /api/categories/{id} - actualizar categoría
    // -------------------------------------------------------------
    @Test
    void updateCategory_existingId_returnsUpdatedCategory() {
        // Arrange
        Category existing = new Category("Ropa", "Moda y vestimenta");
        CategoryDTO updateDTO = new CategoryDTO(1, "Calzado", "Zapatos y botas");
        Category updated = new Category("Calzado", "Zapatos y botas");

        when(categoryService.findById(1)).thenReturn(Optional.of(existing));
        when(categoryService.save(any(Category.class))).thenReturn(updated);

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(1, updateDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Calzado", response.getBody().getCategoryName());
        verify(categoryService, times(1)).findById(1);
        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_nonExistingId_returnsNotFound() {
        // Arrange
        when(categoryService.findById(99)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(99, new CategoryDTO());

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateCategory_whenExceptionThrown_returnsBadRequest() {
        // Arrange
        when(categoryService.findById(1)).thenThrow(new RuntimeException("DB error"));

        // Act
        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(1, new CategoryDTO());

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }

    // -------------------------------------------------------------
    // DELETE /api/categories/{id}
    // -------------------------------------------------------------
    @Test
    void deleteCategory_existingId_returnsNoContent() {
        // Arrange
        when(categoryService.existsById(1)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = categoryController.deleteCategory(1);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(categoryService, times(1)).deleteById(1);
    }

    @Test
    void deleteCategory_nonExistingId_returnsNotFound() {
        // Arrange
        when(categoryService.existsById(99)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = categoryController.deleteCategory(99);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(categoryService, never()).deleteById(anyInt());
    }

    @Test
    void deleteCategory_whenExceptionThrown_returnsServerError() {
        // Arrange
        when(categoryService.existsById(1)).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<Void> response = categoryController.deleteCategory(1);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }

    // -------------------------------------------------------------
    // GET /api/categories/info
    // -------------------------------------------------------------
    @Test
    void getEndpointsInfo_returnsInfoString() {
        // Act
        ResponseEntity<String> response = categoryController.getEndpointsInfo();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("GET /api/categories"));
        assertTrue(response.getBody().contains("DELETE /api/categories/{id}"));
    }
}
