package com.prog4.employee_db.repository;

import com.prog4.employee_db.entity.NationalCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface NationalCardRepository extends JpaRepository<NationalCard, Long> {
    @Query("SELECT nc FROM NationalCard nc WHERE UPPER(nc.number) = UPPER(:number)")
    Optional<NationalCard> findNationalCardByNumberEqualsIgnoreCase(@Param("number") String number);
}
