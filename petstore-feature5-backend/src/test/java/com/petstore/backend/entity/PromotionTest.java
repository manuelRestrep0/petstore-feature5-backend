package com.petstore.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PromotionTest {

    private Promotion promotion;
    private Category category;
    private Status status;
    private User user;

    @BeforeEach
    void setUp() {
        promotion = new Promotion();
        
        category = new Category();
        category.setCategoryId(1);
        category.setCategoryName("Electronics");
        
        status = new Status();
        status.setStatusId(1);
        status.setStatusName("ACTIVE");
        
        user = new User();
        user.setUserId(1);
        user.setEmail("test@test.com");
    }

    @Test
    void constructor_ShouldCreateEmptyPromotion() {
        // Then
        assertNotNull(promotion);
        assertNull(promotion.getPromotionId());
        assertNull(promotion.getPromotionName());
        assertNull(promotion.getDescription());
        assertNull(promotion.getStartDate());
        assertNull(promotion.getEndDate());
        assertNull(promotion.getDiscountValue());
        assertNull(promotion.getStatus());
        assertNull(promotion.getUser());
        assertNull(promotion.getCategory());
    }

    @Test
    void setPromotionId_ShouldSetPromotionId() {
        // Given
        Integer promotionId = 1;

        // When
        promotion.setPromotionId(promotionId);

        // Then
        assertEquals(promotionId, promotion.getPromotionId());
    }

    @Test
    void setPromotionName_ShouldSetPromotionName() {
        // Given
        String promotionName = "Summer Sale";

        // When
        promotion.setPromotionName(promotionName);

        // Then
        assertEquals(promotionName, promotion.getPromotionName());
    }

    @Test
    void setDescription_ShouldSetDescription() {
        // Given
        String description = "Great summer discounts on all electronics";

        // When
        promotion.setDescription(description);

        // Then
        assertEquals(description, promotion.getDescription());
    }

    @Test
    void setStartDate_ShouldSetStartDate() {
        // Given
        LocalDate startDate = LocalDate.of(2025, 6, 1);

        // When
        promotion.setStartDate(startDate);

        // Then
        assertEquals(startDate, promotion.getStartDate());
    }

    @Test
    void setEndDate_ShouldSetEndDate() {
        // Given
        LocalDate endDate = LocalDate.of(2025, 8, 31);

        // When
        promotion.setEndDate(endDate);

        // Then
        assertEquals(endDate, promotion.getEndDate());
    }

    @Test
    void setDiscountValue_ShouldSetDiscountValue() {
        // Given
        Double discountValue = 25.5;

        // When
        promotion.setDiscountValue(discountValue);

        // Then
        assertEquals(discountValue, promotion.getDiscountValue());
    }

    @Test
    void setStatus_ShouldSetStatus() {
        // When
        promotion.setStatus(status);

        // Then
        assertEquals(status, promotion.getStatus());
    }

    @Test
    void setUser_ShouldSetUser() {
        // When
        promotion.setUser(user);

        // Then
        assertEquals(user, promotion.getUser());
    }

    @Test
    void setCategory_ShouldSetCategory() {
        // When
        promotion.setCategory(category);

        // Then
        assertEquals(category, promotion.getCategory());
    }

    @Test
    void allFieldsCanBeSetAndRetrieved() {
        // Given
        Integer promotionId = 1;
        String promotionName = "Black Friday";
        String description = "Biggest sale of the year";
        LocalDate startDate = LocalDate.of(2025, 11, 25);
        LocalDate endDate = LocalDate.of(2025, 11, 30);
        Double discountValue = 50.0;

        // When
        promotion.setPromotionId(promotionId);
        promotion.setPromotionName(promotionName);
        promotion.setDescription(description);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setDiscountValue(discountValue);
        promotion.setStatus(status);
        promotion.setUser(user);
        promotion.setCategory(category);

        // Then
        assertEquals(promotionId, promotion.getPromotionId());
        assertEquals(promotionName, promotion.getPromotionName());
        assertEquals(description, promotion.getDescription());
        assertEquals(startDate, promotion.getStartDate());
        assertEquals(endDate, promotion.getEndDate());
        assertEquals(discountValue, promotion.getDiscountValue());
        assertEquals(status, promotion.getStatus());
        assertEquals(user, promotion.getUser());
        assertEquals(category, promotion.getCategory());
    }

    @Test
    void fieldsCanBeNullSafely() {
        // Given
        promotion.setPromotionId(null);
        promotion.setPromotionName(null);
        promotion.setDescription(null);
        promotion.setStartDate(null);
        promotion.setEndDate(null);
        promotion.setDiscountValue(null);
        promotion.setStatus(null);
        promotion.setUser(null);
        promotion.setCategory(null);

        // When & Then
        assertNull(promotion.getPromotionId());
        assertNull(promotion.getPromotionName());
        assertNull(promotion.getDescription());
        assertNull(promotion.getStartDate());
        assertNull(promotion.getEndDate());
        assertNull(promotion.getDiscountValue());
        assertNull(promotion.getStatus());
        assertNull(promotion.getUser());
        assertNull(promotion.getCategory());

        // Should not throw any exceptions
        assertDoesNotThrow(() -> {
            promotion.toString();
            promotion.hashCode();
        });
    }

    @Test
    void discountValue_ShouldAcceptZero() {
        // Given
        Double zeroDiscount = 0.0;

        // When
        promotion.setDiscountValue(zeroDiscount);

        // Then
        assertEquals(zeroDiscount, promotion.getDiscountValue());
    }

    @Test
    void discountValue_ShouldAcceptNegativeValue() {
        // Given
        Double negativeDiscount = -5.0;

        // When
        promotion.setDiscountValue(negativeDiscount);

        // Then
        assertEquals(negativeDiscount, promotion.getDiscountValue());
    }

    @Test
    void discountValue_ShouldAcceptLargeValue() {
        // Given
        Double largeDiscount = 999.99;

        // When
        promotion.setDiscountValue(largeDiscount);

        // Then
        assertEquals(largeDiscount, promotion.getDiscountValue());
    }

    @Test
    void dates_CanBeInAnyOrder() {
        // Given
        LocalDate laterDate = LocalDate.of(2025, 12, 31);
        LocalDate earlierDate = LocalDate.of(2025, 1, 1);

        // When - Set end date before start date
        promotion.setStartDate(laterDate);
        promotion.setEndDate(earlierDate);

        // Then - Should still work (validation is business logic, not entity constraint)
        assertEquals(laterDate, promotion.getStartDate());
        assertEquals(earlierDate, promotion.getEndDate());
    }

    @Test
    void dates_CanBeSameDate() {
        // Given
        LocalDate sameDate = LocalDate.of(2025, 7, 15);

        // When
        promotion.setStartDate(sameDate);
        promotion.setEndDate(sameDate);

        // Then
        assertEquals(sameDate, promotion.getStartDate());
        assertEquals(sameDate, promotion.getEndDate());
    }

    @Test
    void promotionName_ShouldAcceptEmptyString() {
        // Given
        String emptyName = "";

        // When
        promotion.setPromotionName(emptyName);

        // Then
        assertEquals(emptyName, promotion.getPromotionName());
    }

    @Test
    void description_ShouldAcceptEmptyString() {
        // Given
        String emptyDescription = "";

        // When
        promotion.setDescription(emptyDescription);

        // Then
        assertEquals(emptyDescription, promotion.getDescription());
    }

    @Test
    void promotionName_ShouldAcceptLongString() {
        // Given
        String longName = "A".repeat(1000); // Very long promotion name

        // When
        promotion.setPromotionName(longName);

        // Then
        assertEquals(longName, promotion.getPromotionName());
    }
}
