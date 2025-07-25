package com.FutureFitness.repository;

import com.FutureFitness.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    // Basic CRUD operations are inherited from JpaRepository

    // Method to find staff by email
    Optional<Staff> findByEmail(String email);
}
