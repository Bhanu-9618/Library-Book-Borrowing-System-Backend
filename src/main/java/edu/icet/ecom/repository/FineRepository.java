package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.FineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    Optional<FineEntity> findByBorrowEntity_Borrowid(Long borrowId);

    List<FineEntity> findByBorrowEntity_BorrowidIn(List<Long> borrowIds);
}