package com.FutureFitness.repository;

import com.FutureFitness.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    // Basic CRUD operations are inherited from JpaRepository
}
