package com.kuet.kuet_connect.service;

import com.kuet.kuet_connect.entity.AlumniVerificationRequest;

import java.util.Optional;

public interface AlumniVerificationService {
    AlumniVerificationRequest submitVerificationRequest(Long userId, String studentId, String documentUrl);
    Optional<AlumniVerificationRequest> getLatestRequest(Long userId);
}
