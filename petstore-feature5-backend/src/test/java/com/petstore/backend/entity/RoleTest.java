package com.petstore.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void constructor_ShouldCreateEmptyRole() {
        // Then
        assertNotNull(role);
        assertNull(role.getRoleId());
        assertNull(role.getRoleName());
    }

    @Test
    void constructorWithRoleName_ShouldCreateRoleWithName() {
        // Given
        String roleName = "MARKETING_ADMIN";

        // When
        Role roleWithName = new Role(roleName);

        // Then
        assertNotNull(roleWithName);
        assertNull(roleWithName.getRoleId()); // ID is generated
        assertEquals(roleName, roleWithName.getRoleName());
    }

    @Test
    void setRoleId_ShouldSetRoleId() {
        // Given
        Integer roleId = 1;

        // When
        role.setRoleId(roleId);

        // Then
        assertEquals(roleId, role.getRoleId());
    }

    @Test
    void setRoleName_ShouldSetRoleName() {
        // Given
        String roleName = "ADMIN";

        // When
        role.setRoleName(roleName);

        // Then
        assertEquals(roleName, role.getRoleName());
    }

    @Test
    void setRoleName_WithNullValue_ShouldSetNull() {
        // Given
        role.setRoleName("ADMIN");

        // When
        role.setRoleName(null);

        // Then
        assertNull(role.getRoleName());
    }

    @Test
    void setRoleName_WithEmptyValue_ShouldSetEmptyString() {
        // Given
        String emptyRoleName = "";

        // When
        role.setRoleName(emptyRoleName);

        // Then
        assertEquals(emptyRoleName, role.getRoleName());
    }

    @Test
    void getRoleId_AfterSetting_ShouldReturnSameValue() {
        // Given
        Integer expectedId = 42;

        // When
        role.setRoleId(expectedId);

        // Then
        assertEquals(expectedId, role.getRoleId());
    }

    @Test
    void getRoleName_AfterSetting_ShouldReturnSameValue() {
        // Given
        String expectedName = "USER_ADMIN";

        // When
        role.setRoleName(expectedName);

        // Then
        assertEquals(expectedName, role.getRoleName());
    }

    @Test
    void roleWithCompleteData_ShouldMaintainConsistency() {
        // Given
        Integer roleId = 5;
        String roleName = "MARKETING_ADMIN";

        // When
        role.setRoleId(roleId);
        role.setRoleName(roleName);

        // Then
        assertEquals(roleId, role.getRoleId());
        assertEquals(roleName, role.getRoleName());
    }

    @Test
    void multipleRoleNameChanges_ShouldRetainLatestValue() {
        // Given
        String firstName = "ADMIN";
        String secondName = "USER";
        String finalName = "MARKETING_ADMIN";

        // When
        role.setRoleName(firstName);
        role.setRoleName(secondName);
        role.setRoleName(finalName);

        // Then
        assertEquals(finalName, role.getRoleName());
    }
}
