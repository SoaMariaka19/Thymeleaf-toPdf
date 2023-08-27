package com.prog4.employee_db.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModelUSer {
    private String username;
    private String password;
    private String role = "ADMIN";
    private boolean enabled = true;
}
