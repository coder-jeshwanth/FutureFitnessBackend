package com.FutureFitness.service;

import com.FutureFitness.entity.Subscription;
import com.FutureFitness.entity.SubscriptionPlan;
import com.FutureFitness.entity.User;

import java.time.LocalDate;

public interface SubscriptionService {

    /**
     * Create a new subscription for a user with the given plan and payment
     *
     * @param user the user subscribing to the plan
     * @param plan the subscription plan
     * @param paymentId the ID of the payment made for this subscription
     * @param startDate the start date of the subscription
     * @param endDate the end date of the subscription
     * @return the created subscription
     */
    Subscription createSubscription(User user, SubscriptionPlan plan, Long paymentId, LocalDate startDate, LocalDate endDate);

    /**
     * Check if a user has an active subscription
     *
     * @param userId the ID of the user
     * @return true if the user has an active subscription, false otherwise
     */
    boolean hasActiveSubscription(Long userId);
}
