package com.modify.notificationService.service;

import org.springframework.stereotype.Service;


public interface EmailService {
    public void sendEmail(String toEmail, String name, String  content);

}
