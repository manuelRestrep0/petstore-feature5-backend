package com.petstore.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void constructor_ShouldCreateEmptyUser() {
        // Then
        assertNotNull(user);
        assertNull(user.getUserId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    void setUserId_ShouldSetUserId() {
        // Given
        Integer userId = 1;

        // When
        user.setUserId(userId);

        // Then
        assertEquals(userId, user.getUserId());
    }

    @Test
    void setEmail_ShouldSetEmail() {
        // Given
        String email = "test@example.com";

        // When
        user.setEmail(email);

        // Then
        assertEquals(email, user.getEmail());
    }

    @Test
    void setPassword_ShouldSetPassword() {
        // Given
        String password = "securePassword123";

        // When
        user.setPassword(password);

        // Then
        assertEquals(password, user.getPassword());
    }

    @Test
    void allFieldsCanBeSetAndRetrieved() {
        // Given
        Integer userId = 1;
        String email = "admin@petstore.com";
        String password = "hashedPassword";

        // When
        user.setUserId(userId);
        user.setEmail(email);
        user.setPassword(password);

        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void fieldsCanBeNullSafely() {
        // Given
        user.setUserId(null);
        user.setEmail(null);
        user.setPassword(null);

        // When & Then
        assertNull(user.getUserId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());

        // Should not throw any exceptions
        assertDoesNotThrow(() -> {
            user.toString();
            user.hashCode();
        });
    }

    @Test
    void email_ShouldAcceptValidEmailFormats() {
        // Given
        String[] validEmails = {
            "test@example.com",
            "user.name@domain.co.uk",
            "admin+tag@company.org",
            "123@numbers.com",
            "a@b.co"
        };

        // When & Then
        for (String email : validEmails) {
            user.setEmail(email);
            assertEquals(email, user.getEmail());
        }
    }

    @Test
    void email_ShouldAcceptEmptyString() {
        // Given
        String emptyEmail = "";

        // When
        user.setEmail(emptyEmail);

        // Then
        assertEquals(emptyEmail, user.getEmail());
    }

    @Test
    void password_ShouldAcceptEmptyString() {
        // Given
        String emptyPassword = "";

        // When
        user.setPassword(emptyPassword);

        // Then
        assertEquals(emptyPassword, user.getPassword());
    }

    @Test
    void password_ShouldAcceptLongPassword() {
        // Given
        String longPassword = "a".repeat(200); // Very long password

        // When
        user.setPassword(longPassword);

        // Then
        assertEquals(longPassword, user.getPassword());
    }

    @Test
    void password_ShouldAcceptSpecialCharacters() {
        // Given
        String specialPassword = "P@$$w0rd!#$%^&*()_+{}|:<>?[]\\;'\",./-=";

        // When
        user.setPassword(specialPassword);

        // Then
        assertEquals(specialPassword, user.getPassword());
    }

    @Test
    void userId_ShouldAcceptZero() {
        // Given
        Integer zeroId = 0;

        // When
        user.setUserId(zeroId);

        // Then
        assertEquals(zeroId, user.getUserId());
    }

    @Test
    void userId_ShouldAcceptNegativeValue() {
        // Given
        Integer negativeId = -1;

        // When
        user.setUserId(negativeId);

        // Then
        assertEquals(negativeId, user.getUserId());
    }

    @Test
    void userId_ShouldAcceptLargeValue() {
        // Given
        Integer largeId = Integer.MAX_VALUE;

        // When
        user.setUserId(largeId);

        // Then
        assertEquals(largeId, user.getUserId());
    }

    @Test
    void email_ShouldAcceptUnicodeCharacters() {
        // Given
        String unicodeEmail = "测试@例子.中国";

        // When
        user.setEmail(unicodeEmail);

        // Then
        assertEquals(unicodeEmail, user.getEmail());
    }

    @Test
    void password_ShouldAcceptUnicodeCharacters() {
        // Given
        String unicodePassword = "密码123パスワード🔒";

        // When
        user.setPassword(unicodePassword);

        // Then
        assertEquals(unicodePassword, user.getPassword());
    }
}
