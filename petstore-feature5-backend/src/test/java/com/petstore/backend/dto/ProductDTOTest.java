package com.petstore.backend.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductDTOTest {

    private ProductDTO productDTO;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1);
        categoryDTO.setCategoryName("Electronics");
    }

    @Test
    void constructor_ShouldCreateEmptyProductDTO() {
        // Then
        assertNotNull(productDTO);
        assertNull(productDTO.getProductId());
        assertNull(productDTO.getProductName());
        assertNull(productDTO.getDescription());
        assertNull(productDTO.getPrice());
        assertNull(productDTO.getStock());
        assertNull(productDTO.getImageUrl());
        assertNull(productDTO.getCreatedAt());
        assertNull(productDTO.getUpdatedAt());
        assertNull(productDTO.getCategory());
    }

    @Test
    void setProductId_ShouldSetProductId() {
        // Given
        Integer productId = 1;

        // When
        productDTO.setProductId(productId);

        // Then
        assertEquals(productId, productDTO.getProductId());
    }

    @Test
    void setProductName_ShouldSetProductName() {
        // Given
        String productName = "Gaming Laptop";

        // When
        productDTO.setProductName(productName);

        // Then
        assertEquals(productName, productDTO.getProductName());
    }

    @Test
    void setDescription_ShouldSetDescription() {
        // Given
        String description = "High-performance gaming laptop";

        // When
        productDTO.setDescription(description);

        // Then
        assertEquals(description, productDTO.getDescription());
    }

    @Test
    void setPrice_ShouldSetPrice() {
        // Given
        BigDecimal price = BigDecimal.valueOf(999.99);

        // When
        productDTO.setPrice(price);

        // Then
        assertEquals(price, productDTO.getPrice());
    }

    @Test
    void setStock_ShouldSetStock() {
        // Given
        Integer stock = 10;

        // When
        productDTO.setStock(stock);

        // Then
        assertEquals(stock, productDTO.getStock());
    }

    @Test
    void setImageUrl_ShouldSetImageUrl() {
        // Given
        String imageUrl = "https://example.com/laptop.jpg";

        // When
        productDTO.setImageUrl(imageUrl);

        // Then
        assertEquals(imageUrl, productDTO.getImageUrl());
    }

    @Test
    void setCreatedAt_ShouldSetCreatedAt() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        productDTO.setCreatedAt(createdAt);

        // Then
        assertEquals(createdAt, productDTO.getCreatedAt());
    }

    @Test
    void setUpdatedAt_ShouldSetUpdatedAt() {
        // Given
        LocalDateTime updatedAt = LocalDateTime.now();

        // When
        productDTO.setUpdatedAt(updatedAt);

        // Then
        assertEquals(updatedAt, productDTO.getUpdatedAt());
    }

    @Test
    void setCategory_ShouldSetCategory() {
        // When
        productDTO.setCategory(categoryDTO);

        // Then
        assertEquals(categoryDTO, productDTO.getCategory());
    }

    @Test
    void allFieldsCanBeSetAndRetrieved() {
        // Given
        Integer productId = 1;
        String productName = "Gaming Mouse";
        String description = "RGB gaming mouse with high DPI";
        BigDecimal price = BigDecimal.valueOf(79.99);
        Integer stock = 25;
        String imageUrl = "https://example.com/mouse.jpg";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusMinutes(5);

        // When
        productDTO.setProductId(productId);
        productDTO.setProductName(productName);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setStock(stock);
        productDTO.setImageUrl(imageUrl);
        productDTO.setCreatedAt(createdAt);
        productDTO.setUpdatedAt(updatedAt);
        productDTO.setCategory(categoryDTO);

        // Then
        assertEquals(productId, productDTO.getProductId());
        assertEquals(productName, productDTO.getProductName());
        assertEquals(description, productDTO.getDescription());
        assertEquals(price, productDTO.getPrice());
        assertEquals(stock, productDTO.getStock());
        assertEquals(imageUrl, productDTO.getImageUrl());
        assertEquals(createdAt, productDTO.getCreatedAt());
        assertEquals(updatedAt, productDTO.getUpdatedAt());
        assertEquals(categoryDTO, productDTO.getCategory());
    }

    @Test
    void fieldsCanBeNullSafely() {
        // Given - Set all fields to null
        productDTO.setProductId(null);
        productDTO.setProductName(null);
        productDTO.setDescription(null);
        productDTO.setPrice(null);
        productDTO.setStock(null);
        productDTO.setImageUrl(null);
        productDTO.setCreatedAt(null);
        productDTO.setUpdatedAt(null);
        productDTO.setCategory(null);

        // When & Then
        assertNull(productDTO.getProductId());
        assertNull(productDTO.getProductName());
        assertNull(productDTO.getDescription());
        assertNull(productDTO.getPrice());
        assertNull(productDTO.getStock());
        assertNull(productDTO.getImageUrl());
        assertNull(productDTO.getCreatedAt());
        assertNull(productDTO.getUpdatedAt());
        assertNull(productDTO.getCategory());

        // Should not throw any exceptions
        assertDoesNotThrow(() -> {
            productDTO.toString();
            productDTO.hashCode();
        });
    }

    @Test
    void price_ShouldAcceptZero() {
        // Given
        BigDecimal zeroPrice = BigDecimal.ZERO;

        // When
        productDTO.setPrice(zeroPrice);

        // Then
        assertEquals(zeroPrice, productDTO.getPrice());
    }

    @Test
    void price_ShouldAcceptLargeValue() {
        // Given
        BigDecimal largePrice = BigDecimal.valueOf(99999.99);

        // When
        productDTO.setPrice(largePrice);

        // Then
        assertEquals(largePrice, productDTO.getPrice());
    }

    @Test
    void stock_ShouldAcceptZero() {
        // Given
        Integer zeroStock = 0;

        // When
        productDTO.setStock(zeroStock);

        // Then
        assertEquals(zeroStock, productDTO.getStock());
    }

    @Test
    void stock_ShouldAcceptNegativeValue() {
        // Given
        Integer negativeStock = -5;

        // When
        productDTO.setStock(negativeStock);

        // Then
        assertEquals(negativeStock, productDTO.getStock());
    }

    @Test
    void stock_ShouldAcceptLargeValue() {
        // Given
        Integer largeStock = Integer.MAX_VALUE;

        // When
        productDTO.setStock(largeStock);

        // Then
        assertEquals(largeStock, productDTO.getStock());
    }

    @Test
    void productName_ShouldAcceptEmptyString() {
        // Given
        String emptyName = "";

        // When
        productDTO.setProductName(emptyName);

        // Then
        assertEquals(emptyName, productDTO.getProductName());
    }

    @Test
    void description_ShouldAcceptEmptyString() {
        // Given
        String emptyDescription = "";

        // When
        productDTO.setDescription(emptyDescription);

        // Then
        assertEquals(emptyDescription, productDTO.getDescription());
    }

    @Test
    void imageUrl_ShouldAcceptEmptyString() {
        // Given
        String emptyUrl = "";

        // When
        productDTO.setImageUrl(emptyUrl);

        // Then
        assertEquals(emptyUrl, productDTO.getImageUrl());
    }

    @Test
    void productName_ShouldAcceptLongString() {
        // Given
        String longName = "A".repeat(500);

        // When
        productDTO.setProductName(longName);

        // Then
        assertEquals(longName, productDTO.getProductName());
    }

    @Test
    void imageUrl_ShouldAcceptLongUrl() {
        // Given
        String longUrl = "https://example.com/" + "path/".repeat(50) + "image.jpg";

        // When
        productDTO.setImageUrl(longUrl);

        // Then
        assertEquals(longUrl, productDTO.getImageUrl());
    }
}
