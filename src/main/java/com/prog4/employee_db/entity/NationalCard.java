package com.prog4.employee_db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class NationalCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String number;
    private String date;
    private String place;
}
