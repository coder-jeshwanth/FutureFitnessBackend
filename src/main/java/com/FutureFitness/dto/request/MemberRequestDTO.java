package com.FutureFitness.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {

    private String name;
    private String email;
    private String phone;
    private String password;
    private Long branchId;
}
