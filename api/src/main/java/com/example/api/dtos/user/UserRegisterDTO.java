package com.example.api.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank String fullName;
    @NotBlank String email;
    @NotBlank String password;
    @NotBlank String cpf;
    @NotBlank String phone;
    @NotBlank String birthDate;
    String address;
    String gender;
}

