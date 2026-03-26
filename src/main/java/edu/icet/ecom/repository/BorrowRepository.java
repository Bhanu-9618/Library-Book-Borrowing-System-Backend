package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import edu.icet.ecom.model.enums.BorrowStatus;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowEntity, Long> {

    List<BorrowEntity> findByUserEntity_Id(Long userId);

    List<BorrowEntity> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

    List<BorrowEntity> findByStatusAndDueDate(BorrowStatus status, LocalDate date);

    List<BorrowEntity> findByStatus(BorrowStatus status);

    long countByStatus(BorrowStatus status);
}