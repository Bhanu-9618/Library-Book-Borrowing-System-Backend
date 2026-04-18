package edu.icet.ecom.scheduler;

import edu.icet.ecom.model.entity.BookEntity;
import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.model.enums.BorrowStatus;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import edu.icet.ecom.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineSchedulerTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FineScheduler fineScheduler;

    @Test
    void calculateLateFines_ShouldUpdateStatusAndCreateFine() {
        // Arrange
        BorrowEntity overdue = new BorrowEntity();
        overdue.setBorrowid(1L);
        overdue.setStatus(BorrowStatus.ISSUED);
        overdue.setDueDate(LocalDate.now().minusDays(2)); // 2 days late
        overdue.setUserEntity(new UserEntity());

        when(borrowRepository.findByStatusInAndDueDateBefore(any(), any()))
                .thenReturn(Collections.singletonList(overdue));
        when(fineRepository.findByBorrowEntity_Borrowid(1L))
                .thenReturn(Optional.empty());

        // Act
        fineScheduler.calculateLateFines();

        // Assert
        assertEquals(BorrowStatus.OVERDUE, overdue.getStatus());
        verify(borrowRepository, times(1)).save(overdue);
        verify(fineRepository, times(1)).save(any(FineEntity.class));
    }

    @Test
    void sendDueReminders_ShouldSendEmailForUpcomingReturns() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setName("Test User");

        BookEntity book = new BookEntity();
        book.setTitle("Test Book");

        BorrowEntity dueSoon = new BorrowEntity();
        dueSoon.setUserEntity(user);
        dueSoon.setBookEntity(book);
        dueSoon.setDueDate(LocalDate.now().plusDays(1));

        when(borrowRepository.findByStatusAndDueDate(eq(BorrowStatus.ISSUED), any()))
                .thenReturn(Collections.singletonList(dueSoon));

        // Act
        fineScheduler.sendDueReminders();

        // Assert
        verify(emailService, times(1)).sendSimpleMessage(
                eq("user@test.com"), 
                contains("Book Return Reminder"), 
                contains("tomorrow")
        );
    }
}
