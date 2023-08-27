package com.prog4.employee_db.controller;

import com.prog4.employee_db.controller.mapper.BusinessMapper;
import com.prog4.employee_db.controller.model.ModelBusiness;
import com.prog4.employee_db.entity.Business;
import com.prog4.employee_db.entity.Employee;
import com.prog4.employee_db.repository.BusinessRepository;
import com.prog4.employee_db.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@CrossOrigin("*")
@Controller
public class BusinessController {
    private final BusinessMapper mapper;
    private final BusinessRepository repository;
    private final EmployeeRepository employeeRepository;
    @GetMapping("/business/create")
    public String showBusinessForm(Model model) {
        model.addAttribute("business", new ModelBusiness());
        return "business/business";
    }
    @GetMapping("/business")
    public String profileBusiness(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        Set<String> posts = new HashSet<>();
        for (Employee post : employees){
            posts.add(post.getPost());
        }
        List<Business> business = repository.findAll();
        Business business1 = new Business();
        if (business.isEmpty()){
            model.addAttribute("business", business1);
            model.addAttribute("employee", posts);
        }
        model.addAttribute("employee", posts);
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        return "business/profiles";
    }
    @PostMapping("/business/create")
    public String createBusiness(@ModelAttribute("business") ModelBusiness business,
                                 @RequestParam("logoFile") MultipartFile logoFile,
                                 RedirectAttributes redirectAttributes) throws IOException {
        business.setLogo(logoFile);
        mapper.toEntity(business);
        redirectAttributes.addFlashAttribute("successMessage", "Business created successfully!");
        return "redirect:/";
    }
    @GetMapping("/")
    public String getBusiness(Model model) throws IOException {
        List<Business> business = repository.findAll();
        Business business1 = new Business();
        if (business.isEmpty()){
            model.addAttribute("business", business1);
        }
        model.addAttribute("business",business);
        return "/index";
    }
}
