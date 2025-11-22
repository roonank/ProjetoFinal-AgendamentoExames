package com.example.api.dtos.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentCreateDTO {

    @NotNull
    private Long LabUnitId;

    @NotNull
    private LocalDateTime scheduledAt;

    private String notes;

    @NotNull
    private List<AppointmentExamCreateDTO> exams;
}

