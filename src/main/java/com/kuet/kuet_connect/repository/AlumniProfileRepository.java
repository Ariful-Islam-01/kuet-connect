package com.kuet.kuet_connect.repository;

import com.kuet.kuet_connect.entity.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {

    Optional<AlumniProfile> findByUserId(Long userId);

    @Query("SELECT p FROM AlumniProfile p JOIN FETCH p.user u " +
           "WHERE (:department IS NULL OR LOWER(u.department) = LOWER(:department)) " +
           "AND (:batch IS NULL OR LOWER(p.batch) LIKE LOWER(CONCAT('%', :batch, '%'))) " +
           "AND (:company IS NULL OR LOWER(p.currentCompany) LIKE LOWER(CONCAT('%', :company, '%'))) " +
           "AND (:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(p.currentDesignation) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(p.currentCompany) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY p.passingYear DESC NULLS LAST, u.fullName ASC")
    List<AlumniProfile> searchAlumni(
            @Param("department") String department,
            @Param("batch") String batch,
            @Param("company") String company,
            @Param("keyword") String keyword
    );
}
