package com.example.api.messaging;

import com.example.api.records.UserCreatedEvent;
import com.example.api.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEmailListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${app.rabbitmq.user-queue}")
    public void onUserCreated(UserCreatedEvent event) {
        emailService.sendWelcomeEmail(
                event.email(),
                event.fullName()
        );
    }
}
