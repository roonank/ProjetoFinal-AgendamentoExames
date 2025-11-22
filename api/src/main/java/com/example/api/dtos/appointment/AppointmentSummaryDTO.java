package com.example.api.dtos.appointment;

import com.example.api.models.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentSummaryDTO {

    private Long id;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
    private String LabUnitName;
    private List<String> examNames;
}

