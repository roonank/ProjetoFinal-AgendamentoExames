package com.example.api.repositories;

import com.example.api.models.Appointment;
import com.example.api.models.LabUnit;
import com.example.api.models.User;
import com.example.api.models.enums.AppoitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser(User user);

    List<Appointment> findByUserAndStatus(User user, AppoitmentStatus appoitmentStatus);

    List<Appointment> findByLabUnitAndScheduledAtBetween(LabUnit labUnit, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByStatusAndScheduledAtBetween(AppoitmentStatus status, LocalDateTime start, LocalDateTime end);
}
