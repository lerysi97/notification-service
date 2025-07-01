package com.example.notificationservice.service;

import com.example.common.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka.topics.user-created}")
    public void UserCreated(UserEvent event) {
        try {
            log.info("Processing email for: {}", event.getEmail());
            notificationService.UserCreated(event.getEmail());
        } catch (Exception e) {
            log.error("FATAL: Failed to process email notification", e);
        }
    }

    @KafkaListener(topics = "${kafka.topics.user-deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void UserDeleted(UserEvent event) {
        log.info("Пользователь удалён с email: {}", event.getEmail());
        notificationService.UserDeleted(event.getEmail());
    }
}
