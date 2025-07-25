package com.FutureFitness.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequestDTO {

    private String name;
    private String email;
    private String phone;
    private String password;
    private String roleName; // Changed from Long roleId to String roleName
    private Long branchId;
    private BigDecimal salary;
}
