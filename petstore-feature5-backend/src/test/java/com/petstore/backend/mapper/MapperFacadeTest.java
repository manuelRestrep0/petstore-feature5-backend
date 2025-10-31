package com.petstore.backend.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapperFacadeTest {

    @Mock
    private UserMapper userMapper;
    
    @Mock
    private PromotionMapper promotionMapper;
    
    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private CategoryMapper categoryMapper;

    private MapperFacade mapperFacade;

    @BeforeEach
    void setUp() {
        mapperFacade = new MapperFacade(userMapper, promotionMapper, productMapper, categoryMapper);
    }

    @Test
    void constructor_ShouldInitializeAllMappers() {
        // When & Then
        assertNotNull(mapperFacade);
        assertNotNull(mapperFacade.getUserMapper());
        assertNotNull(mapperFacade.getPromotionMapper());
        assertNotNull(mapperFacade.getProductMapper());
        assertNotNull(mapperFacade.getCategoryMapper());
    }

    @Test
    void getUserMapper_ShouldReturnUserMapper() {
        // When
        UserMapper result = mapperFacade.getUserMapper();

        // Then
        assertEquals(userMapper, result);
    }

    @Test
    void getPromotionMapper_ShouldReturnPromotionMapper() {
        // When
        PromotionMapper result = mapperFacade.getPromotionMapper();

        // Then
        assertEquals(promotionMapper, result);
    }

    @Test
    void getProductMapper_ShouldReturnProductMapper() {
        // When
        ProductMapper result = mapperFacade.getProductMapper();

        // Then
        assertEquals(productMapper, result);
    }

    @Test
    void getCategoryMapper_ShouldReturnCategoryMapper() {
        // When
        CategoryMapper result = mapperFacade.getCategoryMapper();

        // Then
        assertEquals(categoryMapper, result);
    }
}
