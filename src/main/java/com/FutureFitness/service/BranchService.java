package com.FutureFitness.service;

import com.FutureFitness.dto.request.BranchRequestDTO;
import com.FutureFitness.dto.response.BranchResponseDTO;

public interface BranchService {

    /**
     * Add a new branch to the system
     *
     * @param branchRequestDTO containing branch details
     * @return BranchResponseDTO with status information
     */
    BranchResponseDTO addBranch(BranchRequestDTO branchRequestDTO);
}
