package com.prog4.employee_db.controller.model;

import com.prog4.employee_db.entity.Sex;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ModelEmployee {
    private Long id;
    private String lastName;
    private String firstName;
    private int age;
    private BigDecimal salary;
    private LocalDate dateOfBirth;
    private Sex sex;
    private String registrationNbr;
    private String address;
    private MultipartFile photo;
    public MultipartFile getPhoto() {
        return photo;
    }
    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
    private String emailPerso;
    private String emailPro;
    private LocalDate beggingDate;
    private LocalDate outDate;
    private int nbrChildren;
    private Long socioPro;
    private String socioProCat;
    private String pays;
    private List<String> phoneNbr;
    private String cinNumber;
    private String cinDate;
    private String cinPlace;
    private String post;
    private String nbrCnaps;
}
