package com.petstore.backend.service;

import com.petstore.backend.dto.LoginResponse;
import com.petstore.backend.entity.Role;
import com.petstore.backend.entity.User;
import com.petstore.backend.repository.UserRepository;
import com.petstore.backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        mockRole = new Role();
        mockRole.setRoleId(1);
        mockRole.setRoleName("MARKETING_ADMIN");

        mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUserName("Test User");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole(mockRole);
    }

    @Test
    void authenticateMarketingAdmin_WithValidCredentials_ShouldReturnLoginResponse() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        String expectedToken = "jwt-token-123";

        when(userRepository.findMarketingAdminByEmail(email))
                .thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(email)).thenReturn(expectedToken);

        // When
        LoginResponse response = authService.authenticateMarketingAdmin(email, password);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(expectedToken, response.getToken());
        assertEquals(mockUser.getUserName(), response.getUserName());
        assertEquals(mockUser.getEmail(), response.getEmail());
        assertEquals(mockRole.getRoleName(), response.getRole());
        assertEquals("Login exitoso", response.getMessage());

        verify(userRepository).findMarketingAdminByEmail(email);
        verify(jwtUtil).generateToken(email);
    }

    @Test
    void authenticateMarketingAdmin_WithInvalidUser_ShouldThrowException() {
        // Given
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.findMarketingAdminByEmail(email))
                .thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateMarketingAdmin(email, password);
        });

        assertEquals("Usuario no encontrado o no es Marketing Admin", exception.getMessage());
        verify(userRepository).findMarketingAdminByEmail(email);
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void authenticateMarketingAdmin_WithIncorrectPassword_ShouldThrowException() {
        // Given
        String email = "test@example.com";
        String wrongPassword = "wrongpassword";

        when(userRepository.findMarketingAdminByEmail(email))
                .thenReturn(Optional.of(mockUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateMarketingAdmin(email, wrongPassword);
        });

        assertEquals("Contraseña incorrecta", exception.getMessage());
        verify(userRepository).findMarketingAdminByEmail(email);
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        String validToken = "valid-jwt-token";
        when(jwtUtil.validateToken(validToken)).thenReturn(true);

        // When
        boolean result = authService.validateToken(validToken);

        // Then
        assertTrue(result);
        verify(jwtUtil).validateToken(validToken);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Given
        String invalidToken = "invalid-jwt-token";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        // When
        boolean result = authService.validateToken(invalidToken);

        // Then
        assertFalse(result);
        verify(jwtUtil).validateToken(invalidToken);
    }

    @Test
    void validateToken_WithExceptionThrown_ShouldReturnFalse() {
        // Given
        String problematicToken = "problematic-token";
        when(jwtUtil.validateToken(problematicToken))
                .thenThrow(new RuntimeException("JWT parsing error"));

        // When
        boolean result = authService.validateToken(problematicToken);

        // Then
        assertFalse(result);
        verify(jwtUtil).validateToken(problematicToken);
    }

    @Test
    void getUserFromToken_WithValidToken_ShouldReturnUserInfo() {
        // Given
        String validToken = "valid-jwt-token";
        String email = "test@example.com";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(validToken)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // When
        Map<String, Object> userInfo = authService.getUserFromToken(validToken);

        // Then
        assertNotNull(userInfo);
        assertEquals(mockUser.getUserId(), userInfo.get("userId"));
        assertEquals(mockUser.getUserName(), userInfo.get("userName"));
        assertEquals(mockUser.getEmail(), userInfo.get("email"));
        assertEquals(mockRole.getRoleName(), userInfo.get("role"));
        assertEquals(mockRole.getRoleId(), userInfo.get("roleId"));

        verify(jwtUtil).validateToken(validToken);
        verify(jwtUtil).getEmailFromToken(validToken);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserFromToken_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid-jwt-token";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.getUserFromToken(invalidToken);
        });

        assertEquals("Token inválido", exception.getMessage());
        verify(jwtUtil).validateToken(invalidToken);
        verify(jwtUtil, never()).getEmailFromToken(anyString());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void getUserFromToken_WithValidTokenButUserNotFound_ShouldThrowException() {
        // Given
        String validToken = "valid-jwt-token";
        String email = "nonexistent@example.com";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(validToken)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.getUserFromToken(validToken);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(jwtUtil).validateToken(validToken);
        verify(jwtUtil).getEmailFromToken(validToken);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void loadUserByUsername_WithExistingUser_ShouldReturnUserDetails() {
        // Given
        String username = "test@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(mockUser));

        // When
        UserDetails userDetails = authService.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + mockRole.getRoleName())));

        verify(userRepository).findByEmail(username);
    }

    @Test
    void loadUserByUsername_WithNonexistentUser_ShouldThrowUsernameNotFoundException() {
        // Given
        String username = "nonexistent@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername(username);
        });

        verify(userRepository).findByEmail(username);
    }
}
