package com.example.api.repositories;

import com.example.api.models.Appointment;
import com.example.api.models.AppointmentExam;
import com.example.api.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentExamRepository extends JpaRepository<AppointmentExam, Long> {

    List<AppointmentExam> findByAppointment(Appointment appointment);

    List<AppointmentExam> findByExam(Exam exam);
}
