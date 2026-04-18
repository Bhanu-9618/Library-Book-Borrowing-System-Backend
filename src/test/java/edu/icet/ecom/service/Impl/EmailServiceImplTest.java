package edu.icet.ecom.service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void sendSimpleMessage_ShouldCallSender() {
        // Arrange
        ReflectionTestUtils.setField(emailService, "fromEmail", "admin@library.com");
        
        // Act
        emailService.sendSimpleMessage("user@test.com", "Subject", "Body");

        // Assert
        // We might need a small wait if it's truly async, but in unit tests with mockito 
        // we can verify the call if we don't use real threads or wait.
        // Since it's runAsync, we'll wait a tiny bit.
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
