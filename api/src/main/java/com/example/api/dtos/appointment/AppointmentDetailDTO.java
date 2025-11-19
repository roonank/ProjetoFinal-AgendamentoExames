package com.example.api.dtos.appointment;

import com.example.api.models.enums.AppoitmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentDetailDTO {

    private Long id;
    private Long userId;
    private String userName;

    private Long clinicId;
    private String clinicName;
    private String clinicAddress;

    private LocalDateTime scheduledAt;
    private Integer totalDurationMinutes;

    private AppoitmentStatus status;
    private String notes;
    private String cancelReason;

    private List<AppointmentExamResponseDTO> exams;
}

