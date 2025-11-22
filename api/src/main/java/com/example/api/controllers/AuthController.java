package com.example.api.controllers;

import com.example.api.dtos.auth.LoginRequestDTO;
import com.example.api.dtos.auth.LoginResponseDTO;
import com.example.api.dtos.user.UserResponseDTO;
import com.example.api.models.User;
import com.example.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas!"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas!");
        }

        UserResponseDTO userResponse = userService.toResponseDTO(user);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUser(userResponse);
        response.setToken(null);

        return ResponseEntity.ok(response);
    }
}
