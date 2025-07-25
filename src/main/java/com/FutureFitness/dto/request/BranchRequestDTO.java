package com.FutureFitness.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequestDTO {

    private String name;
    private String location;
    private String phoneNumber;
}
