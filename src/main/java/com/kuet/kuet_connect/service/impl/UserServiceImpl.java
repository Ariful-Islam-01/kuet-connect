package com.kuet.kuet_connect.service.impl;

import com.kuet.kuet_connect.dto.AlumniRegisterDto;
import com.kuet.kuet_connect.dto.StudentRegisterDto;
import com.kuet.kuet_connect.entity.AlumniProfile;
import com.kuet.kuet_connect.entity.Role;
import com.kuet.kuet_connect.entity.User;
import com.kuet.kuet_connect.entity.enums.PrivacyLevel;
import com.kuet.kuet_connect.entity.enums.RoleType;
import com.kuet.kuet_connect.entity.enums.UserStatus;
import com.kuet.kuet_connect.repository.RoleRepository;
import com.kuet.kuet_connect.repository.UserRepository;
import com.kuet.kuet_connect.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerStudent(StudentRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("An account with email " + dto.getEmail() + " already exists.");
        }
        if (userRepository.existsByStudentId(dto.getStudentId())) {
            throw new IllegalArgumentException("Student ID " + dto.getStudentId() + " is already registered.");
        }

        Role studentRole = roleRepository.findByName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new IllegalStateException("ROLE_STUDENT not initialized"));

        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStudentId(dto.getStudentId().trim());
        user.setDepartment(dto.getDepartment().trim().toUpperCase());
        user.setStatus(UserStatus.ACTIVE);
        user.setEnabled(true);
        user.addRole(studentRole);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerAlumni(AlumniRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("An account with email " + dto.getEmail() + " already exists.");
        }
        if (userRepository.existsByStudentId(dto.getStudentId())) {
            throw new IllegalArgumentException("Student ID " + dto.getStudentId() + " is already registered.");
        }

        Role alumniRole = roleRepository.findByName(RoleType.ROLE_ALUMNI)
                .orElseThrow(() -> new IllegalStateException("ROLE_ALUMNI not initialized"));

        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStudentId(dto.getStudentId().trim());
        user.setDepartment(dto.getDepartment().trim().toUpperCase());
        user.setStatus(UserStatus.ACTIVE);
        user.setEnabled(true);
        user.addRole(alumniRole);

        AlumniProfile profile = new AlumniProfile();
        profile.setUser(user);
        profile.setPassingYear(dto.getPassingYear());
        profile.setBatch(dto.getBatch().trim());
        profile.setCurrentDesignation(dto.getCurrentDesignation() != null ? dto.getCurrentDesignation().trim() : null);
        profile.setCurrentCompany(dto.getCurrentCompany() != null ? dto.getCurrentCompany().trim() : null);
        profile.setPrivacyLevel(PrivacyLevel.PUBLIC);
        profile.setVerified(false);

        user.setAlumniProfile(profile);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentId(String studentId) {
        return userRepository.existsByStudentId(studentId);
    }
}
