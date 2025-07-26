package com.FutureFitness.service;

import com.FutureFitness.dto.request.UserRegistrationRequestDTO;
import com.FutureFitness.dto.request.UserRequestDTO;
import com.FutureFitness.dto.response.UserResponseDTO;
import com.FutureFitness.enums.RoleName;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * Add a new user to the system
     *
     * @param userRequestDTO containing user details
     * @return UserResponseDTO with status information
     */
    UserResponseDTO addUser(UserRequestDTO userRequestDTO);

    /**
     * Get users by role
     *
     * @param role the role to filter by
     * @return List of users with the specified role
     */
    List<UserResponseDTO> getUsersByRole(RoleName role);

    /**
     * Count users with MEMBER role added in the current month
     *
     * @return Number of members added in the current month
     */
    int countMembersAddedInCurrentMonth();

    /**
     * Register a new user with subscription plan and payment
     *
     * @param registrationRequest containing user details, plan info, and payment data
     * @return Map with registration result details
     */
    Map<String, Object> registerUserWithSubscription(UserRegistrationRequestDTO registrationRequest);
}
