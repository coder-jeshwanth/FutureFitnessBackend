package com.FutureFitness.dto.request;

import com.FutureFitness.enums.Gender;
import com.FutureFitness.enums.PaymentMode;
import com.FutureFitness.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequestDTO {
    // User details
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String address;
    private RoleName role;

    // Branch and plan details
    private Long branchId;
    private Long planId;

    // Payment details
    private PaymentMode paymentMode; // CASH, UPI, CARD
}
