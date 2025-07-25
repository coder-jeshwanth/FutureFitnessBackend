package com.FutureFitness.controller;

import com.FutureFitness.dto.request.UserRequestDTO;
import com.FutureFitness.dto.response.UserResponseDTO;
import com.FutureFitness.enums.RoleName;
import com.FutureFitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * API endpoint to add a new user
     *
     * @param userRequestDTO containing user details
     * @return ResponseEntity with UserResponseDTO containing status information
     */
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO response = userService.addUser(userRequestDTO);

        // Return appropriate HTTP status based on the response status code
        if (response.getStatusCode() == HttpStatus.CREATED.value()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    /**
     * API endpoint to get users by role
     *
     * @param role the role to filter by
     * @return ResponseEntity with a list of users with the specified role
     */
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role) {
        try {
            RoleName roleName = RoleName.valueOf(role.toUpperCase());
            List<UserResponseDTO> users = userService.getUsersByRole(roleName);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            // Invalid role name provided
            return ResponseEntity.badRequest().build();
        }
    }
}
