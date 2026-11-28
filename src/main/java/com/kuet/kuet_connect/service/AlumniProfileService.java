package com.kuet.kuet_connect.service;

import com.kuet.kuet_connect.dto.AlumniProfileUpdateDto;
import com.kuet.kuet_connect.entity.AlumniProfile;

import java.util.List;

public interface AlumniProfileService {
    AlumniProfile getProfileByUserId(Long userId);
    AlumniProfile updateProfile(Long userId, AlumniProfileUpdateDto dto);
    List<AlumniProfile> searchAlumni(String department, String batch, String company, String keyword);
}
