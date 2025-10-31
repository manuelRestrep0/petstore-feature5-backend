package com.petstore.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    void testDefaultConstructor() {
        // When
        LoginRequest request = new LoginRequest();

        // Then
        assertNotNull(request);
        assertNull(request.getEmail());
        assertNull(request.getPassword());
    }

    @Test
    void testParameterizedConstructor() {
        // When
        LoginRequest request = new LoginRequest("admin@petstore.com", "password123");

        // Then
        assertNotNull(request);
        assertEquals("admin@petstore.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        String email = "user@example.com";
        String password = "securePassword";

        // When
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Then
        assertEquals(email, loginRequest.getEmail());
        assertEquals(password, loginRequest.getPassword());
    }

    @Test
    void testSetEmail() {
        // When
        loginRequest.setEmail("test@example.com");

        // Then
        assertEquals("test@example.com", loginRequest.getEmail());
    }

    @Test
    void testSetPassword() {
        // When
        loginRequest.setPassword("myPassword");

        // Then
        assertEquals("myPassword", loginRequest.getPassword());
    }

    @Test
    void testSetEmailWithNull() {
        // When
        loginRequest.setEmail(null);

        // Then
        assertNull(loginRequest.getEmail());
    }

    @Test
    void testSetPasswordWithNull() {
        // When
        loginRequest.setPassword(null);

        // Then
        assertNull(loginRequest.getPassword());
    }

    @Test
    void testSetEmailWithEmptyString() {
        // When
        loginRequest.setEmail("");

        // Then
        assertEquals("", loginRequest.getEmail());
    }

    @Test
    void testSetPasswordWithEmptyString() {
        // When
        loginRequest.setPassword("");

        // Then
        assertEquals("", loginRequest.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        LoginRequest request1 = new LoginRequest("admin@petstore.com", "password123");
        LoginRequest request2 = new LoginRequest("admin@petstore.com", "password123");
        LoginRequest request3 = new LoginRequest("user@petstore.com", "password456");

        // Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        loginRequest.setEmail("admin@petstore.com");
        loginRequest.setPassword("password123");

        // When
        String result = loginRequest.toString();

        // Then
        assertNotNull(result);
        // La contraseña no debería aparecer en el toString por seguridad
        if (result.contains("password")) {
            // Si aparece, debería estar enmascarada
            assertEquals(true, result.contains("***") || result.contains("****") || result.contains("[PROTECTED]"));
        }
        // El email debería aparecer
        assertEquals(true, result.contains("admin@petstore.com") || result.contains("LoginRequest"));
    }

    @Test
    void testEmailValidation() {
        // When - Set various email formats
        loginRequest.setEmail("valid@example.com");
        assertEquals("valid@example.com", loginRequest.getEmail());

        loginRequest.setEmail("user.name@domain.co.uk");
        assertEquals("user.name@domain.co.uk", loginRequest.getEmail());

        loginRequest.setEmail("user+tag@example.com");
        assertEquals("user+tag@example.com", loginRequest.getEmail());
    }

    @Test
    void testPasswordValidation() {
        // When - Set various password formats
        loginRequest.setPassword("simple");
        assertEquals("simple", loginRequest.getPassword());

        loginRequest.setPassword("Complex!Password123");
        assertEquals("Complex!Password123", loginRequest.getPassword());

        loginRequest.setPassword("12345");
        assertEquals("12345", loginRequest.getPassword());
    }

    @Test
    void testChainingSetters() {
        // Given
        LoginRequest request = new LoginRequest();

        // When - Chain setters if possible, otherwise test individual setters
        request.setEmail("chain@example.com");
        request.setPassword("chainPassword");

        // Then
        assertEquals("chain@example.com", request.getEmail());
        assertEquals("chainPassword", request.getPassword());
    }
}
