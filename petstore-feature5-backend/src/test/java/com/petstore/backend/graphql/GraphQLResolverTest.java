package com.petstore.backend.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.petstore.backend.dto.LoginResponse;
import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Product;
import com.petstore.backend.entity.Promotion;
import com.petstore.backend.entity.Role;
import com.petstore.backend.entity.Status;
import com.petstore.backend.entity.User;
import com.petstore.backend.repository.CategoryRepository;
import com.petstore.backend.repository.ProductRepository;
import com.petstore.backend.repository.PromotionRepository;
import com.petstore.backend.repository.UserRepository;
import com.petstore.backend.service.AuthService;
import com.petstore.backend.service.PromotionService;

class GraphQLResolverTest {

    @Mock
    private PromotionService promotionService;

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private GraphQLResolver graphQLResolver;
    private User testUser;
    private Promotion testPromotion;
    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        graphQLResolver = new GraphQLResolver(
                promotionService,
                authService,
                userRepository,
                categoryRepository,
                productRepository,
                promotionRepository
        );

        // Setup test entities
        Role marketingRole = new Role();
        marketingRole.setRoleId(1);
        marketingRole.setRoleName("MARKETING_ADMIN");

        testUser = new User();
        testUser.setUserId(1);
        testUser.setEmail("admin@petstore.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(marketingRole);

        testCategory = new Category();
        testCategory.setCategoryId(1);
        testCategory.setCategoryName("Electronics");
        testCategory.setDescription("Electronic devices");

        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setProductName("Smartphone");
        testProduct.setBasePrice(599.99);
        testProduct.setSku(12345);
        testProduct.setCategory(testCategory);

        Status activeStatus = new Status();
        activeStatus.setStatusId(1);
        activeStatus.setStatusName("ACTIVE");

        testPromotion = new Promotion();
        testPromotion.setPromotionId(1);
        testPromotion.setPromotionName("Summer Sale");
        testPromotion.setDescription("Great summer discount");
        testPromotion.setDiscountValue(25.0);
        testPromotion.setStartDate(LocalDate.now());
        testPromotion.setEndDate(LocalDate.now().plusDays(30));
        testPromotion.setStatus(activeStatus);
        testPromotion.setUser(testUser);
        testPromotion.setCategory(testCategory);
    }

    @Test
    void health_ShouldReturnHealthMessage() {
        // When
        String result = graphQLResolver.health();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("GraphQL API is running!"));
    }

    @Test
    void promotions_ShouldReturnAllPromotions() {
        // Given
        List<Promotion> promotions = Arrays.asList(testPromotion);
        when(promotionRepository.findAll()).thenReturn(promotions);

        // When
        List<Promotion> result = graphQLResolver.promotions();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Summer Sale", result.get(0).getPromotionName());
    }

    @Test
    void promotions_WhenException_ShouldReturnEmptyList() {
        // Given
        when(promotionRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When
        List<Promotion> result = graphQLResolver.promotions();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void currentUser_WhenAuthenticated_ShouldReturnUser() {
        // Given
        setupAuthenticatedUser();
        when(userRepository.findByEmail("admin@petstore.com")).thenReturn(Optional.of(testUser));

        // When
        User result = graphQLResolver.currentUser();

        // Then
        assertNotNull(result);
        assertEquals("admin@petstore.com", result.getEmail());
    }

    @Test
    void currentUser_WhenNotAuthenticated_ShouldThrowException() {
        // Given
        setupUnauthenticatedUser();

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            graphQLResolver.currentUser();
        });
        assertTrue(exception.getMessage().contains("Authentication required"));
    }

    @Test
    void categories_ShouldReturnAllCategories() {
        // Given
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<Category> result = graphQLResolver.categories();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());
    }

    @Test
    void categories_WhenException_ShouldReturnEmptyList() {
        // Given
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When
        List<Category> result = graphQLResolver.categories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void products_ShouldReturnAllProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = graphQLResolver.products();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Smartphone", result.get(0).getProductName());
    }

    @Test
    void products_WhenException_ShouldReturnEmptyList() {
        // Given
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When
        List<Product> result = graphQLResolver.products();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void login_WithValidCredentials_ShouldReturnLoginResponse() {
        // Given
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(true);
        loginResponse.setToken("valid-token");
        loginResponse.setMessage("Login successful");

        when(authService.authenticateMarketingAdmin(anyString(), anyString()))
                .thenReturn(loginResponse);

        // When
        LoginResponse result = graphQLResolver.login("admin@petstore.com", "password");

        // Then
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("valid-token", result.getToken());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnErrorResponse() {
        // Given
        when(authService.authenticateMarketingAdmin(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // When
        LoginResponse result = graphQLResolver.login("admin@petstore.com", "wrongpassword");

        // Then
        assertNotNull(result);
        assertFalse(result.isSuccess()); // Use assertFalse instead of assertEquals(false, ...)
        assertTrue(result.getMessage().contains("Invalid credentials") || result.getMessage().contains("authentication"));
    }

    @Test
    void createPromotion_WhenAuthenticated_ShouldCreatePromotion() {
        // Given
        setupAuthenticatedUser();
        when(userRepository.findByEmail("admin@petstore.com")).thenReturn(Optional.of(testUser));
        
        com.petstore.backend.dto.PromotionDTO promotionDTO = new com.petstore.backend.dto.PromotionDTO();
        promotionDTO.setPromotionName("Summer Sale");
        promotionDTO.setDescription("Great discount");
        // Fix the null pointer exception by setting required dates and percentage
        promotionDTO.setStartDate(java.time.LocalDateTime.now());
        promotionDTO.setEndDate(java.time.LocalDateTime.now().plusDays(30));
        promotionDTO.setDiscountPercentage(java.math.BigDecimal.valueOf(20.0));
        
        when(promotionService.createPromotion(anyString(), anyString(), any(), any(), any(), any(), any(), any()))
                .thenReturn(testPromotion);

        // When
        Promotion result = graphQLResolver.createPromotion(promotionDTO);

        // Then
        assertNotNull(result);
        assertEquals("Summer Sale", result.getPromotionName());
    }

    @Test
    void createPromotion_WhenNotAuthenticated_ShouldThrowException() {
        // Given
        setupUnauthenticatedUser();
        com.petstore.backend.dto.PromotionDTO promotionDTO = new com.petstore.backend.dto.PromotionDTO();
        promotionDTO.setPromotionName("Summer Sale");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            graphQLResolver.createPromotion(promotionDTO);
        });
        assertTrue(exception.getMessage().contains("Authentication required"));
    }

    private void setupAuthenticatedUser() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("admin@petstore.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private void setupUnauthenticatedUser() {
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authentication.getName()).thenReturn("anonymousUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
