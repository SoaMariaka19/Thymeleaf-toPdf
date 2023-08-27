package com.prog4.employee_db.controller;

import com.prog4.employee_db.service.SocioProService;
import com.prog4.employee_db.entity.Business;
import com.prog4.employee_db.entity.SocioPro;
import com.prog4.employee_db.repository.BusinessRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@AllArgsConstructor
@Controller
public class SocioProController {
    private final SocioProService service;
    private final BusinessRepository businessRepository;
    @PostMapping("/socio/save")
    public String save(@ModelAttribute("sociopro")SocioPro socioPro){
        service.saveSocioPro(socioPro);
        return "redirect:/employees";
    }

    @GetMapping("/socio/save")
    public String showSocioForm(Model model){
        List<Business> business = businessRepository.findAll();
        Business business1 = new Business();
        model.addAttribute("business",business.isEmpty() ? business1 : business.get(0));
        model.addAttribute("sociopro",new SocioPro());
        return "socio-pro/add-socio-pro";
    }

}
