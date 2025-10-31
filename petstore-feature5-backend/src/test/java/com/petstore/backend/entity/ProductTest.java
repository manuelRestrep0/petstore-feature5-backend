package com.petstore.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        product = new Product();
        category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Electronics");
    }

    @Test
    void constructor_ShouldCreateEmptyProduct() {
        // Then
        assertNotNull(product);
        assertNull(product.getProductId());
        assertNull(product.getProductName());
        assertNull(product.getBasePrice());
        assertNull(product.getSku());
        assertNull(product.getCategory());
        assertNull(product.getPromotion());
    }

    @Test
    void parameterizedConstructor_ShouldCreateProductWithValues() {
        // Given
        String productName = "Laptop";
        Double basePrice = 999.99;
        Integer sku = 12345;

        // When
        Product product = new Product(productName, basePrice, sku, category);

        // Then
        assertNotNull(product);
        assertNull(product.getProductId()); // ID should be null until persisted
        assertEquals(productName, product.getProductName());
        assertEquals(basePrice, product.getBasePrice());
        assertEquals(sku, product.getSku());
        assertEquals(category, product.getCategory());
        assertNull(product.getPromotion()); // Promotion not set in constructor
    }

    @Test
    void setProductId_ShouldSetProductId() {
        // Given
        Integer productId = 1;

        // When
        product.setProductId(productId);

        // Then
        assertEquals(productId, product.getProductId());
    }

    @Test
    void setProductName_ShouldSetProductName() {
        // Given
        String productName = "Gaming Mouse";

        // When
        product.setProductName(productName);

        // Then
        assertEquals(productName, product.getProductName());
    }

    @Test
    void setBasePrice_ShouldSetBasePrice() {
        // Given
        Double basePrice = 59.99;

        // When
        product.setBasePrice(basePrice);

        // Then
        assertEquals(basePrice, product.getBasePrice());
    }

    @Test
    void setSku_ShouldSetSku() {
        // Given
        Integer sku = 54321;

        // When
        product.setSku(sku);

        // Then
        assertEquals(sku, product.getSku());
    }

    @Test
    void setCategory_ShouldSetCategory() {
        // When
        product.setCategory(category);

        // Then
        assertEquals(category, product.getCategory());
    }

    @Test
    void setPromotion_ShouldSetPromotion() {
        // Given
        Promotion promotion = new Promotion();
        promotion.setPromotionId(1);
        promotion.setPromotionName("Summer Sale");

        // When
        product.setPromotion(promotion);

        // Then
        assertEquals(promotion, product.getPromotion());
    }

    @Test
    void setPromotion_ShouldAllowNull() {
        // Given
        product.setPromotion(new Promotion());

        // When
        product.setPromotion(null);

        // Then
        assertNull(product.getPromotion());
    }

    @Test
    void allFieldsCanBeSetAndRetrieved() {
        // Given
        Integer productId = 1;
        String productName = "Test Product";
        Double basePrice = 199.99;
        Integer sku = 99999;
        Promotion promotion = new Promotion();

        // When
        product.setProductId(productId);
        product.setProductName(productName);
        product.setBasePrice(basePrice);
        product.setSku(sku);
        product.setCategory(category);
        product.setPromotion(promotion);

        // Then
        assertEquals(productId, product.getProductId());
        assertEquals(productName, product.getProductName());
        assertEquals(basePrice, product.getBasePrice());
        assertEquals(sku, product.getSku());
        assertEquals(category, product.getCategory());
        assertEquals(promotion, product.getPromotion());
    }

    @Test
    void fieldsCanBeNullSafely() {
        // Given
        product.setProductId(null);
        product.setProductName(null);
        product.setBasePrice(null);
        product.setSku(null);
        product.setCategory(null);
        product.setPromotion(null);

        // When & Then
        assertNull(product.getProductId());
        assertNull(product.getProductName());
        assertNull(product.getBasePrice());
        assertNull(product.getSku());
        assertNull(product.getCategory());
        assertNull(product.getPromotion());

        // Should not throw any exceptions
        assertDoesNotThrow(() -> {
            product.toString();
            product.hashCode();
        });
    }

    @Test
    void productWithNegativePrice_ShouldStillWork() {
        // Given
        Double negativePrice = -10.0;

        // When
        product.setBasePrice(negativePrice);

        // Then
        assertEquals(negativePrice, product.getBasePrice());
    }

    @Test
    void productWithZeroPrice_ShouldWork() {
        // Given
        Double zeroPrice = 0.0;

        // When
        product.setBasePrice(zeroPrice);

        // Then
        assertEquals(zeroPrice, product.getBasePrice());
    }

    @Test
    void productWithVeryLargePrice_ShouldWork() {
        // Given
        Double largePrice = Double.MAX_VALUE;

        // When
        product.setBasePrice(largePrice);

        // Then
        assertEquals(largePrice, product.getBasePrice());
    }

    @Test
    void productWithNegativeSku_ShouldWork() {
        // Given
        Integer negativeSku = -123;

        // When
        product.setSku(negativeSku);

        // Then
        assertEquals(negativeSku, product.getSku());
    }

    @Test
    void productWithZeroSku_ShouldWork() {
        // Given
        Integer zeroSku = 0;

        // When
        product.setSku(zeroSku);

        // Then
        assertEquals(zeroSku, product.getSku());
    }
}
