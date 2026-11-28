package com.kuet.kuet_connect.service.impl;

import com.kuet.kuet_connect.dto.AlumniProfileUpdateDto;
import com.kuet.kuet_connect.entity.AlumniProfile;
import com.kuet.kuet_connect.entity.User;
import com.kuet.kuet_connect.repository.AlumniProfileRepository;
import com.kuet.kuet_connect.repository.UserRepository;
import com.kuet.kuet_connect.service.AlumniProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumniProfileServiceImpl implements AlumniProfileService {

    private final AlumniProfileRepository alumniProfileRepository;
    private final UserRepository userRepository;

    public AlumniProfileServiceImpl(AlumniProfileRepository alumniProfileRepository,
                                    UserRepository userRepository) {
        this.alumniProfileRepository = alumniProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AlumniProfile getProfileByUserId(Long userId) {
        return alumniProfileRepository.findByUserId(userId).orElse(null);
    }

    @Override
    @Transactional
    public AlumniProfile updateProfile(Long userId, AlumniProfileUpdateDto dto) {
        AlumniProfile profile = alumniProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
                    AlumniProfile newProfile = new AlumniProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        if (dto.getPassingYear() != null) profile.setPassingYear(dto.getPassingYear());
        if (dto.getBatch() != null) profile.setBatch(dto.getBatch().trim());
        if (dto.getDegree() != null) profile.setDegree(dto.getDegree().trim());
        if (dto.getCurrentDesignation() != null) profile.setCurrentDesignation(dto.getCurrentDesignation().trim());
        if (dto.getCurrentCompany() != null) profile.setCurrentCompany(dto.getCurrentCompany().trim());
        if (dto.getIndustry() != null) profile.setIndustry(dto.getIndustry().trim());
        if (dto.getLocation() != null) profile.setLocation(dto.getLocation().trim());
        if (dto.getPhoneNumber() != null) profile.setPhoneNumber(dto.getPhoneNumber().trim());
        if (dto.getLinkedinUrl() != null) profile.setLinkedinUrl(dto.getLinkedinUrl().trim());
        if (dto.getGithubUrl() != null) profile.setGithubUrl(dto.getGithubUrl().trim());
        if (dto.getBio() != null) profile.setBio(dto.getBio().trim());
        if (dto.getPrivacyLevel() != null) profile.setPrivacyLevel(dto.getPrivacyLevel());

        return alumniProfileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlumniProfile> searchAlumni(String department, String batch, String company, String keyword) {
        String cleanDept = (department != null && !department.isBlank()) ? department.trim() : null;
        String cleanBatch = (batch != null && !batch.isBlank()) ? batch.trim() : null;
        String cleanCompany = (company != null && !company.isBlank()) ? company.trim() : null;
        String cleanKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;

        return alumniProfileRepository.searchAlumni(cleanDept, cleanBatch, cleanCompany, cleanKeyword);
    }
}
