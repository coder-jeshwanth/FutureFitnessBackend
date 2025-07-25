package com.FutureFitness.service;

import com.FutureFitness.dto.request.PaymentRequestDTO;
import com.FutureFitness.dto.response.PaymentResponseDTO;

public interface PaymentService {

    /**
     * Add a new payment to the system
     *
     * @param paymentRequestDTO containing payment details
     * @return PaymentResponseDTO with status information
     */
    PaymentResponseDTO addPayment(PaymentRequestDTO paymentRequestDTO);
}
