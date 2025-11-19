package com.example.api.models.enums;

import lombok.Getter;

public enum UserRole {
    CLIENT("user"),
    ADMIN("admin");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

}
