package com.kuet.kuet_connect.repository;

import com.kuet.kuet_connect.entity.AlumniVerificationRequest;
import com.kuet.kuet_connect.entity.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumniVerificationRequestRepository extends JpaRepository<AlumniVerificationRequest, Long> {
    List<AlumniVerificationRequest> findByStatusOrderBySubmittedAtDesc(VerificationStatus status);
    Optional<AlumniVerificationRequest> findTopByUserIdOrderBySubmittedAtDesc(Long userId);
    List<AlumniVerificationRequest> findByUserIdOrderBySubmittedAtDesc(Long userId);
}
