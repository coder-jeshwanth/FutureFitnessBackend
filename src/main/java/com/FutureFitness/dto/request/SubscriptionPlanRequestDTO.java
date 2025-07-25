package com.FutureFitness.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanRequestDTO {

    private String title;
    private String type;
    private BigDecimal price;
    private Integer duration;
    private Long branchId;
    private Long createdByStaffId; // Added field to track which staff created the plan
}
