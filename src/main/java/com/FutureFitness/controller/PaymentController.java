package com.FutureFitness.controller;

import com.FutureFitness.dto.request.PaymentRequestDTO;
import com.FutureFitness.dto.response.PaymentResponseDTO;
import com.FutureFitness.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * API endpoint to add a new payment
     *
     * @param paymentRequestDTO containing payment details
     * @return ResponseEntity with PaymentResponseDTO containing status information
     */
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponseDTO> addPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentResponseDTO response = paymentService.addPayment(paymentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
