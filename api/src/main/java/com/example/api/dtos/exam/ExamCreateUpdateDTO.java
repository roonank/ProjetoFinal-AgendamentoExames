package com.example.api.dtos.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamCreateUpdateDTO {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    @PositiveOrZero
    private Integer deliverTimeInDays;

    private String instructions;

    @PositiveOrZero
    private BigDecimal price;
}

