package com.FutureFitness.repository;

import com.FutureFitness.entity.User;
import com.FutureFitness.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(RoleName role);

    List<User> findByRoleAndCreatedAtBetween(RoleName role, LocalDateTime startDate, LocalDateTime endDate);
}
