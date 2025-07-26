package com.FutureFitness.repository;

import com.FutureFitness.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Basic CRUD operations are inherited from JpaRepository

    /**
     * Find active subscriptions for a user (where end date is after or equal to the given date)
     *
     * @param userId the ID of the user
     * @param date   the date to check against (typically current date)
     * @return a list of active subscriptions for the user
     */
    List<Subscription> findByUserIdAndEndDateGreaterThanEqual(Long userId, LocalDate date);
}
