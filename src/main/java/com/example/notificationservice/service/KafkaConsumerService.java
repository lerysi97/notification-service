package com.example.notificationservice.service;

import com.example.common.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final NotificationService notificationService;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;


    @KafkaListener(topics = "${kafka.topics.user-created}")
    public void UserCreated(UserEvent event) {
        circuitBreakerFactory.create("kafkaEmailService").run(
                () -> {
                    log.info("Отправка уведомления для email: {}", event.getEmail());
                    notificationService.UserCreated(event.getEmail());
                    return null;
                },
                throwable -> {
                    log.error("Ошибка обработки события user-created: {}", throwable.getMessage());
                    return null;
                });
    }

    @KafkaListener(topics = "${kafka.topics.user-deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void UserDeleted(UserEvent event) {
        circuitBreakerFactory.create("kafkaEmailService").run(
                () -> {
                    log.info("Пользователь удалён с email: {}", event.getEmail());
                    notificationService.UserDeleted(event.getEmail());
                    return null;
                },
                throwable -> {
                    log.error("Ошибка обработки события user-deleted: {}", throwable.getMessage());
                    return null;
                });
    }
}
