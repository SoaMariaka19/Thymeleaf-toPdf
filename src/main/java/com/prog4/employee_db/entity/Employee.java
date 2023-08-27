package com.prog4.employee_db.entity;

import com.prog4.employee_db.controller.converter.ConvertAge;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private Sex sex;
    @Column(unique = true)
    private String registrationNbr;
    private String address;
    @Column(columnDefinition = "text")
    private String image = "aucune image";
    private String emailPerso;
    private String emailPro;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDate beggingDate;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDate outDate;
    private int nbrChildren = 0;
    private String post;
    private String pays;
    private BigDecimal salary;
    /*
    * All foreign keys
    *  */

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private SocioPro cateSocioPro;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private NationalCard cin;

    @OneToMany
    private List<PhoneNumber> phoneNumbers;

    @PrePersist
    public void generatedRegistrationNbr(){
        this.registrationNbr = "EMP-" + UUID.randomUUID();
    }

    // to update form
    @Transient
    private String formattedBeggingDate;
    @Transient
    private String formattedOutDate;
    @Transient
    private String birthDay;
    @Transient
    private int age;
    @PostLoad
    public void loadAge(){
        age = ConvertAge.exactAge(this.getDateOfBirth(), null);
    }
}

