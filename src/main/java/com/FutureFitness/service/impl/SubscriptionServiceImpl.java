package com.FutureFitness.service.impl;

import com.FutureFitness.entity.Payment;
import com.FutureFitness.entity.Subscription;
import com.FutureFitness.entity.SubscriptionPlan;
import com.FutureFitness.entity.User;
import com.FutureFitness.repository.PaymentRepository;
import com.FutureFitness.repository.SubscriptionRepository;
import com.FutureFitness.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, PaymentRepository paymentRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Subscription createSubscription(User user, SubscriptionPlan plan, Long paymentId, LocalDate startDate, LocalDate endDate) {
        // Find payment by ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        // Create a new subscription
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setPayment(payment);

        // Save and return the subscription
        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean hasActiveSubscription(Long userId) {
        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Find subscriptions for the user that are currently active
        List<Subscription> activeSubscriptions = subscriptionRepository.findByUserIdAndEndDateGreaterThanEqual(userId, currentDate);

        // Return true if any active subscriptions exist
        return !activeSubscriptions.isEmpty();
    }
}
