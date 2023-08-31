package com.prog4.employee_db.controller.mapper;

import com.prog4.employee_db.controller.model.ModelEmployee;
import com.prog4.employee_db.entity.*;
import com.prog4.employee_db.service.*;
import com.prog4.employee_db.repository.PhoneNumberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@AllArgsConstructor
public class EmployeeMapper {

    private NationalCardService nationalCardService;
    private final PhoneNumberRepository phoneNumberRepository;
    private SocioProService socioProService;
    public Employee toEntity(ModelEmployee employee) throws IOException {
        SocioPro socioPro = socioProService.getByCategory(employee.getSocioProCat());
        socioPro.setCategories(employee.getSocioProCat());
        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        for (String number : employee.getPhoneNbr()) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setNumber(employee.getPays() + number);
            phoneNumbers.add(phoneNumber);
        }

        NationalCard nationalCard = new NationalCard();
        nationalCard.setNumber(employee.getCinNumber());
        nationalCard.setDate(employee.getCinDate());
        nationalCard.setPlace(employee.getCinPlace());

        SocioPro socio = new SocioPro();
        NationalCard nationalCard1 = new NationalCard();

        List<PhoneNumber> savedPhoneNumbers = phoneNumberRepository.saveAll(phoneNumbers);
        if(!savedPhoneNumbers.isEmpty()){
            socio = socioProService.saveSocioPro(socioPro);
            if(socio != null){
                nationalCard1 = nationalCardService.save(nationalCard);
            }
        }


        String photo = Base64.getEncoder().encodeToString(employee.getPhoto().getBytes());
        if (employee.getPhoto() != null && !photo.isEmpty()) {
            employee.setPhoto(employee.getPhoto());
        } else {
            employee.setPhoto(new CustomMultipartFile("aucune image"));
        }
        return Employee.builder()
                .cin(nationalCard1)
                .id(employee.getId())
                .dateOfBirth(employee.getDateOfBirth())
                .beggingDate(employee.getBeggingDate())
                .image(photo)
                .pays(employee.getPays())
                .cateSocioPro(socio)
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .emailPerso(employee.getEmailPerso())
                .emailPro(employee.getEmailPro())
                .phoneNumbers(savedPhoneNumbers)
                .outDate(employee.getOutDate())
                .sex(employee.getSex())
                .post(employee.getPost())
                .nbrChildren(employee.getNbrChildren())
                .address(employee.getAddress())
                .build();
    }
    public Employee toUpdate(ModelEmployee employee) throws IOException {
        SocioPro socioPro = socioProService.getByCategory(employee.getSocioProCat());
        socioPro.setCategories(employee.getSocioProCat());
        List<PhoneNumber> phoneNbrs = new ArrayList<>();
        for (String number : employee.getPhoneNbr()) {
            PhoneNumber number1 = phoneNumberRepository.findPhoneNumbersByNumber(number);
            phoneNbrs.add(number1);
        }

        return Employee.builder()
                .id(employee.getId())
                .dateOfBirth(employee.getDateOfBirth())
                .birthDay(String.valueOf(employee.getDateOfBirth()))
                .formattedOutDate(String.valueOf(employee.getOutDate()))
                .formattedBeggingDate(String.valueOf(employee.getBeggingDate()))
                .beggingDate(employee.getBeggingDate())
                .pays(employee.getPays())
                .cateSocioPro(socioPro)
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .emailPerso(employee.getEmailPerso())
                .emailPro(employee.getEmailPro())
                .registrationNbr(employee.getRegistrationNbr())
                .phoneNumbers(phoneNbrs)
                .outDate(employee.getOutDate())
                .sex(employee.getSex())
                .post(employee.getPost())
                .nbrChildren(employee.getNbrChildren())
                .address(employee.getAddress())
                .build();
    }
}