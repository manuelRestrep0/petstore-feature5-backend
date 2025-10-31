package com.petstore.backend.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.petstore.backend.dto.UserResponseDTO;
import com.petstore.backend.entity.Role;
import com.petstore.backend.entity.User;

@SpringBootTest
@ActiveProfiles("test")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setRoleId(1);
        testRole.setRoleName("MARKETING_ADMIN");

        testUser = new User();
        testUser.setUserId(1);
        testUser.setEmail("admin@petstore.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(testRole);
    }

    @Test
    void toResponseDTO_ShouldConvertUserToUserResponseDTO() {
        // When
        UserResponseDTO result = userMapper.toResponseDTO(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getRole().getRoleName(), result.getRoleName());
        // UserResponseDTO correctly excludes password field for security
    }

    @Test
    void toResponseDTO_ShouldHandleNullUser() {
        // When
        UserResponseDTO result = userMapper.toResponseDTO(null);

        // Then
        assertNull(result);
    }

    @Test
    void toResponseDTO_ShouldHandleUserWithNullRole() {
        // Given
        testUser.setRole(null);

        // When
        UserResponseDTO result = userMapper.toResponseDTO(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertNull(result.getRoleName());
    }

    @Test
    void toResponseDTOList_ShouldConvertUserListToUserResponseDTOList() {
        // Given
        Role role2 = new Role();
        role2.setRoleId(2);
        role2.setRoleName("USER");

        User user2 = new User();
        user2.setUserId(2);
        user2.setEmail("user@petstore.com");
        user2.setPassword("anotherPassword");
        user2.setRole(role2);

        List<User> users = Arrays.asList(testUser, user2);

        // When
        List<UserResponseDTO> result = userMapper.toResponseDTOList(users);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("admin@petstore.com", result.get(0).getEmail());
        assertEquals("MARKETING_ADMIN", result.get(0).getRoleName());
        assertEquals("user@petstore.com", result.get(1).getEmail());
        assertEquals("USER", result.get(1).getRoleName());
    }

    @Test
    void toResponseDTOList_ShouldHandleNullList() {
        // When
        List<UserResponseDTO> result = userMapper.toResponseDTOList(null);

        // Then
        assertNull(result);
    }

    @Test
    void toResponseDTOList_ShouldHandleEmptyList() {
        // When
        List<UserResponseDTO> result = userMapper.toResponseDTOList(Arrays.asList());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void mapperInstance_ShouldNotBeNull() {
        // Then
        assertNotNull(UserMapper.INSTANCE);
    }
}
