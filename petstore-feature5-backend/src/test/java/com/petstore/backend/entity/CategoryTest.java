package com.petstore.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void constructor_ShouldCreateEmptyCategory() {
        // Then
        assertNotNull(category);
        assertNull(category.getCategoryId());
        assertNull(category.getCategoryName());
        assertNull(category.getDescription());
    }

    @Test
    void parameterizedConstructor_ShouldCreateCategoryWithValues() {
        // Given
        String categoryName = "Electronics";
        String description = "Electronic devices and accessories";

        // When
        Category category = new Category(categoryName, description);

        // Then
        assertNotNull(category);
        assertNull(category.getCategoryId()); // ID should be null until persisted
        assertEquals(categoryName, category.getCategoryName());
        assertEquals(description, category.getDescription());
    }

    @Test
    void setCategoryId_ShouldSetCategoryId() {
        // Given
        Integer categoryId = 1;

        // When
        category.setCategoryId(categoryId);

        // Then
        assertEquals(categoryId, category.getCategoryId());
    }

    @Test
    void setCategoryName_ShouldSetCategoryName() {
        // Given
        String categoryName = "Books";

        // When
        category.setCategoryName(categoryName);

        // Then
        assertEquals(categoryName, category.getCategoryName());
    }

    @Test
    void setDescription_ShouldSetDescription() {
        // Given
        String description = "Books and magazines";

        // When
        category.setDescription(description);

        // Then
        assertEquals(description, category.getDescription());
    }

    @Test
    void equals_ShouldReturnTrueForSameCategory() {
        // Given
        category.setCategoryId(1);
        category.setCategoryName("Electronics");
        category.setDescription("Electronic devices");

        // When & Then - Same object reference should be equal
        assertEquals(category, category);
        
        // Different objects with same values are NOT equal (default Object.equals)
        Category otherCategory = new Category();
        otherCategory.setCategoryId(1);
        otherCategory.setCategoryName("Electronics");
        otherCategory.setDescription("Electronic devices");
        
        assertNotEquals(category, otherCategory);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentCategory() {
        // Given
        category.setCategoryId(1);
        category.setCategoryName("Electronics");

        Category otherCategory = new Category();
        otherCategory.setCategoryId(2);
        otherCategory.setCategoryName("Books");

        // When & Then
        assertNotEquals(category, otherCategory);
    }

    @Test
    void equals_ShouldReturnFalseForNull() {
        // When & Then
        assertNotEquals(category, null);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentClass() {
        // Given
        String notACategory = "not a category";

        // When & Then
        assertNotEquals(category, notACategory);
    }

    @Test
    void equals_ShouldReturnTrueForSameInstance() {
        // When & Then
        assertEquals(category, category);
    }

    @Test
    void hashCode_ShouldBeConsistentWithEquals() {
        // Given
        category.setCategoryId(1);
        category.setCategoryName("Electronics");
        category.setDescription("Electronic devices");

        // When & Then - Same object should have same hashCode
        assertEquals(category.hashCode(), category.hashCode());
        
        // Different objects will have different hashCodes (default Object.hashCode)
        Category otherCategory = new Category();
        otherCategory.setCategoryId(1);
        otherCategory.setCategoryName("Electronics");
        otherCategory.setDescription("Electronic devices");
        
        assertNotEquals(category.hashCode(), otherCategory.hashCode());
    }

    @Test
    void toString_ShouldReturnStringRepresentation() {
        // Given
        category.setCategoryId(1);
        category.setCategoryName("Electronics");
        category.setDescription("Electronic devices");

        // When
        String toString = category.toString();

        // Then
        assertNotNull(toString);
        // Default Object.toString() returns className@hashCode format
        assertTrue(toString.contains("Category"));
        assertTrue(toString.contains("@"));
    }

    @Test
    void allFieldsCanBeNullSafely() {
        // Given
        category.setCategoryId(null);
        category.setCategoryName(null);
        category.setDescription(null);

        // When & Then
        assertNull(category.getCategoryId());
        assertNull(category.getCategoryName());
        assertNull(category.getDescription());
        
        // Should not throw any exceptions
        assertDoesNotThrow(() -> {
            category.toString();
            category.hashCode();
            category.equals(new Category());
        });
    }
}
