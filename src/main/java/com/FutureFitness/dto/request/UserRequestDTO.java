package com.FutureFitness.dto.request;

import com.FutureFitness.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String name;
    private String email;
    private String phone;
    private String password;
    private RoleName role;
    private Long branchId;
}
