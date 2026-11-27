package com.kuet.kuet_connect.repository;

import com.kuet.kuet_connect.entity.Role;
import com.kuet.kuet_connect.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
