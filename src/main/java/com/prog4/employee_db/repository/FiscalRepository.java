package com.prog4.employee_db.repository;

import com.prog4.employee_db.entity.Fiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiscalRepository extends JpaRepository<Fiscal, Long> {
}
