package edu.icet.ecom.scheduler;

import edu.icet.ecom.model.entity.BorrowEntity;
import edu.icet.ecom.model.entity.FineEntity;
import edu.icet.ecom.repository.BorrowRepository;
import edu.icet.ecom.repository.FineRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FineScheduler {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private FineRepository fineRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void calculateLateFines() {
        log.info("Starting nightly fine calculation scheduler...");

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

        log.info("Completed nightly fine calculation for {} overdue records.", overdueBorrows.size());
    }
}