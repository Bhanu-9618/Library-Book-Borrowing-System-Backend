package edu.icet.ecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_ShouldReturnCreatedStatus() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("new@user.com");

        mockMvc.perform(post("/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User Saved Successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllDetails_ShouldReturnOkStatus() throws Exception {
        Map<String, Object> mockResp = new HashMap<>();
        when(userService.getAllDetails(0, 10)).thenReturn(mockResp);

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchUserById_WhenExists_ShouldReturnUser() throws Exception {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("John Doe");

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/search/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void searchUserById_WhenNotExists_ShouldReturn404() throws Exception {
        when(userService.getUserById(99L)).thenReturn(null);

        mockMvc.perform(get("/user/search/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not Found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User Deleted Successfully"));
    }
}
