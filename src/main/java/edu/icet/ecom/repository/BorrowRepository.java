package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.BorrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowEntity,Long> {
}
