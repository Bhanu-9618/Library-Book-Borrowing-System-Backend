package edu.icet.ecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.ecom.model.dto.LoginDto;
import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.security.JwtUtils;
import edu.icet.ecom.security.UserDetailsImpl;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @Test
    void signup_ShouldReturnCreated() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("welcome@test.com");
        dto.setName("New User");
        dto.setPassword("password123");
        dto.setPhone("0771234567");
        dto.setAddress("Test Address");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void login_WhenSuccessful_ShouldReturnToken() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("admin@test.com");
        loginDto.setPassword("pass");

        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "Admin", "admin@test.com", "pass", edu.icet.ecom.model.enums.Role.ADMIN, true);
        
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtils.generateToken(anyString())).thenReturn("mocked_jwt_token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("mocked_jwt_token"))
                .andExpect(jsonPath("$.data.name").value("Admin"));
    }

    @Test
    void login_WhenInvalidCredentials_ShouldReturn401() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("wrong@test.com");
        loginDto.setPassword("wrong");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}
