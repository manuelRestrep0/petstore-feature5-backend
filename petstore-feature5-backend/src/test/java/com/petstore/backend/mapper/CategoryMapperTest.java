package com.petstore.backend.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.petstore.backend.dto.CategoryDTO;
import com.petstore.backend.entity.Category;

@SpringBootTest
@ActiveProfiles("test")
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    private Category testCategory;
    private CategoryDTO testCategoryDTO;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setCategoryId(1);
        testCategory.setCategoryName("Electronics");
        testCategory.setDescription("Electronic devices and accessories");

        testCategoryDTO = new CategoryDTO();
        testCategoryDTO.setCategoryId(1);
        testCategoryDTO.setCategoryName("Electronics");
        testCategoryDTO.setDescription("Electronic devices and accessories");
    }

    @Test
    void toDTO_ShouldConvertCategoryToCategoryDTO() {
        // When
        CategoryDTO result = categoryMapper.toDTO(testCategory);

        // Then
        assertNotNull(result);
        assertEquals(testCategory.getCategoryId(), result.getCategoryId());
        assertEquals(testCategory.getCategoryName(), result.getCategoryName());
        assertEquals(testCategory.getDescription(), result.getDescription());
    }

    @Test
    void toDTO_ShouldHandleNullCategory() {
        // When
        CategoryDTO result = categoryMapper.toDTO(null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntity_ShouldConvertCategoryDTOToCategory() {
        // When
        Category result = categoryMapper.toEntity(testCategoryDTO);

        // Then
        assertNotNull(result);
        assertEquals(testCategoryDTO.getCategoryId(), result.getCategoryId());
        assertEquals(testCategoryDTO.getCategoryName(), result.getCategoryName());
        assertEquals(testCategoryDTO.getDescription(), result.getDescription());
    }

    @Test
    void toEntity_ShouldHandleNullCategoryDTO() {
        // When
        Category result = categoryMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDTOList_ShouldConvertCategoryListToCategoryDTOList() {
        // Given
        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setCategoryName("Books");
        category2.setDescription("Books and magazines");

        List<Category> categories = Arrays.asList(testCategory, category2);

        // When
        List<CategoryDTO> result = categoryMapper.toDTOList(categories);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());
        assertEquals("Books", result.get(1).getCategoryName());
    }

    @Test
    void toDTOList_ShouldHandleNullList() {
        // When
        List<CategoryDTO> result = categoryMapper.toDTOList(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDTOList_ShouldHandleEmptyList() {
        // When
        List<CategoryDTO> result = categoryMapper.toDTOList(Arrays.asList());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void mapperInstance_ShouldNotBeNull() {
        // Then
        assertNotNull(CategoryMapper.INSTANCE);
    }

    @Test
    void toEntityList_ShouldConvertCategoryDTOListToCategoryList() {
        // Given
        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setCategoryId(2);
        categoryDTO2.setCategoryName("Books");
        categoryDTO2.setDescription("Books and magazines");

        List<CategoryDTO> categoryDTOs = Arrays.asList(testCategoryDTO, categoryDTO2);

        // When
        List<Category> result = categoryMapper.toEntityList(categoryDTOs);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());
        assertEquals("Books", result.get(1).getCategoryName());
    }
}
