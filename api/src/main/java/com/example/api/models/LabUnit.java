package com.example.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "LabUnits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    private String address;
    private String phone;
    private String openingHours;
}
