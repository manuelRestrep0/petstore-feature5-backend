package com.petstore.backend.mapper;

import com.petstore.backend.dto.PromotionInput;
import com.petstore.backend.dto.PromotionResponseDTO;
import com.petstore.backend.entity.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para transformaciones Promotion <-> DTOs
 */
@Mapper(componentModel = "spring")
public interface PromotionMapper {
    
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);

    /**
     * Convierte una entidad Promotion a PromotionResponseDTO
     * Aplana las relaciones para evitar lazy loading en GraphQL
     */
    @Mapping(target = "statusName", source = "status.statusName")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "categoryName", source = "category.categoryName")
    PromotionResponseDTO toResponseDTO(Promotion promotion);

    /**
     * Convierte una lista de entidades Promotion a lista de PromotionResponseDTO
     */
    java.util.List<PromotionResponseDTO> toResponseDTOList(java.util.List<Promotion> promotions);

    /**
     * Convierte PromotionInput a entidad Promotion (para crear)
     * Ignora campos que se setean por separado (relaciones)
     */
    @Mapping(target = "promotionId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "startDate", source = "startDateAsLocalDate")
    @Mapping(target = "endDate", source = "endDateAsLocalDate")
    Promotion toEntity(PromotionInput promotionInput);

    /**
     * Actualiza una entidad Promotion existente con datos de PromotionInput
     * Útil para operaciones de UPDATE
     */
    @Mapping(target = "promotionId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "startDate", source = "startDateAsLocalDate")
    @Mapping(target = "endDate", source = "endDateAsLocalDate")
    void updateEntityFromInput(PromotionInput promotionInput, @MappingTarget Promotion promotion);
}
