package com.example.api.records;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record AppointmentCreatedEvent(
        Long appointmentId,
        Long userId,
        Long labUnitId,
        LocalDateTime scheduledAt,
        String userEmail,
        String userName,
        String labUnitName,
        String labUnitAddress,
        List<String> examNames
) implements Serializable { }
