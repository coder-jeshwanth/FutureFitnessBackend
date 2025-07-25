package com.FutureFitness.service.impl;

import com.FutureFitness.dto.request.PaymentRequestDTO;
import com.FutureFitness.dto.response.PaymentResponseDTO;
import com.FutureFitness.entity.Member;
import com.FutureFitness.entity.Payment;
import com.FutureFitness.entity.Staff;
import com.FutureFitness.entity.Subscription;
import com.FutureFitness.exception.ResourceNotFoundException;
import com.FutureFitness.repository.MemberRepository;
import com.FutureFitness.repository.PaymentRepository;
import com.FutureFitness.repository.StaffRepository;
import com.FutureFitness.repository.SubscriptionRepository;
import com.FutureFitness.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                             MemberRepository memberRepository,
                             SubscriptionRepository subscriptionRepository,
                             StaffRepository staffRepository) {
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public PaymentResponseDTO addPayment(PaymentRequestDTO paymentRequestDTO) {
        // Validate that at least one of subscriptionId or trainerId is provided
        if (paymentRequestDTO.getSubscriptionId() == null && paymentRequestDTO.getTrainerId() == null) {
            throw new IllegalArgumentException("Either subscriptionId or trainerId must be provided");
        }

        // Fetch Member by memberId (mandatory)
        Member member = memberRepository.findById(paymentRequestDTO.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member",
                        "id",
                        paymentRequestDTO.getMemberId())
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

        // Fetch Staff (trainer) by trainerId (if provided)
        Staff trainer = null;
        if (paymentRequestDTO.getTrainerId() != null) {
            trainer = staffRepository.findById(paymentRequestDTO.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Trainer (Staff)",
                            "id",
                            paymentRequestDTO.getTrainerId())
                    );
        }

        // Map the request DTO to Payment entity
        Payment payment = new Payment();
        payment.setPaymentMode(paymentRequestDTO.getPaymentMode());
        payment.setPaymentDate(paymentRequestDTO.getPaymentDate());
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setMember(member);
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
