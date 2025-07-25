package com.FutureFitness.repository;

import com.FutureFitness.entity.Role;
import com.FutureFitness.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Basic CRUD operations are inherited from JpaRepository

    // Method to find role by roleName enum
    Optional<Role> findByRoleName(RoleName roleName);
}
