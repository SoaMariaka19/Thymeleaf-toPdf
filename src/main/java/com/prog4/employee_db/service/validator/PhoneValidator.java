package com.prog4.employee_db.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class PhoneValidator {
    public boolean phoneCheck(String number){
        List<String> characterList = List.of(number.split(""));
        return characterList.size() == 10;
    }
}
