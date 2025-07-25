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
    private Long memberId;
    private Long subscriptionId; // nullable
    private Long trainerId; // nullable
}
