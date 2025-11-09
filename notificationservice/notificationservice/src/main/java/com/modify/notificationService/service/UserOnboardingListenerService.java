package com.modify.notificationService.service;

import com.modify.notificationService.dtos.UserOnboardedEvent;
import org.springframework.stereotype.Service;


public interface UserOnboardingListenerService {

    public  void listenUserOnboardingEvent(UserOnboardedEvent message);
}
