package com.example.api.controllers;

import com.example.api.config.TokenService;
import com.example.api.dtos.auth.LoginRequestDTO;
import com.example.api.dtos.auth.LoginResponseDTO;
import com.example.api.dtos.user.UserResponseDTO;
import com.example.api.models.User;
import com.example.api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas!"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas!");
        }

        String token = tokenService.generateToken(user);

        UserResponseDTO userResponse = userService.toResponseDTO(user);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUser(userResponse);
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
