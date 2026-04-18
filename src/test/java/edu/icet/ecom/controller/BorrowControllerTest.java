package edu.icet.ecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.service.BorrowService;
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

@WebMvcTest(BorrowController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BorrowService borrowService;

    @Test
    @WithMockUser(roles = "USER")
    void save_ShouldReturnCreatedStatus() throws Exception {
        BorrowDto dto = new BorrowDto();
        dto.setBookid(1L);
        dto.setUserid(1L);

        when(borrowService.saveDetails(any(BorrowDto.class))).thenReturn("Book requested successfully");

        mockMvc.perform(post("/borrow/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Book requested successfully"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnOkStatus() throws Exception {
        BorrowDto dto = new BorrowDto();
        dto.setBorrowid(1L);

        when(borrowService.updateDetails(any(BorrowDto.class))).thenReturn("Update successful");

        mockMvc.perform(put("/borrow/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update successful"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllHistory_ShouldReturnOkStatus() throws Exception {
        Map<String, Object> mockResp = new HashMap<>();
        mockResp.put("totalItems", 5);

        when(borrowService.getAllHistory(0, 10)).thenReturn(mockResp);

        mockMvc.perform(get("/borrow/all")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalItems").value(5));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getHistoryByUserId_ShouldReturnOkStatus() throws Exception {
        Long userId = 1L;
        Map<String, Object> mockResp = new HashMap<>();
        
        when(borrowService.getHistoryByUserId(eq(userId), eq(0), eq(10))).thenReturn(mockResp);

        mockMvc.perform(get("/borrow/search/{userid}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    void getTotalBorrowCount_ShouldReturnOkStatus() throws Exception {
        when(borrowService.getTotalBorrowCount()).thenReturn(100L);

        mockMvc.perform(get("/borrow/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(100));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOverdueHistory_ShouldReturnOkStatus() throws Exception {
        Map<String, Object> mockResp = new HashMap<>();
        when(borrowService.getOverdueHistory(0, 10)).thenReturn(mockResp);

        mockMvc.perform(get("/borrow/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }
}
