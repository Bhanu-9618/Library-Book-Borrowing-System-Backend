package edu.icet.ecom.service.Impl;

import edu.icet.ecom.exception.ResourceNotFoundException;
import edu.icet.ecom.model.dto.BorrowDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.model.enums.BorrowStatus;
import edu.icet.ecom.model.enums.PaymentStatus;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import edu.icet.ecom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Test
    void saveDetails_WhenValid_ShouldRequestBookSuccessfully() {
        // Arrange
        BorrowDto dto = new BorrowDto();
        dto.setBookid(1L);
        dto.setUserid(1L);

        BookEntity book = new BookEntity();
        book.setId(1L);
        book.setAvailableCopies(5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        String result = borrowService.saveDetails(dto);

        // Assert
        assertEquals("Book requested successfully. Waiting for admin approval.", result);
        assertEquals(4, book.getAvailableCopies()); // Inventory should decrease
        verify(borrowRepository, times(1)).save(any(BorrowEntity.class));
    }

    @Test
    void saveDetails_WhenBookUnavailable_ShouldReturnErrorMessage() {
        // Arrange
        BorrowDto dto = new BorrowDto();
        dto.setBookid(1L);
        
        BookEntity book = new BookEntity();
        book.setAvailableCopies(0);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        String result = borrowService.saveDetails(dto);

        // Assert
        assertEquals("Book is currently unavailable!", result);
        verify(borrowRepository, never()).save(any());
    }

    @Test
    void updateDetails_IssueBook_ShouldSetDatesAndStatus() {
        // Arrange
        BorrowDto dto = new BorrowDto();
        dto.setBorrowid(1L);
        dto.setStatus(BorrowStatus.ISSUED);

        BorrowEntity existing = new BorrowEntity();
        existing.setStatus(BorrowStatus.REQUESTED);
        existing.setBookEntity(new BookEntity());

        when(borrowRepository.findById(1L)).thenReturn(Optional.of(existing));

        // Act
        String result = borrowService.updateDetails(dto);

        // Assert
        assertTrue(result.contains("Book issued successfully"));
        assertEquals(BorrowStatus.ISSUED, existing.getStatus());
        assertNotNull(existing.getBorrowdate()); 
        assertEquals(LocalDate.now().plusDays(14), existing.getDueDate());
    }

    @Test
    void updateDetails_ReturnBookOnTime_ShouldUpdateInventoryAndStatus() {
        // Arrange
        BorrowDto dto = new BorrowDto();
        dto.setBorrowid(1L);
        dto.setStatus(BorrowStatus.RETURNED);

        BookEntity book = new BookEntity();
        book.setAvailableCopies(2);

        BorrowEntity existing = new BorrowEntity();
        existing.setBorrowid(1L);
        existing.setStatus(BorrowStatus.ISSUED);
        existing.setBookEntity(book);
        existing.setDueDate(LocalDate.now().plusDays(5)); // Due in future

        when(borrowRepository.findById(1L)).thenReturn(Optional.of(existing));

        // Act
        String result = borrowService.updateDetails(dto);

        // Assert
        assertTrue(result.contains("Book returned successfully. Inventory updated."));
        assertEquals(3, book.getAvailableCopies()); // Inventory should increase
        assertEquals(BorrowStatus.RETURNED, existing.getStatus());
        assertEquals(LocalDate.now(), existing.getReturnDate());
    }

    @Test
    void updateDetails_ReturnBookLate_ShouldApplyFine() {
        // Arrange
        BorrowDto dto = new BorrowDto();
        dto.setBorrowid(1L);
        dto.setStatus(BorrowStatus.RETURNED);

        BorrowEntity existing = new BorrowEntity();
        existing.setBorrowid(1L);
        existing.setStatus(BorrowStatus.ISSUED);
        existing.setBookEntity(new BookEntity());
        existing.setUserEntity(new UserEntity());
        existing.setDueDate(LocalDate.now().minusDays(5)); // 5 days late

        when(borrowRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(fineRepository.findByBorrowEntity_Borrowid(1L)).thenReturn(Optional.empty());

        // Act
        String result = borrowService.updateDetails(dto);

        // Assert
        assertTrue(result.contains("Late fine of Rs 250.0 applied")); // 5 days * 50 = 250
        verify(fineRepository, times(1)).save(any(FineEntity.class));
    }

    @Test
    void getAllHistory_ShouldReturnPaginatedResponse() {
        // Arrange
        Page<BorrowEntity> mockPage = new org.springframework.data.domain.PageImpl<>(new java.util.ArrayList<>());
        when(borrowRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(mockPage);

        // Act
        java.util.Map<String, Object> result = borrowService.getAllHistory(0, 10);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("history"));
        verify(borrowRepository, times(1)).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void getOverdueHistory_ShouldMapFinesCorrectly() {
        // Arrange
        BorrowEntity overdueBorrow = new BorrowEntity();
        overdueBorrow.setBorrowid(1L);
        overdueBorrow.setUserEntity(new UserEntity());
        
        FineEntity fine = new FineEntity();
        fine.setFineAmount(500.0);
        fine.setBorrowEntity(overdueBorrow);
        fine.setPaymentStatus(PaymentStatus.UNPAID);

        Page<BorrowEntity> mockPage = new org.springframework.data.domain.PageImpl<>(java.util.List.of(overdueBorrow));
        
        when(borrowRepository.findByStatus(eq(BorrowStatus.OVERDUE), any())).thenReturn(mockPage);
        when(fineRepository.findByBorrowEntity_BorrowidIn(any())).thenReturn(java.util.List.of(fine));

        // Act
        java.util.Map<String, Object> result = borrowService.getOverdueHistory(0, 10);

        // Assert
        java.util.List<?> list = (java.util.List<?>) result.get("history");
        assertEquals(1, list.size());
        verify(fineRepository, times(1)).findByBorrowEntity_BorrowidIn(any());
    }

    @Test
    void getCounts_ShouldReturnCorrectNumbers() {
        when(borrowRepository.count()).thenReturn(100L);
        when(borrowRepository.countByStatus(BorrowStatus.REQUESTED)).thenReturn(10L);
        when(borrowRepository.countByStatus(BorrowStatus.OVERDUE)).thenReturn(5L);
        when(borrowRepository.countByStatus(BorrowStatus.ISSUED)).thenReturn(20L);

        assertEquals(100L, borrowService.getTotalBorrowCount());
        assertEquals(10L, borrowService.getRequestedCount());
        assertEquals(5L, borrowService.getOverdueCount());
        assertEquals(20L, borrowService.getIssuedCount());
    }
}
