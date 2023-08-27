package com.prog4.employee_db.repository;

import com.prog4.employee_db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    @Query("SELECT u FROM Member u WHERE u.username = :username")
    Member findMemberByUsername(@Param("username") String username);
}