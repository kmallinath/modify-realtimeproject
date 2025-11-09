package com.modify.notificationService.service.ServiceImpl;

import com.modify.notificationService.dtos.UserOnboardedEvent;
import com.modify.notificationService.service.EmailService;
import com.modify.notificationService.service.UserOnboardingListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserOnboadingLiistenerServiceImpl implements UserOnboardingListenerService {

    @Autowired
    private  EmailService emailService;
    @Override

    @KafkaListener(topics = "user-topic", groupId = "user-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenUserOnboardingEvent(UserOnboardedEvent event) {

        emailService.sendVerificationEmail(event.getEmail(), event.getName(), event.getVeridicationCode());

    }
}
