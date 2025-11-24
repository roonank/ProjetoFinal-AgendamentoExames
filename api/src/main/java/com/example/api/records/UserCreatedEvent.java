package com.example.api.records;

import java.io.Serializable;

public record UserCreatedEvent(
        Long userId,
        String fullName,
        String email
) implements Serializable {}