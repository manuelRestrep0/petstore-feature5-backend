package com.petstore.backend.service;

import com.petstore.backend.entity.Category;
import com.petstore.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setCategoryId(1);
        mockCategory.setCategoryName("Electronics");
    }

    @Test
    void findAll_ShouldReturnAllCategories() {
        // Given
        List<Category> expectedCategories = Arrays.asList(mockCategory);
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // When
        List<Category> result = categoryService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockCategory, result.get(0));
        verify(categoryRepository).findAll();
    }

    @Test
    void findById_WithExistingId_ShouldReturnCategory() {
        // Given
        Integer categoryId = 1;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        // When
        Optional<Category> result = categoryService.findById(categoryId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockCategory, result.get());
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        Integer categoryId = 999;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Optional<Category> result = categoryService.findById(categoryId);

        // Then
        assertFalse(result.isPresent());
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void save_ShouldReturnSavedCategory() {
        // Given
        when(categoryRepository.save(mockCategory)).thenReturn(mockCategory);

        // When
        Category result = categoryService.save(mockCategory);

        // Then
        assertNotNull(result);
        assertEquals(mockCategory, result);
        verify(categoryRepository).save(mockCategory);
    }

    @Test
    void save_WithNewCategory_ShouldReturnSavedCategory() {
        // Given
        Category newCategory = new Category();
        newCategory.setCategoryName("Books");
        
        Category savedCategory = new Category();
        savedCategory.setCategoryId(2);
        savedCategory.setCategoryName("Books");
        
        when(categoryRepository.save(newCategory)).thenReturn(savedCategory);

        // When
        Category result = categoryService.save(newCategory);

        // Then
        assertNotNull(result);
        assertEquals(savedCategory, result);
        assertEquals("Books", result.getCategoryName());
        assertEquals(2, result.getCategoryId());
        verify(categoryRepository).save(newCategory);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Given
        Integer categoryId = 1;

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Given
        Integer categoryId = 1;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // When
        boolean result = categoryService.existsById(categoryId);

        // Then
        assertTrue(result);
        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Given
        Integer categoryId = 999;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // When
        boolean result = categoryService.existsById(categoryId);

        // Then
        assertFalse(result);
        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void findAll_WithEmptyRepository_ShouldReturnEmptyList() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Category> result = categoryService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll();
    }

    @Test
    void findAll_WithMultipleCategories_ShouldReturnAllCategories() {
        // Given
        Category category1 = new Category();
        category1.setCategoryId(1);
        category1.setCategoryName("Electronics");
        
        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setCategoryName("Books");
        
        List<Category> expectedCategories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        // When
        List<Category> result = categoryService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(category1, result.get(0));
        assertEquals(category2, result.get(1));
        verify(categoryRepository).findAll();
    }
}
