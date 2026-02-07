package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    boolean existsById(Long l);
    Optional<BookEntity> findById(Long l);
}
