package com.prog4.employee_db.service;

import com.prog4.employee_db.entity.Employee;
import com.prog4.employee_db.entity.PhoneNumber;
import com.prog4.employee_db.entity.Sex;
import com.prog4.employee_db.repository.EmployeeRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    public List<Employee> findAll(){
        return repository.findAll();
    }

    public Employee findById(Long id){
        Optional<Employee> optionalEmployee = repository.findById(id);

        return optionalEmployee.orElseThrow(()->new RuntimeException("no employee with id " +id+ " found"));
    }

    public Employee save(Employee employees){
        repository.save(employees);
        return this.findById(employees.getId());
    }

    public Specification<Employee> buildEmployeeSpecification(
            String lastName,
            String firstName,
            Sex sex,
            String postName,
            String minHireDate,
            String maxHireDate,
            String minLeaveDate,
            String maxLeaveDate,
            List<String> phoneNumber
    ) {
        Specification<Employee> spec = Specification.where(null);

        if (lastName != null && !lastName.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.like(root.get("lastName"), "%" + lastName + "%"));
        }

        if (firstName != null && !firstName.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.like(root.get("firstName"), "%" + firstName + "%"));
        }

        if (sex != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("sex"), sex));
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            AtomicReference<Predicate> predicate = new AtomicReference<>();
            spec = spec.and((root, query, builder) -> {
                Join<Employee, PhoneNumber> postJoin = root.join("phoneNumbers");
                for(String number : phoneNumber){
                    predicate.set(builder.like(postJoin.get("number"),  number + "%"));
                }
                return predicate.get();
            });
        }
        if (postName != null && !postName.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.like(root.get("post"), postName + "%"));
        }

        if (minHireDate != null && !minHireDate.isEmpty()) {
            LocalDate minDate = LocalDate.parse(minHireDate);
            spec = spec.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("beggingDate"), minDate));
        }

        if (maxHireDate != null && !maxHireDate.isEmpty()) {
            LocalDate maxDate = LocalDate.parse(maxHireDate);
            spec = spec.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("beggingDate"), maxDate));
        }

        if (minLeaveDate != null && !minLeaveDate.isEmpty()) {
            LocalDate minDate = LocalDate.parse(minLeaveDate);
            spec = spec.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("outDate"), minDate));
        }

        if (maxLeaveDate != null && !maxLeaveDate.isEmpty()) {
            LocalDate maxDate = LocalDate.parse(maxLeaveDate);
            spec = spec.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("outDate"), maxDate));
        }

        return spec;
    }
}
