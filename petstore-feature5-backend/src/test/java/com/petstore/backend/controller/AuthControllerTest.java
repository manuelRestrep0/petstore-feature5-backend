package com.petstore.backend.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petstore.backend.dto.LoginResponse;
import com.petstore.backend.entity.Role;
import com.petstore.backend.entity.User;
import com.petstore.backend.repository.RoleRepository;
import com.petstore.backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        // Crear rol Marketing Admin si no existe
        java.util.Optional<Role> roleOptional = roleRepository.findByRoleName("Marketing Admin");
        Role marketingAdminRole;
        if (roleOptional.isPresent()) {
            marketingAdminRole = roleOptional.get();
        } else {
            marketingAdminRole = new Role("Marketing Admin");
            roleRepository.save(marketingAdminRole);
        }
        
        // Crear usuario admin@petstore.com si no existe
        if (!userRepository.findByEmail("admin@petstore.com").isPresent()) {
            User adminUser = new User();
            adminUser.setUserName("Admin User");
            adminUser.setEmail("admin@petstore.com");
            adminUser.setPassword("admin123"); // En producción debería estar hasheado
            adminUser.setRole(marketingAdminRole);
            userRepository.save(adminUser);
        }
    }

    @Test
    void testGetStatus() throws Exception {
        mockMvc.perform(get("/api/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service", is("Authentication Service")))
                .andExpect(jsonPath("$.status", is("active")))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.endpoints", hasSize(5)));
    }

    @Test
    void testLoginEndpoint_ValidCredentials() throws Exception {
        String loginRequest = """
            {
                "email": "admin@petstore.com",
                "password": "admin123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.email", is("admin@petstore.com")))
                .andExpect(jsonPath("$.role", is("Marketing Admin")));
    }

    @Test
    void testLoginEndpoint_InvalidCredentials() throws Exception {
        String loginRequest = """
            {
                "email": "admin@petstore.com",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void testLoginEndpoint_MissingFields() throws Exception {
        String loginRequest = """
            {
                "email": "admin@petstore.com"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void testVerifyEndpoint_WithValidToken() throws Exception {
        // First login to get a valid token
        String loginRequest = """
            {
                "email": "admin@petstore.com",
                "password": "admin123"
            }
            """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
        String token = response.getToken();

        // Use the valid token to verify
        mockMvc.perform(get("/api/auth/verify")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid", is(true)));
    }

    @Test
    void testVerifyEndpoint_WithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/verify"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testVerifyEndpoint_WithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/auth/verify")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.valid", is(false)));
    }

    @Test
    void testGetCurrentUser_WithValidToken() throws Exception {
        // First login to get a valid token
        String loginRequest = """
            {
                "email": "admin@petstore.com",
                "password": "admin123"
            }
            """;

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(loginResponse, LoginResponse.class);
        String token = response.getToken();

        // Use the valid token to get current user
        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("admin@petstore.com")))
                .andExpect(jsonPath("$.role", is("Marketing Admin")));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer some-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Logout exitoso")));
    }

    @Test
    void testLoginEndpoint_EmptyBody() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void testLoginEndpoint_InvalidJson() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStatus_CheckAllEndpoints() throws Exception {
        mockMvc.perform(get("/api/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endpoints[0]", is("POST /api/auth/login - Login de Marketing Admin")))
                .andExpect(jsonPath("$.endpoints[1]", is("GET /api/auth/verify - Verificar token")))
                .andExpect(jsonPath("$.endpoints[2]", is("GET /api/auth/me - Obtener perfil del usuario")))
                .andExpect(jsonPath("$.endpoints[3]", is("POST /api/auth/logout - Logout")))
                .andExpect(jsonPath("$.endpoints[4]", is("GET /api/auth/status - Estado del servicio")));
    }

    @Test
    void testOptionsRequest() throws Exception {
        mockMvc.perform(options("/api/auth/login")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type"))
                .andExpect(status().isOk());
    }
}
