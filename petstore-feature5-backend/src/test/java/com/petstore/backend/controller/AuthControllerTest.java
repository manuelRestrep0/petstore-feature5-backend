package com.petstore.backend.controller;

import com.petstore.backend.dto.LoginRequest;
import com.petstore.backend.dto.LoginResponse;
import com.petstore.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void login_validCredentials_returnsOkResponse() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@marketing.com");
        request.setPassword("password123");

        LoginResponse mockResponse = new LoginResponse(
                "mockToken",
                "Admin User",
                "admin@marketing.com",
                "MARKETING_ADMIN"
        );

        when(authService.authenticateMarketingAdmin(
                request.getEmail(),
                request.getPassword()
        )).thenReturn(mockResponse);

        // Act
        ResponseEntity<LoginResponse> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("mockToken", response.getBody().getToken());
        assertEquals("admin@marketing.com", response.getBody().getEmail());
        assertEquals("MARKETING_ADMIN", response.getBody().getRole());
        verify(authService, times(1))
                .authenticateMarketingAdmin(request.getEmail(), request.getPassword());
    }

    @Test
    void login_invalidCredentials_returnsUnauthorizedResponse() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("user@company.com");
        request.setPassword("wrongpass");

        when(authService.authenticateMarketingAdmin(
                request.getEmail(),
                request.getPassword()
        )).thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<LoginResponse> response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Email o contraseña incorrectos"));
        verify(authService, times(1))
                .authenticateMarketingAdmin(request.getEmail(), request.getPassword());
    }

    @Test
    void verifyToken_validToken_returnsOkResponse() {
        // Arrange
        String token = "validToken";
        String authHeader = "Bearer " + token;
        when(authService.validateToken(token)).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.verifyToken(authHeader);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("\"valid\": true"));
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    void verifyToken_invalidToken_returnsUnauthorized() {
        // Arrange
        String token = "invalidToken";
        String authHeader = "Bearer " + token;
        when(authService.validateToken(token)).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.verifyToken(authHeader);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("\"valid\": false"));
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    void verifyToken_exceptionThrown_returnsUnauthorized() {
        // Arrange
        String authHeader = "Bearer brokenToken";
        when(authService.validateToken(anyString())).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<?> response = authController.verifyToken(authHeader);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("malformado"));
        verify(authService, times(1)).validateToken(anyString());
    }

    @Test
    void getCurrentUser_validToken_returnsUserInfo() {
        // Arrange
        String token = "validToken";
        String authHeader = "Bearer " + token;
        Map<String, Object> mockUser = Map.of("username", "juan", "role", "USER");
        when(authService.getUserFromToken(token)).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = authController.getCurrentUser(authHeader);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
        verify(authService, times(1)).getUserFromToken(token);
    }

    @Test
    void getCurrentUser_invalidToken_returnsUnauthorized() {
        // Arrange
        String token = "invalidToken";
        String authHeader = "Bearer " + token;
        when(authService.getUserFromToken(token)).thenThrow(new RuntimeException("Invalid"));

        // Act
        ResponseEntity<?> response = authController.getCurrentUser(authHeader);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Token inválido"));
        verify(authService, times(1)).getUserFromToken(token);
    }

    @Test
    void logout_returnsOkResponse() {
        // Arrange (no dependencies que mockear)

        // Act
        ResponseEntity<?> response = authController.logout();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Logout exitoso"));
    }
}
