package edu.icet.ecom.service.Impl;

import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.model.dto.BookDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.enums.BookCategory;
import edu.icet.ecom.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void add_ShouldSaveBook() {
        BookDto bookDto = new BookDto();
        BookEntity bookEntity = new BookEntity();

        when(mapper.map(bookDto, BookEntity.class)).thenReturn(bookEntity);

        bookService.add(bookDto);

        verify(bookRepository, times(1)).save(bookEntity);
    }

    @Test
    void getAllDetails_ShouldReturnListOfBookDto() throws Exception {
        BookEntity entity = new BookEntity();
        BookDto dto = new BookDto();
        
        when(bookRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.map(entity, BookDto.class)).thenReturn(dto);

        List<BookDto> result = bookService.getAllDetails();

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void update_WhenBookExists_ShouldUpdateBook() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        BookEntity bookEntity = new BookEntity();

        when(bookRepository.existsById(1L)).thenReturn(true);
        when(mapper.map(bookDto, BookEntity.class)).thenReturn(bookEntity);

        bookService.update(bookDto);

        verify(bookRepository, times(1)).save(bookEntity);
    }

    @Test
    void update_WhenBookNotExists_ShouldThrowException() {
        BookDto bookDto = new BookDto();
        bookDto.setId(99L);
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(bookDto));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void delete_WhenBookExists_ShouldDeleteBook() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.delete(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void delete_WhenBookNotExists_ShouldThrowException() {
        Long bookId = 99L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(bookId));
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void searchById_WhenBookExists_ShouldReturnBookDto() {
        Long bookId = 1L;
        BookEntity mockEntity = new BookEntity();
        mockEntity.setId(bookId);
        mockEntity.setTitle("The Great Gatsby");

        BookDto mockDto = new BookDto();
        mockDto.setId(bookId);
        mockDto.setTitle("The Great Gatsby");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockEntity));
        when(mapper.map(any(BookEntity.class), eq(BookDto.class))).thenReturn(mockDto);

        BookDto result = bookService.searchById(bookId);

        assertNotNull(result);
        assertEquals("The Great Gatsby", result.getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void searchById_WhenBookDoesNotExist_ShouldThrowException() {
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.searchById(bookId);
        });

        assertEquals("Book Not Found!", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void getPaginatedBooks_WithCategory_ShouldReturnCategorizedBooks() {
        int page = 0;
        int size = 10;
        BookCategory category = BookCategory.FICTION; // Assuming FICTION exists in your enum
        
        BookEntity entity = new BookEntity();
        Page<BookEntity> mockPage = new PageImpl<>(List.of(entity), PageRequest.of(page, size), 1);
        
        when(bookRepository.findByCategory(eq(category), any(Pageable.class))).thenReturn(mockPage);
        when(mapper.map(entity, BookDto.class)).thenReturn(new BookDto());
        
        Map<String, Object> response = bookService.getPaginatedBooks(page, size, category);
        
        assertNotNull(response);
        assertEquals(1L, response.get("totalItems"));
        assertEquals(0, response.get("currentPage"));
        verify(bookRepository, times(1)).findByCategory(eq(category), any(Pageable.class));
    }
    
    @Test
    void getTotalBooksCount_ShouldReturnCount() {
        when(bookRepository.count()).thenReturn(50L);
        long result = bookService.getTotalBooksCount();
        assertEquals(50L, result);
        verify(bookRepository, times(1)).count();
    }
}
