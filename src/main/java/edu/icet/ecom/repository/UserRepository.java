package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {
    boolean existsById(Long userEntity);
    boolean existsByEmail(String email);
    Optional<UserEntity> findById(Long id);
    void deleteById(Long id);

    @Query("SELECT u FROM UserEntity u WHERE STR(u.id) LIKE %:term% OR LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<UserEntity> searchByTerm(@Param("term") String term);
}
