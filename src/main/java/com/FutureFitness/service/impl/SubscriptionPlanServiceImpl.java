package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.SubscriptionPlanRequestDTO;
import com.FutureFitness.dto.response.SubscriptionPlanResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.entity.SubscriptionPlan;
import com.FutureFitness.entity.User;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.repository.SubscriptionPlanRepository;
import com.FutureFitness.repository.UserRepository;
import com.FutureFitness.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository,
                                      BranchRepository branchRepository,
                                      UserRepository userRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SubscriptionPlanResponseDTO addSubscriptionPlan(SubscriptionPlanRequestDTO subscriptionPlanRequestDTO) {
        // Fetch Branch by branchId
        Branch branch = branchRepository.findById(subscriptionPlanRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch",
                        "id",
                        subscriptionPlanRequestDTO.getBranchId())
                );

        // Fetch User by createdByUserId
        User createdByUser = userRepository.findById(subscriptionPlanRequestDTO.getCreatedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        subscriptionPlanRequestDTO.getCreatedByUserId())
                );

        // Get the user's role name
        String userRoleName = createdByUser.getRole().name();

        // Map the request DTO to SubscriptionPlan entity
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setTitle(subscriptionPlanRequestDTO.getTitle());
        subscriptionPlan.setType(subscriptionPlanRequestDTO.getType());
        subscriptionPlan.setPrice(subscriptionPlanRequestDTO.getPrice());
        subscriptionPlan.setDuration(subscriptionPlanRequestDTO.getDuration());
        subscriptionPlan.setBranch(branch);
        subscriptionPlan.setCreatedBy(createdByUser); // Set the user who created the plan
        subscriptionPlan.setCreatedByRole(userRoleName); // Store the user's role name
        subscriptionPlan.setUpdatedAt(LocalDateTime.now());

        // Save the subscription plan
        subscriptionPlanRepository.save(subscriptionPlan);

        // Return success response
        return new SubscriptionPlanResponseDTO(
                "Subscription plan created successfully",
                HttpStatus.CREATED.value()
        );
    }
}
