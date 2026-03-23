package edu.icet.ecom.scheduler;

import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import edu.icet.ecom.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
public class FineScheduler {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void calculateLateFines() {
        LocalDate today = LocalDate.now();
        List<BorrowEntity> overdueBorrows = borrowRepository.findByStatusAndDueDateBefore("ISSUED", today);

        for (BorrowEntity borrow : overdueBorrows) {
            long daysLate = ChronoUnit.DAYS.between(borrow.getDueDate(), today);
            double calculatedFine = daysLate * 50.0;

            Optional<FineEntity> existingFine = fineRepository.findByBorrowEntity_Borrowid(borrow.getBorrowid());

            if (existingFine.isPresent()) {
                FineEntity fine = existingFine.get();
                fine.setFineAmount(calculatedFine);
                fineRepository.save(fine);
            } else {
                FineEntity newFine = new FineEntity();
                newFine.setBorrowEntity(borrow);
                newFine.setUserEntity(borrow.getUserEntity());
                newFine.setFineAmount(calculatedFine);
                newFine.setPaymentStatus("UNPAID");
                fineRepository.save(newFine);
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void sendDueReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<BorrowEntity> upcomingReturns = borrowRepository.findByStatusAndDueDate("ISSUED", tomorrow);

        for (BorrowEntity borrow : upcomingReturns) {
            String toEmail = borrow.getUserEntity().getEmail();
            String subject = "Library Notice: Book Return Reminder";
            String message = "Dear " + borrow.getUserEntity().getName() + ",\n\n" +
                    "This is a gentle reminder that the book '" + borrow.getBookEntity().getTitle() +
                    "' is due for return tomorrow (" + tomorrow + ").\n" +
                    "Please ensure it is returned on time to avoid any late fines.\n\n" +
                    "Thank you,\n" +
                    "Enterprise Book Borrowing System";

            emailService.sendSimpleMessage(toEmail, subject, message);
        }
    }
}