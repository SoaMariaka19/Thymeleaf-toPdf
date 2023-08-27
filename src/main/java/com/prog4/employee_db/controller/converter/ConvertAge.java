package com.prog4.employee_db.controller.converter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Component
@AllArgsConstructor
public class ConvertAge {
    public static int exactAge(LocalDate birthDate, LocalDate currentDate) {
        if (currentDate == null) {
            currentDate = LocalDate.now();

            if (birthDate == null) {
                return 0;
            }
        }
        return Math.toIntExact(ChronoUnit.YEARS.between(birthDate, currentDate));
    }
}
