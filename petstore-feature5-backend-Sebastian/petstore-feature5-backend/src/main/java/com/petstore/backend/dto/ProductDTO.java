package com.petstore.backend.dto;

public class ProductDTO {
    private Integer productId;
    private String productName;
    private Double basePrice;
    private Integer sku;
    private CategoryDTO category;
    private PromotionDTO promotion;

    // Constructors
    public ProductDTO() {}

    public ProductDTO(Integer productId, String productName, Double basePrice, 
                     Integer sku, CategoryDTO category, PromotionDTO promotion) {
        this.productId = productId;
        this.productName = productName;
        this.basePrice = basePrice;
        this.sku = sku;
        this.category = category;
        this.promotion = promotion;
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public PromotionDTO getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionDTO promotion) {
        this.promotion = promotion;
    }
}
