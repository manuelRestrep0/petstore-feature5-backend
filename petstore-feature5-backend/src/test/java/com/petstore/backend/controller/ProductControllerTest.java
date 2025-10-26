package com.petstore.backend.controller;

import com.petstore.backend.dto.ProductDTO;
import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Product;
import com.petstore.backend.mapper.MapperFacade;
import com.petstore.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category("Tecnología", "Productos tecnológicos");
    }

    @Test
    void getAllProducts_returnsListOfProductDTOs() {
        // Arrange
        List<Product> mockProducts = Arrays.asList(
                new Product("Laptop", 2000.0, 123, category),
                new Product("Mouse", 50.0, 124, category)
        );
        when(productService.findAll()).thenReturn(mockProducts);

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).getProductName());
        verify(productService, times(1)).findAll();
    }

    @Test
    void getAllProducts_whenExceptionThrown_returnsInternalServerError() {
        // Arrange
        when(productService.findAll()).thenThrow(new RuntimeException("DB error"));

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        verify(productService, times(1)).findAll();
    }

    @Test
    void getProductsByCategory_validCategory_returnsProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product("Smartphone", 1500.0, 111, category),
                new Product("Tablet", 1000.0, 112, category)
        );
        when(productService.findByCategoryId(1)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getProductsByCategory(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Smartphone", response.getBody().get(0).getProductName());
        verify(productService, times(1)).findByCategoryId(1);
    }

    @Test
    void getProductsByCategory_whenExceptionThrown_returnsServerError() {
        // Arrange
        when(productService.findByCategoryId(1)).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getProductsByCategory(1);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getProductById_existingId_returnsProductDTO() {
        // Arrange
        Product product = new Product("Teclado", 80.0, 125, category);
        product.setProductId(1);
        when(productService.findById(1)).thenReturn(Optional.of(product));

        // Act
        ResponseEntity<ProductDTO> response = productController.getProductById(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Teclado", response.getBody().getProductName());
        assertEquals(BigDecimal.valueOf(80.0), response.getBody().getPrice());
        verify(productService, times(1)).findById(1);
    }

    @Test
    void getProductById_nonExistingId_returnsNotFound() {
        // Arrange
        when(productService.findById(99)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProductDTO> response = productController.getProductById(99);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(productService, times(1)).findById(99);
    }

    @Test
    void getProductById_whenExceptionThrown_returnsServerError() {
        // Arrange
        when(productService.findById(1)).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<ProductDTO> response = productController.getProductById(1);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void searchProducts_validName_returnsFilteredList() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product("Mouse Gamer", 100.0, 222, category)
        );
        when(productService.findByNameContaining("Mouse")).thenReturn(products);

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.searchProducts("Mouse");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Mouse Gamer", response.getBody().get(0).getProductName());
        verify(productService, times(1)).findByNameContaining("Mouse");
    }

    @Test
    void searchProducts_whenExceptionThrown_returnsServerError() {
        // Arrange
        when(productService.findByNameContaining("Test")).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.searchProducts("Test");

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getProductsByPriceRange_validRange_returnsProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product("Monitor", 500.0, 333, category),
                new Product("TV", 1000.0, 334, category)
        );
        when(productService.findByPriceBetween(400.0, 1100.0)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getProductsByPriceRange(400.0, 1100.0);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Monitor", response.getBody().get(0).getProductName());
        verify(productService, times(1)).findByPriceBetween(400.0, 1100.0);
    }

    @Test
    void getProductsByPriceRange_whenExceptionThrown_returnsServerError() {
        // Arrange
        when(productService.findByPriceBetween(100.0, 200.0)).thenThrow(new RuntimeException("DB error"));

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getProductsByPriceRange(100.0, 200.0);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
    }
}
