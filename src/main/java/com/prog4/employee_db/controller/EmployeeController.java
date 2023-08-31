package com.prog4.employee_db.controller;

import com.lowagie.text.DocumentException;
import com.prog4.employee_db.controller.converter.ConvertIntoPDF;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xhtmlrenderer.pdf.ITextRenderer;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository repository;
    private final MapDTOService mapDTOService;
    private final ConvertIntoPDF converter;
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
        Employee employee = mapper.toUpdate(modelEmployee);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        employee.setFormattedBeggingDate(employee.getBeggingDate().format(formatter));
        if(employee.getOutDate() != null){
            employee.setFormattedOutDate(employee.getOutDate().format(formatter));
        }
        else{
            employee.setFormattedOutDate("");
        }
        if(employee.getDateOfBirth() != null){
            employee.setBirthDay(employee.getDateOfBirth().format(formatter));
        }
        else{
            employee.setBirthDay("");
        }
        model.addAttribute("newEmployee", employee);
        return "employee/update-employee";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("newEmployee") Employee employee) throws IOException {
        ModelEmployee employee1 = mapDTOService.getByEndToEndId(employee.getId());
        Employee employee2 = mapper.toUpdate(employee1);
        employee2.setBirthDay(employee.getBirthDay());
        employee2.setDateOfBirth(LocalDate.parse(employee.getBirthDay()));
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
    @GetMapping(value = "/{id}/asPdf", produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> toPdf(@PathVariable("id") Long employeeId) throws ServerException {
        ModelEmployee employee = mapDTOService.getByEndToEndId(employeeId);
        byte[] pdfCardAsBytes = converter.getPdfCard(employee);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "employee.csv");
        headers.setContentLength(pdfCardAsBytes.length);
        return new ResponseEntity<>(pdfCardAsBytes, headers, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<InputStreamResource> getPdf(@PathVariable("id") Long employeeId,
                                                      @RequestParam(value = "b_interval",required = false) String birthDayInterval,
                                                      @RequestParam(value = "mode", required = false) AgeCalculationMode mode,
                                                      HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        ModelEmployee modelEmployee = mapDTOService.getByEndToEndId(employeeId);

        int calculatedAge = 0;
        if (mode.equals(BIRTHDAY)) {
            calculatedAge = CalculateAge.calculateAgeAtExactBirthday(modelEmployee.getDateOfBirth(), null);
        } else if (mode == YEAR_ONLY) {
            calculatedAge = LocalDate.now().getYear() - modelEmployee.getDateOfBirth().getYear();
        } else if (mode == CUSTOM_DELAY) {
            LocalDate calculationDate = LocalDate.now().minusDays(Integer.parseInt(birthDayInterval));
            calculatedAge = CalculateAge.calculateAgeAtExactBirthday(modelEmployee.getDateOfBirth(), calculationDate);
        }

        modelEmployee.setAge(calculatedAge);

        String parsedHtml = toPDF.parseEmployeeInfoTemplate(modelEmployee, request, response);


    }