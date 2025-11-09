package com.modify.notificationService.service.ServiceImpl;

import com.modify.notificationService.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendVerificationEmail(String toEmail, String name, Integer verificationCode) {
        // Implementation for sending email
        System.out.println("Sending verification email to: " + toEmail);
        System.out.println("Hello " + name + ", your verification code is: " + verificationCode);

        String subject = "User Verification";
        String text = "Hello " + name + ",\n\nYour verification code is: " + verificationCode + "\n\nThank you!";

        sendSimpleEmail(toEmail, subject, text);







    }


    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("musicl@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        System.out.println("Email sent successfully!");
    }
}
