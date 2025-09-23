package com.petstore.backend.service;

import com.petstore.backend.dto.CategoryDTO;
import com.petstore.backend.dto.PromotionDTO;
import com.petstore.backend.entity.Promotion;
import com.petstore.backend.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.petstore.backend.dto.CreatePromotionInput;
import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Status;
import com.petstore.backend.entity.User;
import com.petstore.backend.repository.CategoryRepository;
import com.petstore.backend.repository.StatusRepository;
import com.petstore.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Locale;


@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todas las promociones activas y vigentes
     */
    public List<PromotionDTO> getAllActivePromotions() {
        LocalDate today = LocalDate.now();
        
        // Buscar promociones activas
        List<Promotion> activePromotions = promotionRepository.findActivePromotions();
        
        // Filtrar las que están vigentes (fecha actual entre start y end)
        return activePromotions.stream()
                .filter(promotion -> !today.isBefore(promotion.getStartDate()) && !today.isAfter(promotion.getEndDate()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las promociones (activas e inactivas) para administración
     */
    public List<PromotionDTO> getAllPromotions() {
        List<Promotion> allPromotions = promotionRepository.findAll();
        
        return allPromotions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene promociones por categoría
     */
    public List<PromotionDTO> getPromotionsByCategory(Integer categoryId) {
        List<Promotion> promotions = promotionRepository.findByCategoryCategoryId(categoryId);
        
        return promotions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene promociones vigentes para la fecha actual
     */
    public List<PromotionDTO> getValidPromotions() {
        LocalDate today = LocalDate.now();
        List<Promotion> promotions = promotionRepository.findValidPromotions(today);
        
        return promotions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Promotion a PromotionDTO
     */
    private PromotionDTO convertToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        
        dto.setPromotionId(promotion.getPromotionId());
        dto.setPromotionName(promotion.getPromotionName());
        dto.setDescription(promotion.getDescription());
        
        // Convertir discount value de Double a BigDecimal
        if (promotion.getDiscountValue() != null) {
            dto.setDiscountPercentage(BigDecimal.valueOf(promotion.getDiscountValue()));
        }
        
        // Convertir LocalDate a LocalDateTime (agregando hora 00:00:00)
        if (promotion.getStartDate() != null) {
            dto.setStartDate(promotion.getStartDate().atStartOfDay());
        }
        if (promotion.getEndDate() != null) {
            dto.setEndDate(promotion.getEndDate().atTime(23, 59, 59));
        }
        
        // Nota: Las entidades Promotion no tienen createdAt/updatedAt en el esquema actual
        // Esto se puede agregar más adelante si es necesario
        
        // Convertir status
        if (promotion.getStatus() != null) {
            dto.setStatus(promotion.getStatus().getStatusName());
        }
        
        // Convertir category si existe
        if (promotion.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(promotion.getCategory().getCategoryId());
            categoryDTO.setCategoryName(promotion.getCategory().getCategoryName());
            categoryDTO.setDescription(promotion.getCategory().getDescription());
            dto.setCategory(categoryDTO);
        }
        
        // Nota: La entidad Promotion actual no tiene relación directa con Product
        // Solo tiene relación con Category, que puede contener productos
        dto.setProduct(null);
        
        return dto;
    }

    // === MÉTODOS PARA GRAPHQL que retornan entidades directamente ===

    /**
     * Obtiene todas las promociones activas como entidades para GraphQL
     */
    public List<Promotion> getAllActivePromotionsEntities() {
        LocalDate today = LocalDate.now();
        
        // Buscar promociones activas
        List<Promotion> activePromotions = promotionRepository.findActivePromotions();
        
        // Filtrar las que están vigentes (fecha actual entre start y end)
        return activePromotions.stream()
                .filter(promotion -> !today.isBefore(promotion.getStartDate()) && !today.isAfter(promotion.getEndDate()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene promociones por categoría como entidades para GraphQL
     */
    public List<Promotion> getPromotionsByCategoryEntities(Integer categoryId) {
        return promotionRepository.findByCategoryCategoryId(categoryId);
    }

    /**
     * Obtiene una promoción por ID como entidad para GraphQL
     */
    public Promotion getPromotionByIdEntity(Integer id) {
        return promotionRepository.findById(id).orElse(null);
    }

    /**
     * Crea una nueva promoción desde la entrada DTO
     */
    public Promotion createPromotion(CreatePromotionInput input) {
        // 1) Autenticación y autorización
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("No autenticado");
        }
        // Verificamos que el usuario sea Marketing Admin
        User current = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (current.getRole() == null || !"Marketing Admin".equals(current.getRole().getRoleName())) {
            throw new RuntimeException("No autorizado: se requiere rol Marketing Admin");
        }

        // 2) Validaciones de entrada
        if (input.getPromotionName() == null || input.getPromotionName().isBlank()) {
            throw new RuntimeException("promotionName es requerido");
        }
        if (input.getDiscountValue() == null || input.getDiscountValue() <= 0) {
            throw new RuntimeException("discountValue debe ser > 0");
        }
        if (input.getCategoryId() == null) {
            throw new RuntimeException("categoryId es requerido");
        }

        LocalDate start = LocalDate.parse(input.getStartDate());
        LocalDate end   = LocalDate.parse(input.getEndDate());
        if (end.isBefore(start)) {
            throw new RuntimeException("endDate no puede ser anterior a startDate");
        }

        // 3) Entidades relacionadas
        Category category = categoryRepository.findById(input.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // 4) Status (solo se permiten ACTIVE, EXPIRED, SCHEDULED)
        String statusKey;
        if (input.getStatusName() != null && !input.getStatusName().isBlank()) {
            statusKey = input.getStatusName().toUpperCase(Locale.ROOT);
        } else {
            // Si no viene, lo inferimos por fechas
            LocalDate today = LocalDate.now();
            if (end.isBefore(today)) statusKey = "EXPIRED";
            else if (start.isAfter(today)) statusKey = "SCHEDULED";
            else statusKey = "ACTIVE";
        }
        if (!statusKey.equals("ACTIVE") && !statusKey.equals("EXPIRED") && !statusKey.equals("SCHEDULED")) {
            throw new RuntimeException("statusName inválido");
        }
        Status status = statusRepository.findByStatusName(statusKey)
                .orElseThrow(() -> new RuntimeException("Status no existe: " + statusKey));

        // 5) Construir y persistir
        Promotion p = new Promotion();
        p.setPromotionName(input.getPromotionName());
        p.setDescription(input.getDescription());
        p.setStartDate(start);
        p.setEndDate(end);
        p.setDiscountValue(input.getDiscountValue());
        p.setStatus(status);
        p.setCategory(category);
        p.setUser(current);

        return promotionRepository.save(p);
    }
}
