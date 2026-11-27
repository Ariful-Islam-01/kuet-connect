package com.kuet.kuet_connect.service.impl;

import com.kuet.kuet_connect.entity.AlumniVerificationRequest;
import com.kuet.kuet_connect.entity.User;
import com.kuet.kuet_connect.entity.enums.VerificationStatus;
import com.kuet.kuet_connect.repository.AlumniVerificationRequestRepository;
import com.kuet.kuet_connect.repository.UserRepository;
import com.kuet.kuet_connect.service.AlumniVerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AlumniVerificationServiceImpl implements AlumniVerificationService {

    private final AlumniVerificationRequestRepository requestRepository;
    private final UserRepository userRepository;

    public AlumniVerificationServiceImpl(AlumniVerificationRequestRepository requestRepository,
                                         UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AlumniVerificationRequest submitVerificationRequest(Long userId, String studentId, String documentUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        AlumniVerificationRequest request = new AlumniVerificationRequest();
        request.setUser(user);
        request.setStudentId(studentId != null ? studentId.trim() : user.getStudentId());
        request.setDocumentUrl(documentUrl != null ? documentUrl.trim() : null);
        request.setStatus(VerificationStatus.PENDING);
        request.setSubmittedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlumniVerificationRequest> getLatestRequest(Long userId) {
        return requestRepository.findTopByUserIdOrderBySubmittedAtDesc(userId);
    }
}
