package com.FutureFitness.service;

import com.FutureFitness.dto.request.SubscriptionPlanRequestDTO;
import com.FutureFitness.dto.response.SubscriptionPlanResponseDTO;

public interface SubscriptionPlanService {

    /**
     * Add a new subscription plan to the system
     *
     * @param subscriptionPlanRequestDTO containing subscription plan details
     * @return SubscriptionPlanResponseDTO with status information
     */
    SubscriptionPlanResponseDTO addSubscriptionPlan(SubscriptionPlanRequestDTO subscriptionPlanRequestDTO);
}
