package com.modify.notificationService.service;

import com.modify.notificationService.events.UserOnboardedEvent;


public interface EventListenerService {

        public void ProcessOnboardEvent(UserOnboardedEvent onboardedEvent);

}
