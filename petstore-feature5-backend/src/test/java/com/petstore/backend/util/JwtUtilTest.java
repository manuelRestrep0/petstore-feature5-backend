package com.petstore.backend.util;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String testEmail;
    private String validToken;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        validToken = jwtUtil.generateToken(testEmail);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // Given
        String email = "user@test.com";

        // When
        String token = jwtUtil.generateToken(email);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void extractEmail_ShouldReturnCorrectEmail() {
        // When
        String extractedEmail = jwtUtil.extractEmail(validToken);

        // Then
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void getEmailFromToken_ShouldReturnCorrectEmail() {
        // When
        String extractedEmail = jwtUtil.getEmailFromToken(validToken);

        // Then
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // When
        boolean isValid = jwtUtil.validateToken(validToken);

        // Then
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Given
        String invalidToken = "invalid.token.here";

        // When
        boolean isValid = jwtUtil.validateToken(invalidToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithNullToken_ShouldReturnFalse() {
        // When
        boolean isValid = jwtUtil.validateToken(null);

        // Then
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithEmptyToken_ShouldReturnFalse() {
        // Given
        String emptyToken = "";

        // When
        boolean isValid = jwtUtil.validateToken(emptyToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    void extractEmail_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";

        // Then
        assertThrows(JwtException.class, () -> {
            jwtUtil.extractEmail(invalidToken);
        });
    }

    @Test
    void generateToken_WithNullEmail_ShouldNotThrowException() {
        // When & Then
        assertDoesNotThrow(() -> {
            String token = jwtUtil.generateToken(null);
            assertNotNull(token);
        });
    }

    @Test
    void generateToken_WithEmptyEmail_ShouldNotThrowException() {
        // When & Then
        assertDoesNotThrow(() -> {
            String token = jwtUtil.generateToken("");
            assertNotNull(token);
        });
    }

    @Test
    void tokenGeneration_ShouldProduceDifferentTokensForSameEmail() throws InterruptedException {
        // Given
        String email = "same@email.com";

        // When
        String token1 = jwtUtil.generateToken(email);
        Thread.sleep(1000); // Wait 1 second to ensure different timestamp
        String token2 = jwtUtil.generateToken(email);

        // Then
        assertNotEquals(token1, token2); // Tokens should be different due to timestamp
    }

    @Test
    void extractEmail_FromGeneratedToken_ShouldMatchOriginalEmail() {
        // Given
        String originalEmail = "original@test.com";

        // When
        String token = jwtUtil.generateToken(originalEmail);
        String extractedEmail = jwtUtil.extractEmail(token);

        // Then
        assertEquals(originalEmail, extractedEmail);
    }
}
