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
    private Long createdByUserId; // Changed from createdByStaffId to createdByUserId
}
