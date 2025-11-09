package com.booking.usermanagement.service;

import com.booking.usermanagement.dtos.UserOnboardedEvent;

public interface UserOnboardingEventService {

    void publishUserOnboardedEvent(UserOnboardedEvent userOnboardedEvent);
}
