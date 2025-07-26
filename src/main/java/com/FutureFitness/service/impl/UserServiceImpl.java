package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.UserRequestDTO;
import com.FutureFitness.dto.response.UserResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.entity.User;
import com.FutureFitness.enums.RoleName;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.repository.UserRepository;
import com.FutureFitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BranchRepository branchRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        // Check if user with the same email already exists
        Optional<User> existingUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (existingUser.isPresent()) {
            return new UserResponseDTO(
                    "User with this email already exists",
                    HttpStatus.CONFLICT.value()
            );
        }

        // Fetch Branch by branchId
        Branch branch = branchRepository.findById(userRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Branch",
                        "id",
                        userRequestDTO.getBranchId())
                );

        // Map the request DTO to User entity
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone(userRequestDTO.getPhone());
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(userRequestDTO.getRole());
        user.setBranch(branch);
        user.setCreatedAt(LocalDateTime.now());

        // Save the user
        userRepository.save(user);

        // Return success response
        return new UserResponseDTO(
                "User created successfully",
                HttpStatus.CREATED.value()
        );
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(RoleName role) {
        List<User> users = userRepository.findByRole(role);
        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();

        // In a real implementation, you would map Users to a more detailed response DTO
        // For now, we'll return a simple success message for each user
        for (User user : users) {
            userResponseDTOs.add(new UserResponseDTO(
                    "User found with ID: " + user.getId(),
                    HttpStatus.OK.value()
            ));
        }

        return userResponseDTOs;
    }
}
