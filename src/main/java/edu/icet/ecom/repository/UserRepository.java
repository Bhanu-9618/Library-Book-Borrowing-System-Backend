package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    Optional<UserEntity> findById(Long id);
    void deleteById(Long id);

    @Query("SELECT u FROM UserEntity u WHERE STR(u.id) LIKE %:term% OR LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<UserEntity> searchByTerm(@Param("term") String term, Pageable pageable);

    Optional<UserEntity> findByEmail(String email);
}