package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import edu.icet.ecom.model.enums.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowEntity, Long> {

    Page<BorrowEntity> findByUserEntity_Id(Long userId, Pageable pageable);

    List<BorrowEntity> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

    List<BorrowEntity> findByStatusInAndDueDateBefore(List<BorrowStatus> statuses, LocalDate date);

    List<BorrowEntity> findByStatusAndDueDate(BorrowStatus status, LocalDate date);

    Page<BorrowEntity> findByStatus(BorrowStatus status, Pageable pageable);

    long countByStatus(BorrowStatus status);
}