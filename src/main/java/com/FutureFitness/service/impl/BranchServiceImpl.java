package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.BranchRequestDTO;
import com.FutureFitness.dto.response.BranchResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public BranchResponseDTO addBranch(BranchRequestDTO branchRequestDTO) {
        // Map the request DTO to Branch entity
        Branch branch = new Branch();
        branch.setName(branchRequestDTO.getName());
        branch.setLocation(branchRequestDTO.getLocation());
        branch.setPhoneNumber(branchRequestDTO.getPhoneNumber());
        branch.setUpdatedAt(LocalDateTime.now());

        // Save the branch
        branchRepository.save(branch);

        // Return success response
        return new BranchResponseDTO(
                "Branch created successfully",
                HttpStatus.CREATED.value()
        );
    }
}
