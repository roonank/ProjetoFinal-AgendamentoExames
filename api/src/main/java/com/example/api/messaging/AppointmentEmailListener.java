package com.example.api.messaging;

import com.example.api.records.AppointmentCreatedEvent;
import com.example.api.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentEmailListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void onAppointmentCreated(AppointmentCreatedEvent event) {

        String examsSummary = emailService.joinExamNames(event.examNames());
        String dateTime = emailService.formatDateTime(event.scheduledAt());

        emailService.sendAppointmentCreatedEmail(
                event.userEmail(),
                event.userName(),
                event.labUnitName(),
                event.labUnitAddress(),
                dateTime,
                examsSummary
        );
    }
}
