package com.FutureFitness.repository;

import com.FutureFitness.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Basic CRUD operations are inherited from JpaRepository
}
