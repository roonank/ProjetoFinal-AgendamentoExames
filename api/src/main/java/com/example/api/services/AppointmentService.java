package com.example.api.services;

import com.example.api.dtos.appointment.*;
import com.example.api.models.*;
import com.example.api.models.enums.AppointmentStatus;
import com.example.api.models.enums.UserRole;
import com.example.api.records.AppointmentCreatedEvent;
import com.example.api.repositories.AppointmentExamRepository;
import com.example.api.repositories.AppointmentRepository;
import com.example.api.repositories.ExamRepository;
import com.example.api.repositories.LabUnitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentExamRepository appoitmentExamRepository;
    private final LabUnitRepository labUnitRepository;
    private final ExamRepository examRepository;

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingkey}")
    private String routingkey;

    //---------------------------------------------------------------------------
    //CRUD DE AGENDAMENTO
    //---------------------------------------------------------------------------

    @Transactional
    public AppointmentDetailDTO createAppointment(AppointmentCreateDTO dto, User user) {
        LabUnit labUnit = labUnitRepository.findById(dto.getLabUnitId())
                .orElseThrow(() -> new IllegalArgumentException("Unidade de laboratório não encontrada"));

        ValidateAppointmentConflicts(labUnit, dto.getScheduledAt());

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setLabUnit(labUnit);
        appointment.setScheduledAt(dto.getScheduledAt());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes(dto.getNotes());
        appointment.setCreatedAt(LocalDateTime.now());

        List<AppointmentExam> items = dto.getExams().stream()
                .map(itemDto -> createAppointmentExam(itemDto, appointment))
                .collect(Collectors.toList());

        appointment.setExams(items);

        int totalDuration = items.stream()
                .map(AppointmentExam::getDurationDays)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
        appointment.setTotalDurationDays(totalDuration);

        appointmentRepository.save(appointment);

        eventAppointmentCreated(appointment);

        return toDetailDTO(appointment);
    }

    private AppointmentExam createAppointmentExam(AppointmentExamCreateDTO dto, Appointment appointment) {
        Exam exam = examRepository.findById(dto.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("Exame não encontrado"));

        AppointmentExam item = new AppointmentExam();
        item.setAppointment(appointment);
        item.setExam(exam);
        item.setDurationDays(exam.getDeliverTimeInDays());
        item.setPrice(exam.getPrice());
        item.setNote(dto.getNote());
        item.setStatus(AppointmentStatus.SCHEDULED);
        return item;
    }

    private void ValidateAppointmentConflicts(LabUnit labUnit, LocalDateTime scheduledAt) {
        LocalDateTime start = scheduledAt.minusHours(1);
        LocalDateTime end = scheduledAt.plusHours(1);

        List<Appointment> conflicts = appointmentRepository
                .findByLabUnitAndScheduledAtBetween(labUnit, start, end);

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Já existe outro agendamento nesse horário.");
        }
    }

    //LISTA DE AGENDAMENTOS DO USUÁRIO
    @Transactional
    public List<AppointmentSummaryDTO> listMyAppointments(User user) {
        return appointmentRepository.findByUser(user).stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    //DETALHES DO AGENDAMENTO
    @Transactional
    public AppointmentDetailDTO getAppointmentDetail(Long id, User currentUser){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (currentUser.getRole() == UserRole.CLIENT &&
         !appointment.getUser().getId().equals(currentUser.getId())){
            throw new SecurityException("Você não tem permissão para acessar este agendamento.");
        }

        return toDetailDTO(appointment);
    }

    //CANCELAR AGENDAMENTO
    @Transactional
    public void cancelAppointment(Long id, String cancelReason, User currentUser){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        if (currentUser.getRole() == UserRole.CLIENT &&
                !appointment.getUser().getId().equals(currentUser.getId())){
            throw new SecurityException("Você não tem permissão para cancelar este agendamento.");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setCancelReason(cancelReason);

        appointment.getExams()
                .forEach(item -> item.setStatus(AppointmentStatus.CANCELED));

        appointmentRepository.save(appointment);

        // Enviar mensagem para a fila RabbitMQ
    }


    //---------------------------------------------------------------------------
    //MAPPING PARA DTOS
    //---------------------------------------------------------------------------

    public AppointmentSummaryDTO toSummaryDTO(Appointment appointment){
        AppointmentSummaryDTO dto = new AppointmentSummaryDTO();
        dto.setId(appointment.getId());
        dto.setScheduledAt(appointment.getScheduledAt());
        dto.setStatus(appointment.getStatus());
        dto.setLabUnitName(appointment.getLabUnit().getName());

        List<String> examNames = appointment.getExams().stream()
                .map(item -> item.getExam().getName())
                .collect(Collectors.toList());
        dto.setExamNames(examNames);

        return dto;
    }

    public AppointmentDetailDTO toDetailDTO(Appointment appointment) {
        AppointmentDetailDTO dto = new AppointmentDetailDTO();
        dto.setId(appointment.getId());
        dto.setUserId(appointment.getUser().getId());
        dto.setUserName(appointment.getUser().getFullName());

        dto.setLabUnitId(appointment.getLabUnit().getId());
        dto.setLabUnitName(appointment.getLabUnit().getName());
        dto.setLabUnitAddress(appointment.getLabUnit().getAddress());

        dto.setScheduledAt(appointment.getScheduledAt());
        dto.setTotalDurationDays(appointment.getTotalDurationDays());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        dto.setCancelReason(appointment.getCancelReason());

        List<AppointmentExamResponseDTO> examsDtos = appointment.getExams().stream()
                .map(this::toExamResponseDTO)
                .collect(Collectors.toList());
        dto.setExams(examsDtos);

        return dto;
    }

    private AppointmentExamResponseDTO toExamResponseDTO(AppointmentExam item) {
        AppointmentExamResponseDTO dto = new AppointmentExamResponseDTO();
        dto.setId(item.getId());
        dto.setExamId(item.getExam().getId());
        dto.setExamName(item.getExam().getName());
        dto.setDeliverInDays(item.getDurationDays());
        dto.setPrice(item.getPrice());
        dto.setNote(item.getNote());
        dto.setStatus(item.getStatus().name());
        return dto;
    }


    //---------------------------------------------------------------------------
    //EVENTO DO RABBITMQ
    //---------------------------------------------------------------------------

    private void eventAppointmentCreated(Appointment appointment){
        var event = new AppointmentCreatedEvent(
                appointment.getId(),
                appointment.getUser().getId(),
                appointment.getLabUnit().getId(),
                appointment.getScheduledAt(),
                appointment.getUser().getEmail(),
                appointment.getUser().getFullName(),
                appointment.getLabUnit().getName(),
                appointment.getLabUnit().getAddress(),
                appointment.getExams().stream()
                        .map(item -> item.getExam().getName())
                        .toList()
        );

        rabbitTemplate.convertAndSend(exchange, routingkey, event);
    }


}