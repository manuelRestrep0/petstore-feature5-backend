package com.petstore.backend.dto;

public class CreatePromotionInput {
    private String promotionName;
    private String description;         // opcional
    private String startDate;           // "yyyy-MM-dd"
    private String endDate;             // "yyyy-MM-dd"
    private Double discountValue;       // porcentaje
    private String statusName;          // opcional: ACTIVE | SCHEDULED | EXPIRED
    private Integer categoryId;         // requerido

    public String getPromotionName() { return promotionName; }
    public void setPromotionName(String promotionName) { this.promotionName = promotionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
}
