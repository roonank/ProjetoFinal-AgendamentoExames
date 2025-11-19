package com.example.api.dtos.appointment;

import com.example.api.models.enums.AppoitmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentSummaryDTO {

    private Long id;
    private LocalDateTime scheduledAt;
    private AppoitmentStatus status;
    private String clinicName;
    private List<String> examNames;
}

