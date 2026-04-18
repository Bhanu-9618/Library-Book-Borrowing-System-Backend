package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.FineDto;
import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.model.enums.BorrowStatus;
import edu.icet.ecom.model.enums.PaymentStatus;
import edu.icet.ecom.repository.BookRepository;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineServiceImplTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private FineServiceImpl fineService;

    @Test
    void getFineByBorrowId_WhenExists_ShouldReturnDto() {
        FineEntity entity = new FineEntity();
        entity.setFineAmount(100.0);
        entity.setPaymentStatus(PaymentStatus.UNPAID);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Test User");
        entity.setUserEntity(user);

        when(fineRepository.findByBorrowEntity_Borrowid(1L)).thenReturn(Optional.of(entity));

        FineDto result = fineService.getFineByBorrowId(1L);

        assertNotNull(result);
        assertEquals(100.0, result.getFineAmount());
        assertEquals("Test User", result.getUserName());
    }

    @Test
    void updateFineStatus_WhenPaid_ShouldUpdateEverything() {
        // Arrange
        Long borrowId = 1L;
        BookEntity book = new BookEntity();
        book.setAvailableCopies(0);
        
        BorrowEntity borrow = new BorrowEntity();
        borrow.setBorrowid(borrowId);
        borrow.setBookEntity(book);
        borrow.setStatus(BorrowStatus.OVERDUE);

        FineEntity fine = new FineEntity();
        fine.setBorrowEntity(borrow);
        fine.setPaymentStatus(PaymentStatus.UNPAID);

        when(fineRepository.findByBorrowEntity_Borrowid(borrowId)).thenReturn(Optional.of(fine));

        // Act
        fineService.updateFineStatus(borrowId, "PAID");

        // Assert
        assertEquals(PaymentStatus.PAID, fine.getPaymentStatus());
        assertEquals(BorrowStatus.RETURNED, borrow.getStatus());
        assertEquals(LocalDate.now(), borrow.getReturnDate());
        assertEquals(1, book.getAvailableCopies()); // Should increment
        assertTrue(book.isAvailable());

        verify(fineRepository, times(1)).save(fine);
        verify(borrowRepository, times(1)).save(borrow);
        verify(bookRepository, times(1)).save(book);
    }
}
