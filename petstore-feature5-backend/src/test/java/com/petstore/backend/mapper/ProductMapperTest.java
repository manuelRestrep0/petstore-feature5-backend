package com.petstore.backend.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.petstore.backend.dto.ProductDTO;
import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Product;

@SpringBootTest
@ActiveProfiles("test")
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    private Product testProduct;
    private ProductDTO testProductDTO;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setCategoryId(1);
        testCategory.setCategoryName("Electronics");
        testCategory.setDescription("Electronic devices");

        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setProductName("Laptop");
        testProduct.setBasePrice(999.99);
        testProduct.setSku(12345);
        testProduct.setCategory(testCategory);

        testProductDTO = new ProductDTO();
        testProductDTO.setProductId(1);
        testProductDTO.setProductName("Laptop");
        testProductDTO.setPrice(BigDecimal.valueOf(999.99));
        testProductDTO.setStock(10);
        testProductDTO.setImageUrl("http://example.com/laptop.jpg");
        testProductDTO.setDescription("Gaming laptop");
        testProductDTO.setCreatedAt(LocalDateTime.now());
        testProductDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void toDTO_ShouldConvertProductToProductDTO() {
        // When
        ProductDTO result = productMapper.toDTO(testProduct);

        // Then
        assertNotNull(result);
        assertEquals(testProduct.getProductId(), result.getProductId());
        assertEquals(testProduct.getProductName(), result.getProductName());
        assertEquals(BigDecimal.valueOf(testProduct.getBasePrice()), result.getPrice());
        assertNotNull(result.getCategory());
        assertEquals(testProduct.getCategory().getCategoryId(), result.getCategory().getCategoryId());
    }

    @Test
    void toDTO_ShouldHandleNullProduct() {
        // When
        ProductDTO result = productMapper.toDTO(null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntity_ShouldConvertProductDTOToProduct() {
        // When
        Product result = productMapper.toEntity(testProductDTO);

        // Then
        assertNotNull(result);
        assertEquals(testProductDTO.getProductName(), result.getProductName());
        assertEquals(testProductDTO.getPrice().doubleValue(), result.getBasePrice());
        assertEquals(testProductDTO.getCategory(), result.getCategory());
    }

    @Test
    void toEntity_ShouldHandleNullProductDTO() {
        // When
        Product result = productMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDTOList_ShouldConvertProductListToProductDTOList() {
        // Given
        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("Mouse");
        product2.setBasePrice(29.99);
        product2.setSku(67890);
        product2.setCategory(testCategory);

        List<Product> products = Arrays.asList(testProduct, product2);

        // When
        List<ProductDTO> result = productMapper.toDTOList(products);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        assertEquals("Mouse", result.get(1).getProductName());
    }

    @Test
    void toDTOList_ShouldHandleNullList() {
        // When
        List<ProductDTO> result = productMapper.toDTOList(null);

        // Then
        assertNull(result);
    }

    @Test
    void toDTOList_ShouldHandleEmptyList() {
        // When
        List<ProductDTO> result = productMapper.toDTOList(Arrays.asList());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void mapperInstance_ShouldNotBeNull() {
        // Then
        assertNotNull(ProductMapper.INSTANCE);
    }
}
