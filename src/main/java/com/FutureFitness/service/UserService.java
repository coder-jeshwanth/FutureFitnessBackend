package com.FutureFitness.service;

import com.FutureFitness.dto.request.UserRequestDTO;
import com.FutureFitness.dto.response.UserResponseDTO;
import com.FutureFitness.enums.RoleName;

import java.util.List;

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
}
