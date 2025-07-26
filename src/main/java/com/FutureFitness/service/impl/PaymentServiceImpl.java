package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.PaymentRequestDTO;
import com.FutureFitness.dto.response.PaymentResponseDTO;
import com.FutureFitness.entity.Payment;
import com.FutureFitness.entity.Subscription;
import com.FutureFitness.entity.SubscriptionPlan;
import com.FutureFitness.entity.User;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.PaymentRepository;
import com.FutureFitness.repository.SubscriptionPlanRepository;
import com.FutureFitness.repository.SubscriptionRepository;
import com.FutureFitness.repository.UserRepository;
import com.FutureFitness.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                             UserRepository userRepository,
                             SubscriptionPlanRepository subscriptionPlanRepository,
                             SubscriptionRepository subscriptionRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    @Transactional
    public PaymentResponseDTO addPayment(PaymentRequestDTO paymentRequestDTO) {
        // Validate that at least one of planId or trainerId is provided
        if (paymentRequestDTO.getPlanId() == null && paymentRequestDTO.getTrainerId() == null) {
            throw new IllegalArgumentException("Either planId or trainerId must be provided");
        }

        // Fetch User by userId (mandatory)
        User user = userRepository.findById(paymentRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        "id",
                        paymentRequestDTO.getUserId())
                );

        // Fetch SubscriptionPlan by planId (if provided)
        SubscriptionPlan plan = null;
        if (paymentRequestDTO.getPlanId() != null) {
            plan = subscriptionPlanRepository.findById(paymentRequestDTO.getPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "SubscriptionPlan",
                            "id",
                            paymentRequestDTO.getPlanId())
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
        payment.setPlan(plan);
        payment.setTrainer(trainer);

        // Save the payment
        Payment savedPayment = paymentRepository.save(payment);

        // If this is a payment for a subscription plan, create a subscription
        if (plan != null) {
            // Calculate start and end dates
            LocalDate startDate = paymentRequestDTO.getPaymentDate().toLocalDate(); // Start from payment date
            LocalDate endDate = startDate.plusDays(plan.getDuration()); // Add the duration in days

            // Create subscription entity
            Subscription subscription = new Subscription();
            subscription.setUser(user);
            subscription.setPlan(plan);
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            subscription.setPayment(savedPayment);

            // Save subscription
            subscriptionRepository.save(subscription);
        }

        // Return success response
        return new PaymentResponseDTO(
                "Payment processed successfully and subscription created",
                HttpStatus.CREATED.value()
        );
    }
}
