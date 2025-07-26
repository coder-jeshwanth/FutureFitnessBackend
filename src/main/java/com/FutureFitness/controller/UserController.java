package com.FutureFitness.controller;

import com.FutureFitness.dto.request.UserRegistrationRequestDTO;
import com.FutureFitness.dto.request.UserRequestDTO;
import com.FutureFitness.dto.response.UserResponseDTO;
import com.FutureFitness.enums.RoleName;
import com.FutureFitness.service.PaymentService;
import com.FutureFitness.service.SubscriptionService;
import com.FutureFitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public UserController(UserService userService, PaymentService paymentService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.subscriptionService = subscriptionService;
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

    /**
     * API endpoint to get count of users with MEMBER role
     *
     * @return ResponseEntity with a JSON containing the total count of members
     */
    @GetMapping("/users/members")
    public ResponseEntity<Map<String, Object>> getMembersCount() {
        List<UserResponseDTO> members = userService.getUsersByRole(RoleName.MEMBER);

        Map<String, Object> response = new HashMap<>();
        response.put("Total members", members.size());
        response.put("message", "Members fetched successfully");
        response.put("statusCode", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    /**
     * API endpoint to get count of members added in the current month
     *
     * @return ResponseEntity with a JSON containing the count of members added this month
     */
    @GetMapping("/users/members/this-month")
    public ResponseEntity<Map<String, Object>> getMembersAddedThisMonth() {
        int count = userService.countMembersAddedInCurrentMonth();

        Map<String, Object> response = new HashMap<>();
        response.put("New members this month", count);
        response.put("message", "New members count fetched successfully");
        response.put("statusCode", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    /**
     * Comprehensive API endpoint to register a new user with subscription plan and payment
     *
     * @param registrationRequest containing user details, plan info, and payment data
     * @return ResponseEntity with registration result details
     */
    @PostMapping("/users/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationRequestDTO registrationRequest) {
        Map<String, Object> response = userService.registerUserWithSubscription(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
