package com.prog4.employee_db.controller;

import com.prog4.employee_db.controller.mapper.EmployeeMapper;
import com.prog4.employee_db.controller.model.ModelEmployee;
import com.prog4.employee_db.entity.*;
import com.prog4.employee_db.service.EmployeeService;
import com.prog4.employee_db.service.MapDTOService;
import com.prog4.employee_db.service.SocioProService;
import com.prog4.employee_db.service.validator.AlphanumericValidator;
import com.prog4.employee_db.service.validator.PhoneValidator;
import com.prog4.employee_db.repository.BusinessRepository;
import com.prog4.employee_db.repository.EmployeeRepository;
import com.prog4.employee_db.repository.PhoneNumberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository repository;
    private final MapDTOService mapDTOService;
    private final PhoneNumberRepository phoneNumberRepository;
    private final BusinessRepository businessRepository;
    private SocioProService socioProService;
    private EmployeeMapper mapper;
    private final AlphanumericValidator validator;
    private final PhoneValidator phoneValidator;
    @GetMapping
    public String getAllEmployees(Model model){
        List<Employee> employees = employeeService.findAll();
        List<Business> business = businessRepository.findAll();
        Business business1 = new Business();
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        model.addAttribute("employees", employees);

        return "employee/list_employee";
    }

    @ModelAttribute("sexOptions")
    public Sex[] getSexOptions() {
        return Sex.values();
    }

    @ModelAttribute("suggestedSocioProOptions")
    public List<SocioPro> getSuggestedSocioProOptions() {
        return socioProService.findAll();
    }

    @GetMapping("/filter")
    public String filterAndSortEmployees(
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "sex", required = false) Sex sex,
            @RequestParam(name = "postName", required = false) String postName,
            @RequestParam(name = "minHireDate", required = false) String minHireDate,
            @RequestParam(name = "maxHireDate", required = false) String maxHireDate,
            @RequestParam(name = "minLeaveDate", required = false) String minLeaveDate,
            @RequestParam(name = "maxLeaveDate", required = false) String maxLeaveDate,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            @RequestParam(name = "phoneNumber", required = false) List<String> phone,
            Model model
    ) {
        List<Business> business = businessRepository.findAll();
        Business business1 = new Business();
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        Specification<Employee> spec = employeeService.buildEmployeeSpecification(
                lastName, firstName, sex, postName, minHireDate, maxHireDate, minLeaveDate, maxLeaveDate , phone);

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = Sort.by(sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        }

        List<Employee> filteredAndSortedEmployees = repository.findAll(spec, sort);
        model.addAttribute("employees", filteredAndSortedEmployees);

        return "employee/list_employee";
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model){
        List<Business> business = businessRepository.findAll();
        Business business1 = new Business();
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        model.addAttribute("employee", new ModelEmployee());
        return "employee/add-employee";
    }

    @PostMapping("/add")
    public String addEmployee(
            @ModelAttribute("employee") ModelEmployee modelEmployee,
            Model modelError
    ) throws IOException {
       try{
           if(validator.checkIfAlphanumeric(modelEmployee.getCinNumber())){
               for(String number : modelEmployee.getPhoneNbr()){
                   if(!phoneValidator.phoneCheck(number)){
                       modelError.addAttribute("phoneError","phone number size must be equal 10");
                       return "employee/add-employee";
                   }
               }
               Employee employee = mapper.toEntity(modelEmployee);
               employeeService.save(employee);
               return "redirect:/employees/" + employee.getId();
           }
            modelError.addAttribute("cnapsError","cnaps number must be alphanumeric only [a-zA-Z0-9]");
            return "employee/add-employee";
       }
       catch (DataIntegrityViolationException ex) {
           modelError.addAttribute("errorMessage", "Registration number must be unique.");
           return "employee/add-employee";
       }
    }


    @GetMapping("/{id}")
    public String showEmployeeDetails(
            @PathVariable("id") Long id, Model model
    ){
        List<Business> business = businessRepository.findAll();
        Business business1 = new Business();
        ModelEmployee modelEmployee = mapDTOService.getByEndToEndId(id);
        model.addAttribute("employee", modelEmployee);
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        return "employee/profiles";
    }

    @GetMapping("/{id}/update")
    public String showUpdateEmployeeForm(@PathVariable("id") Long employeeId, Model model) throws IOException {
        List<Business> business = businessRepository.findAll();
        Business business1 = new Business();
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        ModelEmployee modelEmployee = mapDTOService.getByEndToEndId(employeeId);
        Employee employee = mapper.toEntity(modelEmployee);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        employee.setFormattedBeggingDate(employee.getBeggingDate().format(formatter));
        if(employee.getOutDate() != null){
            employee.setFormattedOutDate(employee.getOutDate().format(formatter));
        }
        else{
            employee.setFormattedOutDate("");
        }
        model.addAttribute("newEmployee", employee);
        return "employee/update-employee";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("newEmployee") Employee employee) throws IOException {
        ModelEmployee employee1 = mapDTOService.getByEndToEndId(employee.getId());
        Employee employee2 = mapper.toEntity(employee1);
        Employee emp = employeeService.save(employee2);
        return "redirect:/employees/" + emp.getId();
    }

    @GetMapping("/export-csv")
    public void exportCsv(HttpServletResponse response , @ModelAttribute("employees") List<Employee> employeeList) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"employees.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("First Name,Last Name,Date of Birth,Registration Number");
            for (Employee employee : employeeList) {
                writer.println(
                        employee.getFirstName() + "," +
                                employee.getLastName() + "," +
                                employee.getDateOfBirth() + "," +
                                employee.getSex() + "," +
                                employee.getRegistrationNbr() + "," +
                                employee.getAddress() + "," +
                                employee.getPost() + "," +
                                employee.getEmailPerso() + "," +
                                employee.getEmailPro() + "," +
                                employee.getBeggingDate() + "," +
                                employee.getOutDate() + "," +
                                employee.getNbrChildren() + "," +
                                employee.getCateSocioPro().getCategories() + "," +
                                employee.getCin().getNumber() + "," +
                                employee.getCin().getDate() + "," +
                                employee.getCin().getPlace() + ","
                );
            }
        }
    }

}