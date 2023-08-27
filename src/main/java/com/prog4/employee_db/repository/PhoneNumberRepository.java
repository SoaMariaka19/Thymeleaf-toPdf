package com.prog4.employee_db.repository;

import com.prog4.employee_db.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber , Long> {
    @Query("SELECT u FROM PhoneNumber u WHERE u.number = :number")
    PhoneNumber findPhoneNumbersByNumber(@Param("number") String number);
}
