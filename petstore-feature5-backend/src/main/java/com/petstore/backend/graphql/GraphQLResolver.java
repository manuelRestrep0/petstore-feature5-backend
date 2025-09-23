package com.petstore.backend.graphql;

import com.petstore.backend.entity.*;
import com.petstore.backend.dto.GraphQLLoginResponse;
import com.petstore.backend.dto.PromotionInput;
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

import java.util.List;
import java.util.Collections;

@Controller
public class GraphQLResolver {

    private final PromotionService promotionService;
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
    public GraphQLLoginResponse login(@Argument String email, @Argument String password) {
        try {
            System.out.println("GraphQL Login attempt for email: " + email);
            
            // Intentar primero con BD real
            try {
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    System.out.println("REAL DB: User found: " + user.getEmail() + ", Role: " + 
                        (user.getRole() != null ? user.getRole().getRoleName() : "NULL"));
                    System.out.println("REAL DB: User password from DB: " + user.getPassword());
                    System.out.println("REAL DB: Password provided by user: " + password);

                    // Verificar contraseña - comparar con la contraseña almacenada en la BD
                    // Por ahora las contraseñas están en texto plano para pruebas
                    if (!user.getPassword().equals(password)) {
                        System.out.println("REAL DB: Authentication failed for user: " + email + " - incorrect password");
                        return new GraphQLLoginResponse("", null, false);
                    }

                    String token = "jwt-real-" + System.currentTimeMillis();
                    System.out.println("REAL DB: Login successful, token: " + token);
                    
                    return new GraphQLLoginResponse(token, user, true);
                }
            } catch (Exception dbError) {
                System.out.println("BD CONNECTION ERROR: " + dbError.getMessage());
                System.out.println("FALLBACK: Using mock data due to DB connection issues");
                
                // FALLBACK: Mock data cuando BD no está disponible
                if ("admin@marketing.com".equals(email) && "admin123".equals(password)) {
                    
                    // Crear usuario mock
                    User mockUser = new User();
                    mockUser.setUserId(1);
                    mockUser.setUserName("Marketing Admin (Mock)");
                    mockUser.setEmail("admin@marketing.com");
                    
                    // Crear rol mock
                    Role mockRole = new Role();
                    mockRole.setRoleId(1);
                    mockRole.setRoleName("Marketing Admin");
                    mockUser.setRole(mockRole);
                    
                    String token = "jwt-mock-fallback-" + System.currentTimeMillis();
                    System.out.println("MOCK FALLBACK: Login successful, token: " + token);
                    
                    return new GraphQLLoginResponse(token, mockUser, true);
                }
            }
            
            System.out.println("Login failed - invalid credentials or user not found");
            return new GraphQLLoginResponse("", null, false);
            
        } catch (Exception e) {
            System.err.println("Error in GraphQL login: " + e.getMessage());
            e.printStackTrace();
            return new GraphQLLoginResponse("", null, false);
        }
    }

    // === PROMOTION MUTATIONS ===

    @MutationMapping
    public Promotion createPromotion(@Argument PromotionInput input) {
        try {
            System.out.println("Creating promotion with input: " + input);
            
            return promotionService.createPromotion(
                input.getPromotionName(),
                input.getDescription(),
                input.getStartDateAsLocalDate(),
                input.getEndDateAsLocalDate(),
                input.getDiscountValue(),
                input.getStatusId(),
                input.getUserId(),
                input.getCategoryId()
            );
        } catch (Exception e) {
            System.err.println("Error creating promotion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create promotion: " + e.getMessage());
        }
    }

    @MutationMapping
    public Promotion updatePromotion(@Argument Integer id, @Argument PromotionInput input) {
        try {
            System.out.println("Updating promotion " + id + " with input: " + input);
            
            Promotion updated = promotionService.updatePromotion(
                id,
                input.getPromotionName(),
                input.getDescription(),
                input.getStartDateAsLocalDate(),
                input.getEndDateAsLocalDate(),
                input.getDiscountValue(),
                input.getStatusId(),
                input.getUserId(),
                input.getCategoryId()
            );
            
            if (updated == null) {
                throw new RuntimeException("Promotion not found with id: " + id);
            }
            
            return updated;
        } catch (Exception e) {
            System.err.println("Error updating promotion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update promotion: " + e.getMessage());
        }
    }

    @MutationMapping
    public Boolean deletePromotion(@Argument Integer id) {
        try {
            System.out.println("Deleting promotion with id: " + id);
            
            boolean deleted = promotionService.deletePromotion(id);
            
            if (!deleted) {
                throw new RuntimeException("Promotion not found with id: " + id);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting promotion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete promotion: " + e.getMessage());
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
