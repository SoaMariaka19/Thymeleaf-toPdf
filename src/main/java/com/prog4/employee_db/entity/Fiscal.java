package com.prog4.employee_db.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Fiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String NIF;
    @Column(unique = true)
    private String STAT;
    @Column(unique = true)
    private String RCS;
}
