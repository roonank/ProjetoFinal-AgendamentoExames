package com.example.api.controllers;

import com.example.api.dtos.appointment.AppointmentCreateDTO;
import com.example.api.dtos.appointment.AppointmentDetailDTO;
import com.example.api.dtos.appointment.AppointmentSummaryDTO;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @PostMapping
    public ResponseEntity<AppointmentDetailDTO> create(
            @RequestParam Long userId,
            @RequestBody @Valid AppointmentCreateDTO dto
    ) {
        User user = getUserOrThrow(userId);
        AppointmentDetailDTO created = appointmentService.createAppointment(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentSummaryDTO>> listMyAppointments(@RequestParam Long userId) {
        User user = getUserOrThrow(userId);
        List<AppointmentSummaryDTO> list = appointmentService.listMyAppointments(user);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailDTO> getDetail(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        User user = getUserOrThrow(userId);
        AppointmentDetailDTO detail = appointmentService.getAppointmentDetail(id, user);
        return ResponseEntity.ok(detail);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(required = false) String reason
    ) {
        User user = getUserOrThrow(userId);
        appointmentService.cancelAppointment(id, reason, user);
        return ResponseEntity.noContent().build();
    }
}
