package com.prog4.employee_db.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Component
public class AlphanumericValidator {

    public boolean checkIfAlphanumeric(String args){

        String regex = "^[a-zA-Z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(args);
        return matcher.matches();
    }
}
