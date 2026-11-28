package com.kuet.kuet_connect.service;

import com.kuet.kuet_connect.dto.AlumniRegisterDto;
import com.kuet.kuet_connect.dto.StudentRegisterDto;
import com.kuet.kuet_connect.entity.User;

import java.util.Optional;

public interface UserService {
    User registerStudent(StudentRegisterDto dto);
    User registerAlumni(AlumniRegisterDto dto);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByStudentId(String studentId);
}
