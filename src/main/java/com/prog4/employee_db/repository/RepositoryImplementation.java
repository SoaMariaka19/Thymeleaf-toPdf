package com.prog4.employee_db.repository;

import com.prog4.cnaps_db.entity.EmployeeEntity;
import com.prog4.cnaps_db.repositories.EmployeeEntityRepository;
import com.prog4.employee_db.controller.model.ModelEmployee;
import com.prog4.employee_db.entity.Employee;
import com.prog4.employee_db.entity.PhoneNumber;
import com.prog4.employee_db.repository.EmployeeRepository;
import com.prog4.employee_db.service.NationalCardService;
import com.prog4.employee_db.service.SocioProService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RepositoryImplementation {
    private SocioProService socioProService;
    private EmployeeRepository employeeRepository; // Repository for employee_db

    private EmployeeEntityRepository employeeEntityRepository; // Repository for cnaps_db

    public ModelEmployee getEmployeeDtoByEndToEndId(Long endToEndId) {
        Optional<Employee> employee = employeeRepository.findById(endToEndId);
        Optional<EmployeeEntity> employeeEntity = employeeEntityRepository.findEmployeeEntityByEndToEndId(endToEndId);

        return mapToDto(employee.orElseThrow(() -> new RuntimeException("employee id "+endToEndId+"not found")), employeeEntity.orElseGet(EmployeeEntity::new));
    }

    private ModelEmployee mapToDto(Employee employee, EmployeeEntity employeeEntity) {
        List<String> phoneNumbers = new ArrayList<>();
        for(PhoneNumber number : employee.getPhoneNumbers()){
            phoneNumbers.add(number.getNumber());
        }
        ModelEmployee modelEmployee = new ModelEmployee();
        modelEmployee.setId(employee.getId());
        modelEmployee.setLastName(employee.getLastName());
        modelEmployee.setFirstName(employee.getFirstName());
        modelEmployee.setAge(employee.getAge());
        modelEmployee.setSalary(employee.getSalary());
        modelEmployee.setSocioPro(employee.getCateSocioPro().getId());
        modelEmployee.setDateOfBirth(employee.getDateOfBirth());
        modelEmployee.setSex(employee.getSex());
        modelEmployee.setRegistrationNbr(employee.getRegistrationNbr());
        modelEmployee.setPhoneNbr(phoneNumbers);
        modelEmployee.setAddress(employee.getAddress());
        modelEmployee.setPays(employee.getPays());
        modelEmployee.setEmailPerso(employee.getEmailPerso());
        modelEmployee.setEmailPro(employee.getEmailPro());
        modelEmployee.setBeggingDate(employee.getBeggingDate());
        modelEmployee.setOutDate(employee.getOutDate());
        modelEmployee.setNbrChildren(employee.getNbrChildren());
        modelEmployee.setNbrCnaps(employeeEntity.getNbrCnaps());

        if (employee.getCateSocioPro() != null) {
            modelEmployee.setSocioProCat(socioProService.getById(employee.getCateSocioPro().getId()).getCategories());
        }
        if (employee.getCin() != null) {
            modelEmployee.setCinNumber(employee.getCin().getNumber());
            modelEmployee.setCinDate(employee.getCin().getDate());
            modelEmployee.setCinPlace(employee.getCin().getPlace());
        }
        if (employee.getPost() != null) {
            modelEmployee.setPost(employee.getPost());
        }
        return modelEmployee;
    }
}
