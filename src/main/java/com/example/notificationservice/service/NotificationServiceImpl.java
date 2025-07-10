package com.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender mailSender;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Value("${app.email.from}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String text) {
        circuitBreakerFactory.create("emailService").run(
                () -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(fromEmail);
                    message.setTo(to);
                    message.setSubject(subject);
                    message.setText(text);
                    mailSender.send(message);
                    return null;
                },
                throwable -> {
                    log.error("Ошибка отправки email: {}", throwable.getMessage());
                    return null;
                });
    }

    @Override
    public void UserCreated(String email) {
        String subject = "Успешная регистрация";
        String text = "Здравствуйте! Ваш аккаунт был успешно создан.";
        sendEmail(email, subject, text);
    }

    @Override
    public void UserDeleted(String email) {
        String subject = "Удаление аккаунта";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendEmail(email, subject, text);
    }
}