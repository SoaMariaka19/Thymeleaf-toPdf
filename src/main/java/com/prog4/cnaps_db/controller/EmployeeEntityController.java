package com.prog4.cnaps_db.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmployeeEntityController {
    @GetMapping("cnaps/ping")
    public String healthController(){
        return "pong";
    }
}
