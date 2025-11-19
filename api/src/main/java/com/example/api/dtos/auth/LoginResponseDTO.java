package com.example.api.dtos.auth;

import com.example.api.dtos.user.UserResponseDTO;
import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private UserResponseDTO user;
}

