package com.FutureFitness.dto.request;

import com.FutureFitness.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    private PaymentMode paymentMode;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private Long userId; // Changed from memberId to userId
    private Long subscriptionId; // nullable
    private Long trainerId; // Still needed to identify the trainer user
}
