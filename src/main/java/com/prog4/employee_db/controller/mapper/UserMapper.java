package com.prog4.employee_db.controller.mapper;

import com.prog4.employee_db.controller.model.ModelUSer;
import com.prog4.employee_db.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {
    private BCryptPasswordEncoder passwordEncoder;

    public Member toEntity(ModelUSer modelUSer){
        return Member.builder()
                .password(passwordEncoder.encode(modelUSer.getPassword()))
                .username(modelUSer.getUsername())
                .role(modelUSer.getRole())
                .build();
    }
}
