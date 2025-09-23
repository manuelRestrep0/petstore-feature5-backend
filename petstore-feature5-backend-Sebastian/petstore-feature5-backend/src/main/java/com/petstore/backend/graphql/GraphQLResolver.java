package com.petstore.backend.graphql;

import com.petstore.backend.entity.*;
import com.petstore.backend.service.AuthService;
import com.petstore.backend.service.PromotionService;
import com.petstore.backend.repository.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import com.petstore.backend.dto.CreatePromotionInput;

import java.util.List;
import java.util.Map;
import java.util.Collections;

@Controller
public class GraphQLResolver {

    private final PromotionService promotionService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public GraphQLResolver(
            PromotionService promotionService,
            AuthService authService,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            PromotionRepository promotionRepository) {
        this.promotionService = promotionService;
        this.authService = authService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    // === QUERIES ===

    @QueryMapping
    public String health() {
        return "GraphQL API is running! " + java.time.LocalDateTime.now();
    }

    @QueryMapping
    public User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }

    @QueryMapping
    public List<Promotion> promotions() {
        try {
            return promotionRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all promotions: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping
    public List<Promotion> promotionsActive() {
        try {
            return promotionService.getAllActivePromotionsEntities();
        } catch (Exception e) {
            System.err.println("Error getting active promotions: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping
    public List<Promotion> promotionsByCategory(@Argument Integer categoryId) {
        try {
            return promotionService.getPromotionsByCategoryEntities(categoryId);
        } catch (Exception e) {
            System.err.println("Error getting promotions by category: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping
    public Promotion promotion(@Argument Integer id) {
        try {
            return promotionRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting promotion by id: " + e.getMessage());
            return null;
        }
    }

    @QueryMapping
    public List<Category> categories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting categories: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping
    public Category category(@Argument Integer id) {
        try {
            return categoryRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting category by id: " + e.getMessage());
            return null;
        }
    }

    @QueryMapping
    public List<Product> products() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error getting products: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Integer categoryId) {
        try {
            return productRepository.findByCategoryCategoryId(categoryId);
        } catch (Exception e) {
            System.err.println("Error getting products by category: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @QueryMapping
    public Product product(@Argument Integer id) {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting product by id: " + e.getMessage());
            return null;
        }
    }

    // === MUTATIONS ===

    @MutationMapping
    public Map<String, Object> login(@Argument String email, @Argument String password) {
        try {
            return authService.login(email, password);
        } catch (Exception e) {
            System.err.println("Error in login: " + e.getMessage());
            return Map.of(
                "success", false,
                "token", "",
                "user", Map.of()
            );
        }
    }
    @MutationMapping
    public Promotion createPromotion(@Argument CreatePromotionInput input) {
        try {
            return promotionService.createPromotion(input);
        } catch (Exception e) {
            System.err.println("Error creating promotion: " + e.getMessage());
            throw e;
        }
    }

    // === SCHEMA MAPPINGS para resolver relaciones ===

    @SchemaMapping(typeName = "Promotion", field = "products")
    public List<Product> promotionProducts(Promotion promotion) {
        try {
            return productRepository.findByPromotionPromotionId(promotion.getPromotionId());
        } catch (Exception e) {
            System.err.println("Error getting products for promotion: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @SchemaMapping(typeName = "Category", field = "promotions")
    public List<Promotion> categoryPromotions(Category category) {
        try {
            return promotionService.getPromotionsByCategoryEntities(category.getCategoryId());
        } catch (Exception e) {
            System.err.println("Error getting promotions for category: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @SchemaMapping(typeName = "Category", field = "products")
    public List<Product> categoryProducts(Category category) {
        try {
            return productRepository.findByCategoryCategoryId(category.getCategoryId());
        } catch (Exception e) {
            System.err.println("Error getting products for category: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
