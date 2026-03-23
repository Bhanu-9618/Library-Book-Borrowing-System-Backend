package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsById(Long id);

    Optional<BookEntity> findById(Long id);

    @Query("SELECT b FROM BookEntity b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(b.category) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<BookEntity> searchBooksByTerm(@Param("term") String term, Pageable pageable);
}