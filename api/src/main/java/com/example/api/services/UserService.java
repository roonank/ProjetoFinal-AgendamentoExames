package com.example.api.services;

import com.example.api.dtos.user.UserRegisterDTO;
import com.example.api.dtos.user.UserResponseDTO;
import com.example.api.models.User;
import com.example.api.models.enums.UserRole;
import com.example.api.records.UserCreatedEvent;
import com.example.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.user-routingkey}")
    private String userRoutingKey;

    //CREATE USUÁRIO
    public UserResponseDTO createUser(UserRegisterDTO dto){
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        LocalDate birthDate = null;
        if (dto.getBirthDate() != null && !dto.getBirthDate().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            birthDate = LocalDate.parse(dto.getBirthDate(), formatter);
        }

        User user = User.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .cpf(dto.getCpf())
                .address(dto.getAddress())
                .gender(dto.getGender())
                .birthDate(birthDate)
                .createdAt(LocalDateTime.now())
                .role(UserRole.CLIENT)
                .active(true)
                .build();

        user = userRepository.save(user);

        publishUserCreatedEvent(user);

        return toResponseDTO(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponseDTO toResponseDTO(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setCpf(user.getCpf());
        dto.setPhone(user.getPhone());
        dto.setBirthDate(user.getBirthDate());
        dto.setAddress(user.getAddress());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    private void publishUserCreatedEvent(User user) {
        var event = new UserCreatedEvent(
                user.getId(),
                user.getFullName(),
                user.getEmail()
        );

        rabbitTemplate.convertAndSend(exchange, userRoutingKey, event);
    }

    public Optional<User> findById(Long id) { return userRepository.findById(id); }

    public List<User> findAll() { return userRepository.findAll(); }

}
