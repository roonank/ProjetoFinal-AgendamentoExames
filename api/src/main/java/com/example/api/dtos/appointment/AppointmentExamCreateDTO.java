package com.example.api.dtos.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentExamCreateDTO {

    @NotNull
    private Long examId;

    private String note;
}

