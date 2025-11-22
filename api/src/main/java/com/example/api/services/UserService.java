package com.example.api.services;

import com.example.api.dtos.user.UserRegisterDTO;
import com.example.api.dtos.user.UserResponseDTO;
import com.example.api.models.User;
import com.example.api.models.enums.UserRole;
import com.example.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //CREATE USUÁRIO
    public UserResponseDTO createUser(UserRegisterDTO dto){
        if (userRepository.existByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .role(UserRole.CLIENT)
                .active(true)
                .build();

        user = userRepository.save(user);

        return toResponseDTO(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponseDTO toResponseDTO(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    public Optional<User> findById(Long id) { return userRepository.findById(id); }

    public List<User> findAll() { return userRepository.findAll(); }

}
