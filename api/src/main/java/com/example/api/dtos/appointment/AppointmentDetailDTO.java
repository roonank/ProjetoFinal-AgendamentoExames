package com.example.api.dtos.appointment;

import com.example.api.models.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentDetailDTO {

    private Long id;
    private Long userId;
    private String userName;

    private Long labUnitId;
    private String labUnitName;
    private String labUnitAddress;

    private LocalDateTime scheduledAt;
    private Integer totalDurationDays;

    private AppointmentStatus status;
    private String notes;
    private String cancelReason;

    private List<AppointmentExamResponseDTO> exams;
}

