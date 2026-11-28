package com.kuet.kuet_connect.controller;

import com.kuet.kuet_connect.entity.AlumniProfile;
import com.kuet.kuet_connect.repository.AlumniProfileRepository;
import com.kuet.kuet_connect.service.AlumniProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AlumniDirectoryController {

    private final AlumniProfileService alumniProfileService;
    private final AlumniProfileRepository alumniProfileRepository;

    public AlumniDirectoryController(AlumniProfileService alumniProfileService,
                                     AlumniProfileRepository alumniProfileRepository) {
        this.alumniProfileService = alumniProfileService;
        this.alumniProfileRepository = alumniProfileRepository;
    }

    @GetMapping("/alumni/directory")
    public String directory(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String keyword,
            Model model) {

        List<AlumniProfile> alumniList = alumniProfileService.searchAlumni(department, batch, company, keyword);
        model.addAttribute("alumniList", alumniList);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("selectedBatch", batch);
        model.addAttribute("selectedCompany", company);
        model.addAttribute("keyword", keyword);

        return "alumni/directory";
    }

    @GetMapping("/alumni/profile/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        AlumniProfile profile = alumniProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));
        model.addAttribute("profile", profile);
        return "alumni/profile-view";
    }
}
