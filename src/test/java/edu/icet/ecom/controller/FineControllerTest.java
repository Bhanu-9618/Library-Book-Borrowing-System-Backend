package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.FineDto;
import edu.icet.ecom.service.FineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FineController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class FineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FineService fineService;

    @Test
    void getFineByBorrowId_WhenExists_ShouldReturnOk() throws Exception {
        FineDto dto = new FineDto();
        dto.setFineAmount(150.0);

        when(fineService.getFineByBorrowId(1L)).thenReturn(dto);

        mockMvc.perform(get("/fine/search/{borrowId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fineAmount").value(150.0));
    }

    @Test
    void getFineByBorrowId_WhenNotExists_ShouldReturn404() throws Exception {
        when(fineService.getFineByBorrowId(99L)).thenReturn(null);

        mockMvc.perform(get("/fine/search/{borrowId}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Fine Record Found for This Borrow ID"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePaymentStatus_ShouldReturnOk() throws Exception {
        mockMvc.perform(put("/fine/update-payment")
                .param("borrowId", "1")
                .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("successfully")));
    }
}
