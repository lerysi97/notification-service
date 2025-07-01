package com.example.notificationservice.service;

public interface NotificationService {
    void sendEmail(String to, String subject, String text);
    void UserCreated(String email);
    void UserDeleted(String email);
}
