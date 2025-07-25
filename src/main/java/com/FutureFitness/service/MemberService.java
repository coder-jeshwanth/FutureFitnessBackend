package com.FutureFitness.service;

import com.FutureFitness.dto.request.MemberRequestDTO;
import com.FutureFitness.dto.response.MemberResponseDTO;

public interface MemberService {

    /**
     * Add a new member to the system
     *
     * @param memberRequestDTO containing member details
     * @return MemberResponseDTO with status information
     */
    MemberResponseDTO addMember(MemberRequestDTO memberRequestDTO);
}
