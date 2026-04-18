package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.ecom.model.enums.BookCategory;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Used to convert Objects to JSON strings

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void save_ShouldReturnCreatedStatus() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book");
        bookDto.setAuthor("Author");

        // We don't need to specify 'when' for void methods in Mockito by default

        mockMvc.perform(post("/book/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Book saved successfully"));

        verify(bookService, times(1)).add(any(BookDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_ShouldReturnOkStatus() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Updated Title");

        mockMvc.perform(put("/book/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book updated successfully"));

        verify(bookService, times(1)).update(any(BookDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ShouldReturnOkStatus() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/book/delete/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Book deleted successfully"));

        verify(bookService, times(1)).delete(bookId);
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchById_ShouldReturnBookDetails() throws Exception {
        Long bookId = 1L;
        BookDto mockBook = new BookDto();
        mockBook.setId(bookId);
        mockBook.setTitle("Atomic Habits");

        when(bookService.searchById(bookId)).thenReturn(mockBook);

        mockMvc.perform(get("/book/id/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Atomic Habits"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getDetails_ShouldReturnPaginatedData() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("totalItems", 1);
        
        when(bookService.getPaginatedBooks(eq(0), eq(16), any())).thenReturn(mockResponse);

        mockMvc.perform(get("/book/all")
                .param("page", "0")
                .param("size", "16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalItems").value(1));
    }
}
