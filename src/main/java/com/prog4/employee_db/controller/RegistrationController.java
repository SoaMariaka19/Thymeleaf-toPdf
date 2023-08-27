package com.prog4.employee_db.controller;

import com.prog4.employee_db.controller.mapper.UserMapper;
import com.prog4.employee_db.controller.model.ModelUSer;
import com.prog4.employee_db.service.UserService;
import com.prog4.employee_db.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class RegistrationController {
    private final UserService service;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    @GetMapping( "/login")
    public String login(){
        return "auth/login";
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(){
        return "redirect:/login";
    }
    @GetMapping("/main")
    public String index(){
        return "index";
    }

    @PostMapping(value = "/authenticate" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public String auth(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("loginError") Model error
    ){
        String username = request.getParameter("password");
        String password = request.getParameter("username");
        if(service.findMemberByUsername(username) != null){
            if(service.loadUserByUsername(username).isEnabled()){
                redirectAttributes.addAttribute("success","logged with succes code");
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);
            }
            error.addAttribute("loginErrorCredential","Verify credentials");
        }
        error.addAttribute("loginErrorCredential","Verify credentials");
        return "redirect:/login";
    }
    @GetMapping("/registry")
    public String registryForm(Model model) {
        model.addAttribute("member", new ModelUSer());
        return "auth/registration";
    }

    @PostMapping("/registry")
    public String registry(
            HttpServletRequest request,
            @ModelAttribute ModelUSer registryRequest) {
        try {
            Member newUser = service.save(mapper.toEntity(registryRequest));
            if(newUser == null){
                return "redirect:/register?error";
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registryRequest.getUsername(),registryRequest.getPassword()));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);

            return "redirect:/";

        } catch (Exception e){
            return "redirect:/register?error";
        }

    }
}
