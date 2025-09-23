package com.petstore.backend.dto;

import com.petstore.backend.entity.User;

public class GraphQLLoginResponse {
    private String token;
    private User user;
    private Boolean success;

    public GraphQLLoginResponse() {}

    public GraphQLLoginResponse(String token, User user, Boolean success) {
        this.token = token;
        this.user = user;
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
