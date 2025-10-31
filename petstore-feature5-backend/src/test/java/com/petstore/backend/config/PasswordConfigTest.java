package com.petstore.backend.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordConfigTest {

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Given
        PasswordConfig passwordConfig = new PasswordConfig();

        // When
        PasswordEncoder result = passwordConfig.passwordEncoder();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof BCryptPasswordEncoder);
    }

    @Test
    void passwordEncoder_ShouldEncodePasswords() {
        // Given
        PasswordConfig passwordConfig = new PasswordConfig();
        PasswordEncoder encoder = passwordConfig.passwordEncoder();
        String plainPassword = "testPassword123";

        // When
        String encodedPassword = encoder.encode(plainPassword);

        // Then
        assertNotNull(encodedPassword);
        assertNotEquals(plainPassword, encodedPassword);
        assertTrue(encoder.matches(plainPassword, encodedPassword));
    }

    @Test
    void passwordEncoder_ShouldProduceDifferentHashesForSamePassword() {
        // Given
        PasswordConfig passwordConfig = new PasswordConfig();
        PasswordEncoder encoder = passwordConfig.passwordEncoder();
        String plainPassword = "testPassword123";

        // When
        String encodedPassword1 = encoder.encode(plainPassword);
        String encodedPassword2 = encoder.encode(plainPassword);

        // Then
        assertNotNull(encodedPassword1);
        assertNotNull(encodedPassword2);
        assertNotEquals(encodedPassword1, encodedPassword2);
        assertTrue(encoder.matches(plainPassword, encodedPassword1));
        assertTrue(encoder.matches(plainPassword, encodedPassword2));
    }
}
