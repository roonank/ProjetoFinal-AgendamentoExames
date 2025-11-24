package com.example.api.dtos.user;

import com.example.api.models.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String cpf;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private String gender;
    private UserRole role;
    private boolean active;
}

