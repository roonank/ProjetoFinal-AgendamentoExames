package com.example.api.models.enums;

public enum UserRole {
    CLIENT("user"),
    ADMIN("admin");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
