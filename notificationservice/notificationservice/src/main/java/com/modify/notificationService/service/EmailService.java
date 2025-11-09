package com.modify.notificationService.service;

import org.springframework.stereotype.Service;


public interface EmailService {
    public void sendVerificationEmail(String toEmail, String name, Integer verificationCode);
}
