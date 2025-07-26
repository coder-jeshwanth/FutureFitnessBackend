package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.UserRegistrationRequestDTO;
import com.FutureFitness.dto.request.UserRequestDTO;
import com.FutureFitness.dto.response.UserResponseDTO;
import com.FutureFitness.entity.Branch;
import com.FutureFitness.entity.Payment;
import com.FutureFitness.entity.Subscription;
import com.FutureFitness.entity.SubscriptionPlan;
import com.FutureFitness.entity.User;
import com.FutureFitness.enums.RoleName;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.BranchRepository;
import com.FutureFitness.repository.PaymentRepository;
import com.FutureFitness.repository.SubscriptionPlanRepository;
import com.FutureFitness.repository.SubscriptionRepository;
import com.FutureFitness.repository.UserRepository;
import com.FutureFitness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                          BranchRepository branchRepository,
                          PasswordEncoder passwordEncoder,
                          SubscriptionPlanRepository subscriptionPlanRepository,
                          PaymentRepository paymentRepository,
                          SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
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

        // Generate password from phone number and birth year
        String generatedPassword = generatePasswordFromUserData(userRequestDTO.getPhone(), userRequestDTO.getDateOfBirth() != null ?
                userRequestDTO.getDateOfBirth().getYear() : 0);

        // Map the request DTO to User entity
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone(userRequestDTO.getPhone());
        // Set the generated password (encrypted)
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setRole(userRequestDTO.getRole());
        user.setBranch(branch);
        user.setGender(userRequestDTO.getGender());
        user.setDateOfBirth(userRequestDTO.getDateOfBirth());
        user.setAddress(userRequestDTO.getAddress());
        user.setCreatedAt(LocalDateTime.now());

        // Save the user
        User savedUser = userRepository.save(user);

        // Return success response with the generated password
        UserResponseDTO response = new UserResponseDTO(
                "User created successfully with generated password: " + generatedPassword,
                HttpStatus.CREATED.value()
        );

        return response;
    }

    /**
     * Generates a password from the first 4 digits of phone number and birth year
     * @param phoneNumber the user's phone number
     * @param birthYear the user's year of birth
     * @return the generated password
     */
    private String generatePasswordFromUserData(String phoneNumber, int birthYear) {
        // Extract first 4 digits of phone number
        String phoneDigits = "";
        if (phoneNumber != null && phoneNumber.length() >= 4) {
            // Remove any non-digit characters (like +, -, spaces)
            phoneDigits = phoneNumber.replaceAll("\\D", "").substring(0, 4);
        } else {
            // Fallback if phone number is too short or null
            phoneDigits = "0000";
        }

        // Use birth year or default if not available
        String yearPart = birthYear > 0 ? String.valueOf(birthYear) : "0000";

        // Combine phone digits and birth year
        return phoneDigits + yearPart;
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

    @Override
    public int countMembersAddedInCurrentMonth() {
        // Get the first day of the current month
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = now.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0).minusNanos(1);

        // Find users with MEMBER role created in the current month
        List<User> membersAddedThisMonth = userRepository.findByRoleAndCreatedAtBetween(RoleName.MEMBER, startOfMonth, endOfMonth);

        return membersAddedThisMonth.size();
    }

    @Override
    @Transactional
    public Map<String, Object> registerUserWithSubscription(UserRegistrationRequestDTO registrationRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Check if user with the email already exists
            Optional<User> existingUser = userRepository.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                response.put("success", false);
                response.put("message", "User with this email already exists");
                response.put("statusCode", HttpStatus.CONFLICT.value());
                return response;
            }

            // 2. Fetch Branch by branchId
            Branch branch = branchRepository.findById(registrationRequest.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Branch",
                            "id",
                            registrationRequest.getBranchId())
                    );

            // 3. Fetch SubscriptionPlan by planId
            SubscriptionPlan plan = subscriptionPlanRepository.findById(registrationRequest.getPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "SubscriptionPlan",
                            "id",
                            registrationRequest.getPlanId())
                    );

            // 4. Generate password from phone number and birth year
            String generatedPassword = generatePasswordFromUserData(
                    registrationRequest.getPhone(),
                    registrationRequest.getDateOfBirth() != null ?
                            registrationRequest.getDateOfBirth().getYear() : 0
            );

            // 5. Create and save the User entity
            User user = new User();
            user.setName(registrationRequest.getName());
            user.setEmail(registrationRequest.getEmail());
            user.setPhone(registrationRequest.getPhone());
            user.setPassword(passwordEncoder.encode(generatedPassword));
            user.setRole(registrationRequest.getRole());
            user.setBranch(branch);
            user.setGender(registrationRequest.getGender());
            user.setDateOfBirth(registrationRequest.getDateOfBirth());
            user.setAddress(registrationRequest.getAddress());
            user.setCreatedAt(LocalDateTime.now());

            User savedUser = userRepository.save(user);

            // 6. Create and save Payment entity
            Payment payment = new Payment();
            payment.setPaymentMode(registrationRequest.getPaymentMode());
            payment.setPaymentDate(LocalDateTime.now()); // Current date and time
            payment.setAmount(plan.getPrice()); // Amount from the subscription plan
            payment.setUser(savedUser);
            payment.setPlan(plan);

            Payment savedPayment = paymentRepository.save(payment);

            // 7. Create and save Subscription entity
            LocalDate startDate = LocalDate.now(); // Start from today
            LocalDate endDate = startDate.plusDays(plan.getDuration()); // End date based on plan duration

            Subscription subscription = new Subscription();
            subscription.setUser(savedUser);
            subscription.setPlan(plan);
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            subscription.setPayment(savedPayment);

            Subscription savedSubscription = subscriptionRepository.save(subscription);

            // 8. Prepare and return response
            response.put("success", true);
            response.put("message", "User registered successfully with subscription");
            response.put("statusCode", HttpStatus.CREATED.value());
            response.put("userId", savedUser.getId());
            response.put("generatedPassword", generatedPassword);
            response.put("paymentId", savedPayment.getId());
            response.put("subscriptionId", savedSubscription.getId());
            response.put("subscriptionStartDate", startDate);
            response.put("subscriptionEndDate", endDate);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error registering user: " + e.getMessage());
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response;
    }
}
