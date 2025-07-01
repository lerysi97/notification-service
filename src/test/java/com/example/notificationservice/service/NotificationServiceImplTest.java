package com.example.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void sendEmail() {
        notificationService.sendEmail("test@test.ru","subject", "message");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void UserCreated() {
        notificationService.UserCreated("new@test.ru");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void UserDeleted() {
        notificationService.UserDeleted("deleted@test.ru");
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}