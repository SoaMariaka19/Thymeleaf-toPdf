package com.prog4.cnaps_db.repositories;

import com.prog4.cnaps_db.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity, Long> {
    @Query("SELECT e FROM EmployeeEntity e WHERE e.endToEndId = :id")
    Optional<EmployeeEntity> findEmployeeEntityByEndToEndId(Long id);
}
