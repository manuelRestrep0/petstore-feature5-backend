package com.petstore.backend.mapper;

import com.petstore.backend.dto.ProductResponseDTO;
import com.petstore.backend.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para transformaciones Product <-> ProductResponseDTO
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    /**
     * Convierte una entidad Product a ProductResponseDTO
     * Aplana las relaciones para evitar lazy loading en GraphQL
     */
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "promotionName", source = "promotion.promotionName")
    @Mapping(target = "discountValue", source = "promotion.discountValue")
    ProductResponseDTO toResponseDTO(Product product);

    /**
     * Convierte una lista de entidades Product a lista de ProductResponseDTO
     */
    java.util.List<ProductResponseDTO> toResponseDTOList(java.util.List<Product> products);
}
