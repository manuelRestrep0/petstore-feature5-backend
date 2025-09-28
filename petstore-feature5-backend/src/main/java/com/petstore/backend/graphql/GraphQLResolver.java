package com.petstore.backend.graphql;

import java.util.Collections;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.petstore.backend.dto.LoginResponse;
import com.petstore.backend.dto.PromotionDTO;
import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Product;
import com.petstore.backend.entity.Promotion;
import com.petstore.backend.entity.Role;
import com.petstore.backend.entity.User;
import com.petstore.backend.repository.CategoryRepository;
import com.petstore.backend.repository.ProductRepository;
import com.petstore.backend.repository.PromotionRepository;
import com.petstore.backend.repository.UserRepository;
import com.petstore.backend.service.AuthService;
import com.petstore.backend.service.PromotionService;

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
    public LoginResponse login(@Argument String email, @Argument String password) {
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
                        LoginResponse response = new LoginResponse();
                        response.setSuccess(false);
                        response.setMessage("Invalid credentials");
                        return response;
                    }

                    String token = "jwt-real-" + System.currentTimeMillis();
                    System.out.println("REAL DB: Login successful, token: " + token);
                    
                    LoginResponse response = new LoginResponse();
                    response.setToken(token);
                    response.setUserName(user.getUserName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole() != null ? user.getRole().getRoleName() : "USER");
                    response.setSuccess(true);
                    response.setMessage("Login successful");
                    return response;
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
                    
                    LoginResponse response = new LoginResponse();
                    response.setToken(token);
                    response.setUserName(mockUser.getUserName());
                    response.setEmail(mockUser.getEmail());
                    response.setRole(mockRole.getRoleName());
                    response.setSuccess(true);
                    response.setMessage("Login successful (Mock)");
                    return response;
                }
            }
            
            System.out.println("Login failed - invalid credentials or user not found");
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("Invalid credentials or user not found");
            return response;
            
        } catch (Exception e) {
            System.err.println("Error in GraphQL login: " + e.getMessage());
            e.printStackTrace();
            LoginResponse response = new LoginResponse();
            response.setSuccess(false);
            response.setMessage("Login error: " + e.getMessage());
            return response;
        }
    }

    // === PROMOTION MUTATIONS ===

    @MutationMapping
    public Promotion createPromotion(@Argument PromotionDTO input) {
        try {
            System.out.println("Creating promotion with input: " + input);
            
            // Usar el método existente createPromotion del service
            return promotionService.createPromotion(
                input.getPromotionName(),
                input.getDescription(),
                input.getStartDate().toLocalDate(),
                input.getEndDate().toLocalDate(),
                input.getDiscountPercentage().doubleValue(),
                1, // statusId por defecto
                1, // userId por defecto  
                input.getCategory() != null ? input.getCategory().getCategoryId() : null
            );
        } catch (Exception e) {
            System.err.println("Error creating promotion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create promotion: " + e.getMessage());
        }
    }

    @MutationMapping
    public Promotion updatePromotion(@Argument Integer id, @Argument PromotionDTO input) {
        try {
            System.out.println("Updating promotion " + id + " with input: " + input);
            
            // Usar el método existente updatePromotion del service
            Promotion updated = promotionService.updatePromotion(
                id,
                input.getPromotionName(),
                input.getDescription(),
                input.getStartDate().toLocalDate(),
                input.getEndDate().toLocalDate(),
                input.getDiscountPercentage().doubleValue(),
                1, // statusId por defecto
                1, // userId por defecto
                input.getCategory() != null ? input.getCategory().getCategoryId() : null
            );
            

            
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
    // Nota: Con los DTOs de respuesta, estas relaciones ya están aplanadas
    // Estos métodos pueden ser opcionales si usas solo los DTOs de respuesta
    
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
