package com.kuet.kuet_connect.controller;

import com.kuet.kuet_connect.dto.AlumniRegisterDto;
import com.kuet.kuet_connect.dto.StudentRegisterDto;
import com.kuet.kuet_connect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerOptions() {
        return "auth/register-options";
    }

    @GetMapping("/register/student")
    public String studentRegisterForm(Model model) {
        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new StudentRegisterDto());
        }
        return "auth/register-student";
    }

    @PostMapping("/register/student")
    public String processStudentRegistration(
            @Valid @ModelAttribute("student") StudentRegisterDto dto,
            BindingResult bindingResult,
            Model model) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.student", "Passwords do not match");
        }

        if (userService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.student", "Email is already in use");
        }

        if (userService.existsByStudentId(dto.getStudentId())) {
            bindingResult.rejectValue("studentId", "error.student", "Student ID is already registered");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register-student";
        }

        try {
            userService.registerStudent(dto);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register-student";
        }
    }

    @GetMapping("/register/alumni")
    public String alumniRegisterForm(Model model) {
        if (!model.containsAttribute("alumni")) {
            model.addAttribute("alumni", new AlumniRegisterDto());
        }
        return "auth/register-alumni";
    }

    @PostMapping("/register/alumni")
    public String processAlumniRegistration(
            @Valid @ModelAttribute("alumni") AlumniRegisterDto dto,
            BindingResult bindingResult,
            Model model) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.alumni", "Passwords do not match");
        }

        if (userService.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "error.alumni", "Email is already in use");
        }

        if (userService.existsByStudentId(dto.getStudentId())) {
            bindingResult.rejectValue("studentId", "error.alumni", "Student ID is already registered");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register-alumni";
        }

        try {
            userService.registerAlumni(dto);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register-alumni";
        }
    }
}
