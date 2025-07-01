package com.example.notificationservice.integration;

import com.example.notificationservice.service.NotificationService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmailIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(
            new ServerSetup(0, null, ServerSetup.PROTOCOL_SMTP)
    )
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("user", "admin")
            )
            .withPerMethodLifecycle(false);

    @DynamicPropertySource
    static void Mail(DynamicPropertyRegistry registry) {
        int port = greenMail.getSmtp().getPort();
        System.out.println("GreenMail SMTP port: " + port);
        registry.add("spring.mail.port", () -> port);
        registry.add("spring.mail.host", () -> "localhost");
    }

    @Autowired
    private NotificationService notificationService;

    @Test
    void SendEmail() throws Exception {
        String to = "test@test.ru";
        String subject = "test subject";
        String text = "test message";

        notificationService.sendEmail(to, subject, text);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals(subject, receivedMessages[0].getSubject());
        assertTrue(receivedMessages[0].getContent().toString().contains(text));
        assertEquals(to, receivedMessages[0].getAllRecipients()[0].toString());
    }
}
