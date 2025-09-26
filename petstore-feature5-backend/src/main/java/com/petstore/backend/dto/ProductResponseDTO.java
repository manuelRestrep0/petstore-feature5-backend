package com.petstore.backend.dto;

/**
 * DTO de respuesta para Product - Versión optimizada para GraphQL
 */
public class ProductResponseDTO {
    private Integer productId;
    private String productName;
    private Double basePrice;
    private Integer sku;
    private String categoryName;
    private String promotionName;
    private Double discountValue;

    // Constructors
    public ProductResponseDTO() {}

    public ProductResponseDTO(Integer productId, String productName, Double basePrice, 
                             Integer sku, String categoryName, String promotionName, 
                             Double discountValue) {
        this.productId = productId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.sku = sku;
        this.categoryName = categoryName;
        this.promotionName = promotionName;
        this.discountValue = discountValue;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getSku() {
        return sku;
    }

    public void setSku(Integer sku) {
        this.sku = sku;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    // Método para calcular precio con descuento
    public Double getFinalPrice() {
        if (discountValue != null && discountValue > 0) {
            return basePrice - (basePrice * discountValue / 100);
        }
        return basePrice;
    }

    @Override
    public String toString() {
        return "ProductResponseDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", basePrice=" + basePrice +
                ", sku=" + sku +
                ", categoryName='" + categoryName + '\'' +
                ", promotionName='" + promotionName + '\'' +
                ", discountValue=" + discountValue +
                '}';
    }
}
