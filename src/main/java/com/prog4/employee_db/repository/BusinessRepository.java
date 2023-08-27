package com.prog4.employee_db.repository;

import com.prog4.employee_db.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business , Long> {
}
