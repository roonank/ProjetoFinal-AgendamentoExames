package com.example.api.controllers;

import com.example.api.dtos.user.UserRegisterDTO;
import com.example.api.dtos.user.UserResponseDTO;
import com.example.api.models.User;
import com.example.api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRegisterDTO dto){
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
        User user = userService
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        return ResponseEntity.ok(userService.toResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll(){
        List<UserResponseDTO> users = userService.findAll().stream()
                .map(userService::toResponseDTO)
                .toList();
        return ResponseEntity.ok(users);
    }


}
