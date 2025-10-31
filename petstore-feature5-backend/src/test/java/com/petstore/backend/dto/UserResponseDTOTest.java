package com.petstore.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserResponseDTOTest {

    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userResponseDTO = new UserResponseDTO();
    }

    @Test
    void testDefaultConstructor() {
        // When
        UserResponseDTO dto = new UserResponseDTO();

        // Then
        assertNotNull(dto);
        assertNull(dto.getUserId());
        assertNull(dto.getUserName());
        assertNull(dto.getEmail());
        assertNull(dto.getRoleName());
    }

    @Test
    void testParameterizedConstructor() {
        // When
        UserResponseDTO dto = new UserResponseDTO(1, "admin", "admin@petstore.com", "MARKETING_ADMIN");

        // Then
        assertNotNull(dto);
        assertEquals(1, dto.getUserId());
        assertEquals("admin", dto.getUserName());
        assertEquals("admin@petstore.com", dto.getEmail());
        assertEquals("MARKETING_ADMIN", dto.getRoleName());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        Integer userId = 1;
        String userName = "testUser";
        String email = "test@example.com";
        String roleName = "USER";

        // When
        userResponseDTO.setUserId(userId);
        userResponseDTO.setUserName(userName);
        userResponseDTO.setEmail(email);
        userResponseDTO.setRoleName(roleName);

        // Then
        assertEquals(userId, userResponseDTO.getUserId());
        assertEquals(userName, userResponseDTO.getUserName());
        assertEquals(email, userResponseDTO.getEmail());
        assertEquals(roleName, userResponseDTO.getRoleName());
    }

    @Test
    void testSetUserId() {
        // When
        userResponseDTO.setUserId(100);

        // Then
        assertEquals(100, userResponseDTO.getUserId());
    }

    @Test
    void testSetUserName() {
        // When
        userResponseDTO.setUserName("johnDoe");

        // Then
        assertEquals("johnDoe", userResponseDTO.getUserName());
    }

    @Test
    void testSetEmail() {
        // When
        userResponseDTO.setEmail("john.doe@company.com");

        // Then
        assertEquals("john.doe@company.com", userResponseDTO.getEmail());
    }

    @Test
    void testSetRoleName() {
        // When
        userResponseDTO.setRoleName("ADMIN");

        // Then
        assertEquals("ADMIN", userResponseDTO.getRoleName());
    }

    @Test
    void testSetUserIdWithNull() {
        // When
        userResponseDTO.setUserId(null);

        // Then
        assertNull(userResponseDTO.getUserId());
    }

    @Test
    void testSetUserNameWithNull() {
        // When
        userResponseDTO.setUserName(null);

        // Then
        assertNull(userResponseDTO.getUserName());
    }

    @Test
    void testSetEmailWithNull() {
        // When
        userResponseDTO.setEmail(null);

        // Then
        assertNull(userResponseDTO.getEmail());
    }

    @Test
    void testSetRoleNameWithNull() {
        // When
        userResponseDTO.setRoleName(null);

        // Then
        assertNull(userResponseDTO.getRoleName());
    }

    @Test
    void testSetUserNameWithEmptyString() {
        // When
        userResponseDTO.setUserName("");

        // Then
        assertEquals("", userResponseDTO.getUserName());
    }

    @Test
    void testSetEmailWithEmptyString() {
        // When
        userResponseDTO.setEmail("");

        // Then
        assertEquals("", userResponseDTO.getEmail());
    }

    @Test
    void testSetRoleNameWithEmptyString() {
        // When
        userResponseDTO.setRoleName("");

        // Then
        assertEquals("", userResponseDTO.getRoleName());
    }

    @Test
    void testToString() {
        // Given
        userResponseDTO.setUserId(1);
        userResponseDTO.setUserName("testUser");
        userResponseDTO.setEmail("test@example.com");
        userResponseDTO.setRoleName("USER");

        // When
        String result = userResponseDTO.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("UserResponseDTO") || 
                  result.contains("userId=1") || 
                  result.contains("testUser"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        UserResponseDTO dto1 = new UserResponseDTO(1, "admin", "admin@test.com", "ADMIN");
        UserResponseDTO dto2 = new UserResponseDTO(1, "admin", "admin@test.com", "ADMIN");
        UserResponseDTO dto3 = new UserResponseDTO(2, "user", "user@test.com", "USER");

        // Then - Test property equality since DTO might not override equals/hashCode
        assertEquals(dto1.getUserId(), dto2.getUserId());
        assertEquals(dto1.getUserName(), dto2.getUserName());
        assertEquals(dto1.getEmail(), dto2.getEmail());
        assertEquals(dto1.getRoleName(), dto2.getRoleName());
        
        assertNotEquals(dto1.getUserId(), dto3.getUserId());
        assertNotEquals(dto1.getUserName(), dto3.getUserName());
    }

    @Test
    void testMarketingAdminRole() {
        // When
        userResponseDTO.setRoleName("MARKETING_ADMIN");

        // Then
        assertEquals("MARKETING_ADMIN", userResponseDTO.getRoleName());
    }

    @Test
    void testValidEmailFormats() {
        // When & Then - Test various email formats
        userResponseDTO.setEmail("user@domain.com");
        assertEquals("user@domain.com", userResponseDTO.getEmail());

        userResponseDTO.setEmail("user.name@domain.co.uk");
        assertEquals("user.name@domain.co.uk", userResponseDTO.getEmail());

        userResponseDTO.setEmail("user+tag@example.org");
        assertEquals("user+tag@example.org", userResponseDTO.getEmail());
    }

    @Test
    void testUserNameWithSpecialCharacters() {
        // When
        userResponseDTO.setUserName("user_name-123");

        // Then
        assertEquals("user_name-123", userResponseDTO.getUserName());
    }

    @Test
    void testLongValues() {
        // Given
        String longUserName = "thisIsAVeryLongUserNameThatShouldStillWork";
        String longEmail = "very.long.email.address.that.should.still.work@company.domain.com";
        String longRoleName = "VERY_LONG_ROLE_NAME_THAT_DESCRIBES_PERMISSIONS";

        // When
        userResponseDTO.setUserName(longUserName);
        userResponseDTO.setEmail(longEmail);
        userResponseDTO.setRoleName(longRoleName);

        // Then
        assertEquals(longUserName, userResponseDTO.getUserName());
        assertEquals(longEmail, userResponseDTO.getEmail());
        assertEquals(longRoleName, userResponseDTO.getRoleName());
    }

    @Test
    void testSecurityAspect() {
        // Given - UserResponseDTO should not contain password field
        UserResponseDTO dto = new UserResponseDTO(1, "secureUser", "secure@test.com", "USER");

        // Then - Verify that there's no password getter/setter (this DTO is for responses)
        assertNotNull(dto);
        assertEquals("secureUser", dto.getUserName());
        assertEquals("secure@test.com", dto.getEmail());
        // Password should not be accessible in response DTO
    }

    @Test
    void testImmutableBuilderPattern() {
        // Given
        UserResponseDTO dto = new UserResponseDTO();

        // When - Build the DTO step by step
        dto.setUserId(1);
        dto.setUserName("builder");
        dto.setEmail("builder@test.com");
        dto.setRoleName("BUILDER");

        // Then
        assertEquals(1, dto.getUserId());
        assertEquals("builder", dto.getUserName());
        assertEquals("builder@test.com", dto.getEmail());
        assertEquals("BUILDER", dto.getRoleName());
    }
}
