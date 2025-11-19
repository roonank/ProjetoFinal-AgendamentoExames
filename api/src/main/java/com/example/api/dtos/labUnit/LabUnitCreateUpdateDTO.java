package com.example.api.dtos.labUnit;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabUnitCreateUpdateDTO {

    @NotBlank
    private String name;

    private String address;

    private String phone;

    private String openingHours;
}

