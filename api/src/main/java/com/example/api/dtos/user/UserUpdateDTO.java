package com.example.api.dtos.user;

import lombok.Data;

@Data
public class UserUpdateDTO {

    private String fullName;
    private String phone;
    private Boolean active;
}

