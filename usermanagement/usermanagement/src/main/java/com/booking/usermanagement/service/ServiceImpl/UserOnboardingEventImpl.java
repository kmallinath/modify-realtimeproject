package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.UserOnboardedEvent;
import com.booking.usermanagement.service.UserOnboardingEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserOnboardingEventImpl implements UserOnboardingEventService {

    private static final String TOPIC = "user-topic";

    @Autowired
    private  KafkaTemplate<String,Object>kafkaTemplate;

    @Override
    public void publishUserOnboardedEvent(UserOnboardedEvent userOnboardedEvent) {

        kafkaTemplate.send(TOPIC,userOnboardedEvent.getEmail(),userOnboardedEvent);
        System.out.println("📤 UserOnboardedEvent sent: " + userOnboardedEvent);

    }
}
