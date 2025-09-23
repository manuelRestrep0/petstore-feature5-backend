package com.petstore.backend.dto;

import java.util.List;

public class PromotionListResponse {
    private boolean success;
    private String message;
    private List<PromotionDTO> promotions;
    private int totalCount;

    // Constructors
    public PromotionListResponse() {}

    public PromotionListResponse(boolean success, String message, List<PromotionDTO> promotions, int totalCount) {
        this.success = success;
        this.message = message;
        this.promotions = promotions;
        this.totalCount = totalCount;
    }

    // Static factory methods for common responses
    public static PromotionListResponse success(List<PromotionDTO> promotions) {
        return new PromotionListResponse(true, "Promociones obtenidas exitosamente", promotions, promotions.size());
    }

    public static PromotionListResponse error(String message) {
        return new PromotionListResponse(false, message, null, 0);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PromotionDTO> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionDTO> promotions) {
        this.promotions = promotions;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
