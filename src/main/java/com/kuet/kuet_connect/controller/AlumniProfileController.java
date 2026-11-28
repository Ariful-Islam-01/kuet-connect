package com.kuet.kuet_connect.controller;

import com.kuet.kuet_connect.dto.AlumniProfileUpdateDto;
import com.kuet.kuet_connect.entity.AlumniProfile;
import com.kuet.kuet_connect.entity.AlumniVerificationRequest;
import com.kuet.kuet_connect.security.CustomUserDetails;
import com.kuet.kuet_connect.service.AlumniProfileService;
import com.kuet.kuet_connect.service.AlumniVerificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AlumniProfileController {

    private final AlumniProfileService alumniProfileService;
    private final AlumniVerificationService verificationService;

    public AlumniProfileController(AlumniProfileService alumniProfileService,
                                   AlumniVerificationService verificationService) {
        this.alumniProfileService = alumniProfileService;
        this.verificationService = verificationService;
    }

    @GetMapping("/alumni/profile/edit")
    public String editProfilePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        AlumniProfile profile = alumniProfileService.getProfileByUserId(userDetails.getId());
        if (profile == null) {
            profile = new AlumniProfile();
        }

        AlumniProfileUpdateDto dto = new AlumniProfileUpdateDto();
        dto.setPassingYear(profile.getPassingYear());
        dto.setBatch(profile.getBatch());
        dto.setDegree(profile.getDegree());
        dto.setCurrentDesignation(profile.getCurrentDesignation());
        dto.setCurrentCompany(profile.getCurrentCompany());
        dto.setIndustry(profile.getIndustry());
        dto.setLocation(profile.getLocation());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setLinkedinUrl(profile.getLinkedinUrl());
        dto.setGithubUrl(profile.getGithubUrl());
        dto.setBio(profile.getBio());
        dto.setPrivacyLevel(profile.getPrivacyLevel());

        model.addAttribute("profileDto", dto);
        model.addAttribute("profile", profile);
        return "alumni/profile-edit";
    }

    @PostMapping("/alumni/profile/edit")
    public String saveProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute("profileDto") AlumniProfileUpdateDto dto,
            Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        alumniProfileService.updateProfile(userDetails.getId(), dto);
        return "redirect:/alumni/profile/edit?success=true";
    }

    @GetMapping("/alumni/verify")
    public String verificationPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Optional<AlumniVerificationRequest> latestRequest = verificationService.getLatestRequest(userDetails.getId());
        latestRequest.ifPresent(req -> model.addAttribute("verificationRequest", req));
        model.addAttribute("user", userDetails.getUser());

        return "alumni/verify";
    }

    @PostMapping("/alumni/verify")
    public String submitVerification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("studentId") String studentId,
            @RequestParam("documentUrl") String documentUrl) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        verificationService.submitVerificationRequest(userDetails.getId(), studentId, documentUrl);
        return "redirect:/alumni/verify?submitted=true";
    }
}
