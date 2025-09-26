package com.petstore.backend.dto;

import java.time.LocalDate;

/**
 * DTO de respuesta para Promotion - Versión optimizada para GraphQL
 */
public class PromotionResponseDTO {
    private Integer promotionId;
    private String promotionName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double discountValue;
    private String statusName;
    private String userName;
    private String categoryName;

    // Constructors
    public PromotionResponseDTO() {}

    public PromotionResponseDTO(Integer promotionId, String promotionName, String description, 
                               LocalDate startDate, LocalDate endDate, Double discountValue, 
                               String statusName, String userName, String categoryName) {
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
        this.statusName = statusName;
        this.userName = userName;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Integer getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "PromotionResponseDTO{" +
                "promotionId=" + promotionId +
                ", promotionName='" + promotionName + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", discountValue=" + discountValue +
                ", statusName='" + statusName + '\'' +
                ", userName='" + userName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
