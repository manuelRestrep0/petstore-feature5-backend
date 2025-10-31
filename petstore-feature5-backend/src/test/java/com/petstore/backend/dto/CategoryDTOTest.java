package com.petstore.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryDTOTest {

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO();
    }

    @Test
    void testDefaultConstructor() {
        // When
        CategoryDTO dto = new CategoryDTO();

        // Then
        assertNotNull(dto);
    }

    @Test
    void testParameterizedConstructor() {
        // When
        CategoryDTO dto = new CategoryDTO(1, "Electronics", "Electronic devices and accessories");

        // Then
        assertNotNull(dto);
        assertEquals(1, dto.getCategoryId());
        assertEquals("Electronics", dto.getCategoryName());
        assertEquals("Electronic devices and accessories", dto.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        Integer categoryId = 1;
        String categoryName = "Books";
        String description = "Books and literature";

        // When
        categoryDTO.setCategoryId(categoryId);
        categoryDTO.setCategoryName(categoryName);
        categoryDTO.setDescription(description);

        // Then
        assertEquals(categoryId, categoryDTO.getCategoryId());
        assertEquals(categoryName, categoryDTO.getCategoryName());
        assertEquals(description, categoryDTO.getDescription());
    }

    @Test
    void testSetCategoryId() {
        // When
        categoryDTO.setCategoryId(10);

        // Then
        assertEquals(10, categoryDTO.getCategoryId());
    }

    @Test
    void testSetCategoryName() {
        // When
        categoryDTO.setCategoryName("Sports");

        // Then
        assertEquals("Sports", categoryDTO.getCategoryName());
    }

    @Test
    void testSetDescription() {
        // When
        categoryDTO.setDescription("Sports equipment and gear");

        // Then
        assertEquals("Sports equipment and gear", categoryDTO.getDescription());
    }

    @Test
    void testToString() {
        // Given
        categoryDTO.setCategoryId(1);
        categoryDTO.setCategoryName("Technology");
        categoryDTO.setDescription("Tech products");

        // When
        String result = categoryDTO.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("CategoryDTO"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        CategoryDTO dto1 = new CategoryDTO(1, "Electronics", "Electronic devices");
        CategoryDTO dto2 = new CategoryDTO(1, "Electronics", "Electronic devices");
        CategoryDTO dto3 = new CategoryDTO(2, "Books", "Literature");

        // Then - Since CategoryDTO doesn't override equals/hashCode, they use reference equality
        assertNotEquals(dto1, dto2); // Different object instances
        assertEquals(dto1, dto1); // Same reference
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testEqualsWithNull() {
        // Given
        CategoryDTO dto = new CategoryDTO(1, "Electronics", "Electronic devices");

        // Then
        assertNotNull(dto);
        assertFalse(dto.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        CategoryDTO dto = new CategoryDTO(1, "Electronics", "Electronic devices");
        String notADTO = "Not a DTO";

        // Then
        assertNotEquals(dto, notADTO);
    }

    @Test
    void testEqualsWithSameReference() {
        // Given
        CategoryDTO dto = new CategoryDTO(1, "Electronics", "Electronic devices");

        // Then
        assertEquals(dto, dto);
        assertTrue(dto.equals(dto));
    }

    @Test
    void testSettersWithNullValues() {
        // When
        categoryDTO.setCategoryId(null);
        categoryDTO.setCategoryName(null);
        categoryDTO.setDescription(null);

        // Then
        assertEquals(null, categoryDTO.getCategoryId());
        assertEquals(null, categoryDTO.getCategoryName());
        assertEquals(null, categoryDTO.getDescription());
    }

    @Test
    void testSettersWithEmptyStrings() {
        // When
        categoryDTO.setCategoryName("");
        categoryDTO.setDescription("");

        // Then
        assertEquals("", categoryDTO.getCategoryName());
        assertEquals("", categoryDTO.getDescription());
    }

    @Test
    void testBuilderPattern() {
        // Given & When
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(1);
        dto.setCategoryName("Home & Garden");
        dto.setDescription("Items for home and garden");

        // Then
        assertEquals(1, dto.getCategoryId());
        assertEquals("Home & Garden", dto.getCategoryName());
        assertEquals("Items for home and garden", dto.getDescription());
    }
}
