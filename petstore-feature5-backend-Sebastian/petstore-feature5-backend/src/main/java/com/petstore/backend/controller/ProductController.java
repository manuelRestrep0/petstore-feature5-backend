package com.petstore.backend.controller;

import com.petstore.backend.dto.ProductDTO;
import com.petstore.backend.dto.ProductListResponse;
import com.petstore.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "http://127.0.0.1:5500", "http://localhost:5500"})
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Obtiene todos los productos
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            
            ProductListResponse response = ProductListResponse.success(products);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ProductListResponse errorResponse = ProductListResponse.error(
                "Error al obtener los productos: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Obtiene productos por ID de categoría
     * GET /api/products/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductListResponse> getProductsByCategoryId(@PathVariable Integer categoryId) {
        try {
            List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
            
            ProductListResponse response = ProductListResponse.success(products);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ProductListResponse errorResponse = ProductListResponse.error(
                "Error al obtener productos por categoría: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Obtiene productos por nombre de categoría
     * GET /api/products/category/name/{categoryName}
     */
    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<ProductListResponse> getProductsByCategoryName(@PathVariable String categoryName) {
        try {
            List<ProductDTO> products = productService.getProductsByCategoryName(categoryName);
            
            ProductListResponse response = ProductListResponse.success(products);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ProductListResponse errorResponse = ProductListResponse.error(
                "Error al obtener productos por nombre de categoría: " + e.getMessage()
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Endpoint de estado para verificar que el servicio funciona
     * GET /api/products/status
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok().body(java.util.Map.of(
            "status", "OK",
            "message", "Product service is running",
            "timestamp", java.time.LocalDateTime.now(),
            "endpoints", java.util.List.of(
                "GET /api/products - Todos los productos",
                "GET /api/products/category/{id} - Productos por ID de categoría",
                "GET /api/products/category/name/{name} - Productos por nombre de categoría",
                "GET /api/products/status - Estado del servicio"
            )
        ));
    }
}
