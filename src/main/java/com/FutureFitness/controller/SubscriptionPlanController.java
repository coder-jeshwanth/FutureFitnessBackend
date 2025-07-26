package com.FutureFitness.controller;

import com.FutureFitness.dto.request.SubscriptionPlanRequestDTO;
import com.FutureFitness.dto.response.SubscriptionPlanResponseDTO;
import com.FutureFitness.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @Autowired
    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    /**
     * API endpoint to add a new subscription plan
     * Only ADMIN and MANAGER roles are authorized to create subscription plans
     *
     * @param subscriptionPlanRequestDTO containing subscription plan details
     * @return ResponseEntity with SubscriptionPlanResponseDTO containing status information
     */
    @PostMapping("/subscription-plans")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<SubscriptionPlanResponseDTO> addSubscriptionPlan(@RequestBody SubscriptionPlanRequestDTO subscriptionPlanRequestDTO) {
        SubscriptionPlanResponseDTO response = subscriptionPlanService.addSubscriptionPlan(subscriptionPlanRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
