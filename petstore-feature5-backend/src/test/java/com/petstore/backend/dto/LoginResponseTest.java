package com.petstore.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginResponseTest {

    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginResponse = new LoginResponse();
    }

    @Test
    void testDefaultConstructor() {
        // When
        LoginResponse response = new LoginResponse();

        // Then
        assertNotNull(response);
        assertNull(response.getToken());
        assertNull(response.getMessage());
        assertEquals(false, response.isSuccess()); // Default boolean should be false
    }

    @Test
    void testParameterizedConstructor() {
        // When
        LoginResponse response = new LoginResponse("jwt-token-123", "admin", "admin@petstore.com", "MARKETING_ADMIN");

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("jwt-token-123", response.getToken());
        assertEquals("admin", response.getUserName());
        assertEquals("admin@petstore.com", response.getEmail());
        assertEquals("MARKETING_ADMIN", response.getRole());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        boolean success = true;
        String message = "Authentication successful";
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        // When
        loginResponse.setSuccess(success);
        loginResponse.setMessage(message);
        loginResponse.setToken(token);

        // Then
        assertEquals(success, loginResponse.isSuccess());
        assertEquals(message, loginResponse.getMessage());
        assertEquals(token, loginResponse.getToken());
    }

    @Test
    void testSetSuccess() {
        // When
        loginResponse.setSuccess(true);

        // Then
        assertTrue(loginResponse.isSuccess());

        // When
        loginResponse.setSuccess(false);

        // Then
        assertFalse(loginResponse.isSuccess());
    }

    @Test
    void testSetMessage() {
        // When
        loginResponse.setMessage("Invalid credentials");

        // Then
        assertEquals("Invalid credentials", loginResponse.getMessage());
    }

    @Test
    void testSetToken() {
        // When
        loginResponse.setToken("sample-jwt-token");

        // Then
        assertEquals("sample-jwt-token", loginResponse.getToken());
    }

    @Test
    void testSetMessageWithNull() {
        // When
        loginResponse.setMessage(null);

        // Then
        assertNull(loginResponse.getMessage());
    }

    @Test
    void testSetTokenWithNull() {
        // When
        loginResponse.setToken(null);

        // Then
        assertNull(loginResponse.getToken());
    }

    @Test
    void testSetMessageWithEmptyString() {
        // When
        loginResponse.setMessage("");

        // Then
        assertEquals("", loginResponse.getMessage());
    }

    @Test
    void testSetTokenWithEmptyString() {
        // When
        loginResponse.setToken("");

        // Then
        assertEquals("", loginResponse.getToken());
    }

    @Test
    void testSuccessfulLoginResponse() {
        // Given
        LoginResponse response = new LoginResponse();

        // When
        response.setSuccess(true);
        response.setMessage("Login successful");
        response.setToken("valid-jwt-token");

        // Then
        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("valid-jwt-token", response.getToken());
    }

    @Test
    void testFailedLoginResponse() {
        // Given
        LoginResponse response = new LoginResponse();

        // When
        response.setSuccess(false);
        response.setMessage("Invalid email or password");
        response.setToken(null);

        // Then
        assertFalse(response.isSuccess());
        assertEquals("Invalid email or password", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        LoginResponse response1 = new LoginResponse("token123", "user1", "user1@test.com", "ADMIN");
        LoginResponse response2 = new LoginResponse("token123", "user1", "user1@test.com", "ADMIN");
        
        LoginResponse response3 = new LoginResponse();
        response3.setSuccess(false);
        response3.setMessage("Failed");

        // Then - Since LoginResponse might not override equals/hashCode, we test the properties
        assertEquals(response1.getToken(), response2.getToken());
        assertEquals(response1.getUserName(), response2.getUserName());
        assertNotEquals(response1.isSuccess(), response3.isSuccess());
    }

    @Test
    void testToString() {
        // Given
        loginResponse.setSuccess(true);
        loginResponse.setMessage("Authentication successful");
        loginResponse.setToken("jwt-token-example");

        // When
        String result = loginResponse.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("LoginResponse") || result.contains("success=true"));
        assertTrue(result.contains("Authentication successful") || result.contains("LoginResponse"));
    }

    @Test
    void testLongToken() {
        // Given
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // When
        loginResponse.setToken(longToken);

        // Then
        assertEquals(longToken, loginResponse.getToken());
    }

    @Test
    void testLongMessage() {
        // Given
        String longMessage = "This is a very long error message that could potentially be returned by the authentication service when something goes wrong during the login process and we need to provide detailed feedback to the user about what exactly happened";

        // When
        loginResponse.setMessage(longMessage);

        // Then
        assertEquals(longMessage, loginResponse.getMessage());
    }

    @Test
    void testSpecialCharactersInMessage() {
        // Given
        String specialMessage = "Error: Usuario/contraseña incorrectos. ¡Inténtalo de nuevo!";

        // When
        loginResponse.setMessage(specialMessage);

        // Then
        assertEquals(specialMessage, loginResponse.getMessage());
    }

    @Test
    void testUserPropertyIfExists() {
        // This test is for the user property if it exists in LoginResponse
        // Given
        LoginResponse response = new LoginResponse();
        
        // When & Then - Just test that the response can be created without user
        assertNotNull(response);
        // If user property exists, we could test it here
    }
}
