package com.example.api.dtos.exam;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamResponseDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer deliverTimeInDays;
    private String instructions;
    private BigDecimal price;
}

