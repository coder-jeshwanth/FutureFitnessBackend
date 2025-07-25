package com.FutureFitness.repository;

import com.FutureFitness.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // Basic CRUD operations are inherited from JpaRepository

    // Method to find member by email (useful for checking duplicates)
    Optional<Member> findByEmail(String email);
}
