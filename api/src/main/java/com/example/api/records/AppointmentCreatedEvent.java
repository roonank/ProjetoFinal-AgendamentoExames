package com.example.api.records;

import java.time.LocalDateTime;

public record AppointmentCreatedEvent(
        Long appointmentId,
        Long userId,
        Long labUnitId,
        LocalDateTime scheduledAt) {}
