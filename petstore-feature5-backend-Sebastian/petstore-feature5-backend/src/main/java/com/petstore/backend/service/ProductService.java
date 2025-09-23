package com.petstore.backend.service;

import com.petstore.backend.dto.CategoryDTO;
import com.petstore.backend.dto.ProductDTO;
import com.petstore.backend.dto.PromotionDTO;
import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Product;
import com.petstore.backend.repository.CategoryRepository;
import com.petstore.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Obtiene todos los productos por categoría usando el ID de la categoría
     */
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) {
        List<Product> products = productRepository.findByCategoryCategoryId(categoryId);
        
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los productos por nombre de categoría
     */
    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
        
        if (categoryOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada: " + categoryName);
        }
        
        Category category = categoryOpt.get();
        List<Product> products = productRepository.findByCategoryCategoryId(category.getCategoryId());
        
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los productos
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Product a ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setBasePrice(product.getBasePrice());
        dto.setSku(product.getSku());
        
        // Convertir categoría
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(product.getCategory().getCategoryId());
            categoryDTO.setCategoryName(product.getCategory().getCategoryName());
            categoryDTO.setDescription(product.getCategory().getDescription());
            dto.setCategory(categoryDTO);
        }
        
        // Convertir promoción si existe
        if (product.getPromotion() != null) {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setPromotionId(product.getPromotion().getPromotionId());
            promotionDTO.setPromotionName(product.getPromotion().getPromotionName());
            promotionDTO.setDescription(product.getPromotion().getDescription());
            promotionDTO.setDiscountPercentage(java.math.BigDecimal.valueOf(product.getPromotion().getDiscountValue()));
            
            // Convertir fechas
            if (product.getPromotion().getStartDate() != null) {
                promotionDTO.setStartDate(product.getPromotion().getStartDate().atStartOfDay());
            }
            if (product.getPromotion().getEndDate() != null) {
                promotionDTO.setEndDate(product.getPromotion().getEndDate().atTime(23, 59, 59));
            }
            
            // Convertir status
            if (product.getPromotion().getStatus() != null) {
                promotionDTO.setStatus(product.getPromotion().getStatus().getStatusName());
            }
            
            // Convertir categoría de la promoción
            if (product.getPromotion().getCategory() != null) {
                CategoryDTO promotionCategoryDTO = new CategoryDTO();
                promotionCategoryDTO.setCategoryId(product.getPromotion().getCategory().getCategoryId());
                promotionCategoryDTO.setCategoryName(product.getPromotion().getCategory().getCategoryName());
                promotionCategoryDTO.setDescription(product.getPromotion().getCategory().getDescription());
                promotionDTO.setCategory(promotionCategoryDTO);
            }
            
            dto.setPromotion(promotionDTO);
        }
        
        return dto;
    }
}
