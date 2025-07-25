package com.FutureFitness.repository;

import com.FutureFitness.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    // Basic CRUD operations are inherited from JpaRepository
}
