package com.example.notificationservice.service;

import com.example.common.event.UserEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void UserCreatedEvent() {
        UserEvent event = new UserEvent();
        event.setEmail("test@test.ru");
        kafkaConsumerService.UserCreated(event);
        verify(notificationService).UserCreated("test@test.ru");
    }

    @Test
    void UserDeletedEvent() {
        UserEvent event = new UserEvent();
        event.setEmail("test@test.ru");
        kafkaConsumerService.UserDeleted(event);
        verify(notificationService).UserDeleted("test@test.ru");
    }
}
