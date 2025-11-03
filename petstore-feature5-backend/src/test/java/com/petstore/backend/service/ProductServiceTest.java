package com.petstore.backend.service;

import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Product;
import com.petstore.backend.repository.ProductRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;
    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setCategoryId(1);
        mockCategory.setCategoryName("Electronics");

        mockProduct = new Product();
        mockProduct.setProductId(1);
        mockProduct.setProductName("Test Product");
        mockProduct.setBasePrice(99.99);
        mockProduct.setSku(12345);
        mockProduct.setCategory(mockCategory);
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        // Given
        List<Product> expectedProducts = Arrays.asList(mockProduct);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // When
        List<Product> result = productService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockProduct, result.get(0));
        verify(productRepository).findAll();
    }

    @Test
    void findByCategoryId_ShouldReturnProductsForCategory() {
        // Given
        Integer categoryId = 1;
        List<Product> expectedProducts = Arrays.asList(mockProduct);
        when(productRepository.findByCategoryCategoryId(categoryId)).thenReturn(expectedProducts);

        // When
        List<Product> result = productService.findByCategoryId(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockProduct, result.get(0));
        verify(productRepository).findByCategoryCategoryId(categoryId);
    }

    @Test
    void findById_WithExistingId_ShouldReturnProduct() {
        // Given
        Integer productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // When
        Optional<Product> result = productService.findById(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(mockProduct, result.get());
        verify(productRepository).findById(productId);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Given
        Integer productId = 999;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.findById(productId);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository).findById(productId);
    }

    @Test
    void save_ShouldReturnSavedProduct() {
        // Given
        when(productRepository.save(mockProduct)).thenReturn(mockProduct);

        // When
        Product result = productService.save(mockProduct);

        // Then
        assertNotNull(result);
        assertEquals(mockProduct, result);
        verify(productRepository).save(mockProduct);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Given
        Integer productId = 1;

        // When
        productService.deleteById(productId);

        // Then
        verify(productRepository).deleteById(productId);
    }

    @Test
    void findByNameContaining_ShouldReturnMatchingProducts() {
        // Given
        String searchName = "Test";
        List<Product> expectedProducts = Arrays.asList(mockProduct);
        when(productRepository.findByProductNameContainingIgnoreCase(searchName))
                .thenReturn(expectedProducts);

        // When
        List<Product> result = productService.findByNameContaining(searchName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockProduct, result.get(0));
        verify(productRepository).findByProductNameContainingIgnoreCase(searchName);
    }

    @Test
    void findByPriceBetween_ShouldReturnProductsInPriceRange() {
        // Given
        Double minPrice = 50.0;
        Double maxPrice = 150.0;
        List<Product> expectedProducts = Arrays.asList(mockProduct);
        when(productRepository.findByBasePriceBetween(minPrice, maxPrice))
                .thenReturn(expectedProducts);

        // When
        List<Product> result = productService.findByPriceBetween(minPrice, maxPrice);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockProduct, result.get(0));
        verify(productRepository).findByBasePriceBetween(minPrice, maxPrice);
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Given
        Integer productId = 1;
        when(productRepository.existsById(productId)).thenReturn(true);

        // When
        boolean result = productService.existsById(productId);

        // Then
        assertTrue(result);
        verify(productRepository).existsById(productId);
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Given
        Integer productId = 999;
        when(productRepository.existsById(productId)).thenReturn(false);

        // When
        boolean result = productService.existsById(productId);

        // Then
        assertFalse(result);
        verify(productRepository).existsById(productId);
    }

    @Test
    void count_ShouldReturnTotalProductCount() {
        // Given
        long expectedCount = 5L;
        when(productRepository.count()).thenReturn(expectedCount);

        // When
        long result = productService.count();

        // Then
        assertEquals(expectedCount, result);
        verify(productRepository).count();
    }

    @Test
    void findByCategoryId_WithNullCategoryId_ShouldHandleGracefully() {
        // Given
        Integer categoryId = null;
        when(productRepository.findByCategoryCategoryId(categoryId)).thenReturn(Arrays.asList());

        // When
        List<Product> result = productService.findByCategoryId(categoryId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findByCategoryCategoryId(categoryId);
    }

    @Test
    void findByNameContaining_WithEmptyString_ShouldReturnResults() {
        // Given
        String searchName = "";
        List<Product> expectedProducts = Arrays.asList(mockProduct);
        when(productRepository.findByProductNameContainingIgnoreCase(searchName))
                .thenReturn(expectedProducts);

        // When
        List<Product> result = productService.findByNameContaining(searchName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByProductNameContainingIgnoreCase(searchName);
    }

    @Test
    void findByPriceBetween_WithEqualMinAndMaxPrice_ShouldReturnResults() {
        // Given
        Double price = 99.99;
        List<Product> expectedProducts = Arrays.asList(mockProduct);
        when(productRepository.findByBasePriceBetween(price, price))
                .thenReturn(expectedProducts);

        // When
        List<Product> result = productService.findByPriceBetween(price, price);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByBasePriceBetween(price, price);
    }
}
