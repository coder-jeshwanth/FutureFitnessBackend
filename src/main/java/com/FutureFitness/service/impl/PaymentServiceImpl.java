package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.PaymentRequestDTO;
import com.FutureFitness.dto.response.PaymentResponseDTO;
import com.FutureFitness.entity.Payment;
import com.FutureFitness.entity.Subscription;
import com.FutureFitness.entity.User;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.PaymentRepository;
import com.FutureFitness.repository.SubscriptionRepository;
import com.FutureFitness.repository.UserRepository;
import com.FutureFitness.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                             UserRepository userRepository,
                             SubscriptionRepository subscriptionRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public PaymentResponseDTO addPayment(PaymentRequestDTO paymentRequestDTO) {
        // Validate that at least one of subscriptionId or trainerId is provided
        if (paymentRequestDTO.getSubscriptionId() == null && paymentRequestDTO.getTrainerId() == null) {
            throw new IllegalArgumentException("Either subscriptionId or trainerId must be provided");
        }

        // Fetch User by userId (mandatory)
        User user = userRepository.findById(paymentRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        paymentRequestDTO.getUserId())
                );

        // Fetch Subscription by subscriptionId (if provided)
        Subscription subscription = null;
        if (paymentRequestDTO.getSubscriptionId() != null) {
            subscription = subscriptionRepository.findById(paymentRequestDTO.getSubscriptionId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Subscription",
                            "id",
                            paymentRequestDTO.getSubscriptionId())
                    );
        }

        // Fetch User (trainer) by trainerId (if provided)
        User trainer = null;
        if (paymentRequestDTO.getTrainerId() != null) {
            trainer = userRepository.findById(paymentRequestDTO.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Trainer (User)",
                            "id",
                            paymentRequestDTO.getTrainerId())
                    );
        }

        // Map the request DTO to Payment entity
        Payment payment = new Payment();
        payment.setPaymentMode(paymentRequestDTO.getPaymentMode());
        payment.setPaymentDate(paymentRequestDTO.getPaymentDate());
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setUser(user);
        payment.setSubscription(subscription);
        payment.setTrainer(trainer);

        // Save the payment
        paymentRepository.save(payment);

        // Return success response
        return new PaymentResponseDTO(
                "Payment recorded successfully",
                HttpStatus.CREATED.value()
        );
    }
}
