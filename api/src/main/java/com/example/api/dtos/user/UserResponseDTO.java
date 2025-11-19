package com.example.api.dtos.user;

import com.example.api.models.enums.UserRole;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private UserRole role;
    private boolean active;
}

