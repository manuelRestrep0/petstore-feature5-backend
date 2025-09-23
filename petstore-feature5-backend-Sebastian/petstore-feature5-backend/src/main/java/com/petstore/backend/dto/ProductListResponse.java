package com.petstore.backend.dto;

import java.util.List;

public class ProductListResponse {
    private boolean success;
    private String message;
    private List<ProductDTO> products;

    // Constructors
    public ProductListResponse() {}

    public ProductListResponse(boolean success, String message, List<ProductDTO> products) {
        this.success = success;
        this.message = message;
        this.products = products;
    }

    // Static factory methods
    public static ProductListResponse success(List<ProductDTO> products) {
        return new ProductListResponse(true, "Productos obtenidos exitosamente", products);
    }

    public static ProductListResponse error(String message) {
        return new ProductListResponse(false, message, null);
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

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
