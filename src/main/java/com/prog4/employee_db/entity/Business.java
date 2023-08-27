package com.prog4.employee_db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String slogan;
    @Column(unique = true)
    private String address;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private String logo;

    @OneToOne(cascade = CascadeType.ALL)
    private Fiscal idFiscal;

    @OneToMany
    private List<PhoneNumber> phoneNumbers;
}
