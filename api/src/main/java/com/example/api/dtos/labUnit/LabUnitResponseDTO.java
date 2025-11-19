package com.example.api.dtos.labUnit;

import lombok.Data;

@Data
public class LabUnitResponseDTO {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
}

