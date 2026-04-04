package edu.icet.ecom.service.Impl;

import edu.icet.ecom.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        // Run the email sending asynchronously so it doesn't block the UI
        CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);
                emailSender.send(message);
                log.info("Email sent successfully to {}", to);
            } catch (Exception e) {
                // Catch any exception (like timeout due to Render blocking SMTP)
                // This prevents a 500 Internal Server Error from ever reaching the user
                log.error("Failed to send email to {}. Error: {}", to, e.getMessage());
            }
        });
    }
}