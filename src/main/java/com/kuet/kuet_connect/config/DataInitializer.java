package com.kuet.kuet_connect.config;

import com.kuet.kuet_connect.entity.AlumniProfile;
import com.kuet.kuet_connect.entity.Role;
import com.kuet.kuet_connect.entity.User;
import com.kuet.kuet_connect.entity.enums.PrivacyLevel;
import com.kuet.kuet_connect.entity.enums.RoleType;
import com.kuet.kuet_connect.entity.enums.UserStatus;
import com.kuet.kuet_connect.repository.RoleRepository;
import com.kuet.kuet_connect.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Seed Roles if missing
        Role studentRole = roleRepository.findByName(RoleType.ROLE_STUDENT)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_STUDENT)));

        Role alumniRole = roleRepository.findByName(RoleType.ROLE_ALUMNI)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_ALUMNI)));

        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_ADMIN)));

        // 2. Seed Default Admin if missing (Admin accounts cannot be registered publicly)
        if (userRepository.findByEmail("admin@kuet.ac.bd").isEmpty()) {
            User admin = new User();
            admin.setFullName("KUET System Admin");
            admin.setEmail("admin@kuet.ac.bd");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setDepartment("CSE");
            admin.setStatus(UserStatus.ACTIVE);
            admin.setEnabled(true);
            admin.addRole(adminRole);

            userRepository.save(admin);
        }

        // 3. Seed Sample Alumni if no alumni present (for directory testing)
        if (userRepository.findByEmail("alumni.sample@kuet.ac.bd").isEmpty()) {
            User sampleAlumni = new User();
            sampleAlumni.setFullName("Tanvir Ahmed");
            sampleAlumni.setEmail("alumni.sample@kuet.ac.bd");
            sampleAlumni.setPassword(passwordEncoder.encode("alumni123"));
            sampleAlumni.setStudentId("1607001");
            sampleAlumni.setDepartment("CSE");
            sampleAlumni.setStatus(UserStatus.ACTIVE);
            sampleAlumni.setEnabled(true);
            sampleAlumni.addRole(alumniRole);

            AlumniProfile profile = new AlumniProfile();
            profile.setUser(sampleAlumni);
            profile.setBatch("2k16");
            profile.setPassingYear(2021);
            profile.setDegree("B.Sc. in Computer Science and Engineering");
            profile.setCurrentDesignation("Senior Software Engineer");
            profile.setCurrentCompany("Google");
            profile.setIndustry("Technology");
            profile.setLocation("Singapore");
            profile.setBio("Proud KUETian passionate about distributed systems and cloud architecture.");
            profile.setLinkedinUrl("https://linkedin.com");
            profile.setGithubUrl("https://github.com");
            profile.setPrivacyLevel(PrivacyLevel.PUBLIC);
            profile.setVerified(true);

            sampleAlumni.setAlumniProfile(profile);
            userRepository.save(sampleAlumni);
        }
    }
}
