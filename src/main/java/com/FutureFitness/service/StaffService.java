package com.FutureFitness.service;

import com.FutureFitness.dto.request.StaffRequestDTO;
import com.FutureFitness.dto.response.StaffResponseDTO;

public interface StaffService {

    /**
     * Add a new staff member to the system
     *
     * @param staffRequestDTO containing staff details
     * @return StaffResponseDTO with status information
     */
    StaffResponseDTO addStaff(StaffRequestDTO staffRequestDTO);
}
