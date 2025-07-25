package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.SubscriptionPlanRequestDTO;
import com.FutureFitness.dto.response.SubscriptionPlanResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.entity.Staff;
import com.FutureFitness.entity.SubscriptionPlan;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.repository.StaffRepository;
import com.FutureFitness.repository.SubscriptionPlanRepository;
import com.FutureFitness.service.SubscriptionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final BranchRepository branchRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public SubscriptionPlanServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository,
                                      BranchRepository branchRepository,
                                      StaffRepository staffRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.branchRepository = branchRepository;
        this.staffRepository = staffRepository;
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

        // Fetch Staff by createdByStaffId
        Staff createdByStaff = staffRepository.findById(subscriptionPlanRequestDTO.getCreatedByStaffId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Staff",
                        "id",
                        subscriptionPlanRequestDTO.getCreatedByStaffId())
                );

        // Get the staff's role name
        String staffRoleName = createdByStaff.getRole().getRoleName().name();

        // Map the request DTO to SubscriptionPlan entity
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setTitle(subscriptionPlanRequestDTO.getTitle());
        subscriptionPlan.setType(subscriptionPlanRequestDTO.getType());
        subscriptionPlan.setPrice(subscriptionPlanRequestDTO.getPrice());
        subscriptionPlan.setDuration(subscriptionPlanRequestDTO.getDuration());
        subscriptionPlan.setBranch(branch);
        subscriptionPlan.setCreatedBy(createdByStaff); // Set the staff who created the plan
        subscriptionPlan.setCreatedByRole(staffRoleName); // Store the staff's role name
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
