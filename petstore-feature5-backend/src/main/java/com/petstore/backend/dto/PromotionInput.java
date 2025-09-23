package com.petstore.backend.dto;

import java.time.LocalDate;

public class PromotionInput {
    private String promotionName;
    private String description;
    private String startDate;  // String para compatibilidad con GraphQL
    private String endDate;    // String para compatibilidad con GraphQL
    private Double discountValue;
    private Integer statusId;
    private Integer userId;
    private Integer categoryId;

    // Constructors
    public PromotionInput() {}

    public PromotionInput(String promotionName, String description, String startDate, 
                         String endDate, Double discountValue, Integer statusId, 
                         Integer userId, Integer categoryId) {
        this.promotionName = promotionName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
        this.statusId = statusId;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    // Utility methods para convertir fechas String a LocalDate
    public LocalDate getStartDateAsLocalDate() {
        return startDate != null ? LocalDate.parse(startDate) : null;
    }

    public LocalDate getEndDateAsLocalDate() {
        return endDate != null ? LocalDate.parse(endDate) : null;
    }

    @Override
    public String toString() {
        return "PromotionInput{" +
                "promotionName='" + promotionName + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", discountValue=" + discountValue +
                ", statusId=" + statusId +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                '}';
    }
}
