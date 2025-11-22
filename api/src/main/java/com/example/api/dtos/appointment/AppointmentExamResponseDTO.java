package com.example.api.dtos.appointment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppointmentExamResponseDTO {

    private Long id;
    private Long examId;
    private String examCode;
    private String examName;
    private Integer deliverInDays;
    private String note;
    private BigDecimal price;
    private String status;
}

