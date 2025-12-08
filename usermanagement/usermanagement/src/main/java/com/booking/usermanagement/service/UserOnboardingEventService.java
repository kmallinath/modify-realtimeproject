package com.booking.usermanagement.service;

import com.booking.usermanagement.event.UserOnboardedEvent;

public interface UserOnboardingEventService {

    void publishUserOnboardedEvent(UserOnboardedEvent userOnboardedEvent);
}
