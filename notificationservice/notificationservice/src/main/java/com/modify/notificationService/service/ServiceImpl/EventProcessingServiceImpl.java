package com.modify.notificationService.service.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modify.notificationService.events.EligibilityEvent;
import com.modify.notificationService.events.EmailEvent;
import com.modify.notificationService.events.OrderOnboardEvent;
import com.modify.notificationService.events.UserOnboardedEvent;
import com.modify.notificationService.service.EmailService;
import com.modify.notificationService.service.EventListenerService;

import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventProcessingServiceImpl implements EventListenerService {

    private  final EmailService emailService;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(EventProcessingServiceImpl.class);

    public EventProcessingServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }


    @Override
    @KafkaListener(topics = "user-topic", groupId = "user-notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void ProcessOnboardEvent(UserOnboardedEvent onboardedEvent) {

        System.out.println("Received UserOnboardedEvent: " + onboardedEvent);
        String toEmail = onboardedEvent.getEmail();
        String name = onboardedEvent.getName();
        Integer verificationCode = onboardedEvent.getVeridicationCode();
        String content = "Dear " + name + ",\n\n" +
                "Welcome to our platform! Your verification code is: " + verificationCode + "\n\n" +
                "Best regards,\n" +
                "The Team";

        String subject = "Welcome to Our Platform";
        emailService.sendEmail(toEmail,subject, content);

        logger.info("Processed UserOnboardedEvent for email: {}",toEmail);


    }


    @KafkaListener(topics = "order-topic", groupId = "order-notification-group",containerFactory = "kafkaListenerContainerFactory")
    public void ProcessOrderEvent(EmailEvent event) {
        // Placeholder for future order event processing logic

        switch (event.getEventType()) {
            case "ORDER_CREATED"-> {
                logger.info("Received Order Event: {}", event.getEventType());

                ObjectMapper objectMapper = new ObjectMapper();
                OrderOnboardEvent orderEvent = objectMapper.convertValue(event.getPayload(), OrderOnboardEvent.class);
                String toEmail = orderEvent.getCreatedBy();
                String content = "Dear User,\n\n" +
                        "Your order with ID: " + orderEvent.getOrderId() + " has been successfully created.\n\n" +
                        "Best regards,\n" +
                        "MODIFY Team";
                String subject = "Order Confirmation - Order ID: " + orderEvent.getOrderId();
                emailService.sendEmail(toEmail, subject, content);
                logger.info("Processed OrderOnboardEvent for email: {}", toEmail);
            }

            case "ELIGIBILITY_COMPLETED" -> {
                logger.info("Received Eligibility Completed Event: {}", event.getEventType());

                ObjectMapper objectMapper = new ObjectMapper();
                EligibilityEvent eligibilityEvent = objectMapper.convertValue(event.getPayload(), EligibilityEvent.class);
                String toEmail = eligibilityEvent.getCreatedBy();
                String subject = String.format("Eligibility Status Completed - Order ID: %s", eligibilityEvent.getOrderId());
                String content = "Dear User,\n\n" +
                        "The eligibility is completed for your order with ID: " + eligibilityEvent.getOrderId() + " is: " + eligibilityEvent.getReason() + ".\n\n" +
                        "Best regards,\n" +
                        "MODIFY Team";

                emailService.sendEmail(toEmail,subject, content);
                logger.info("Processed EligibilityEvent for email: {}", toEmail);
            }

            default -> logger.warn("Unknown event type received: {}", event.getEventType());
        }

    }


}
